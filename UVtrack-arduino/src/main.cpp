#include <Arduino.h>

#include <Wire.h>
#include <avr/sleep.h>
#include <avr/power.h>

#include "Adafruit_VEML6075.h"

Adafruit_VEML6075 uv_sensor = Adafruit_VEML6075();

const int read_delay = 1000; //sensor read delay time

void setup() {
  /**
   * Start of UV sensor init code
   */
  Serial.begin(115200);
  if(!uv_sensor.begin()){
    Serial.println("Failed to communicate with UV sensor!");
  }else{
    Serial.println("UV sensor found.");
  }

  uv_sensor.setIntegrationTime(VEML6075_100MS);
  
  /* DEBUGGING vvv
  Serial.print("Integration time set to ");
  switch (uv_sensor.getIntegrationTime()) {
    case VEML6075_50MS: Serial.print("50"); break;
    case VEML6075_100MS: Serial.print("100"); break;
    case VEML6075_200MS: Serial.print("200"); break;
    case VEML6075_400MS: Serial.print("400"); break;
    case VEML6075_800MS: Serial.print("800"); break;
  }
  Serial.println("ms"); 
  */

  //Reading rate configs
  uv_sensor.setHighDynamic(true);
  uv_sensor.setForcedMode(false);

  //calibration coeffs
  uv_sensor.setCoefficients(2.22, 1.33,  // UVA_A and UVA_B coefficients
                            2.95, 1.74,  // UVB_C and UVB_D coefficients
                            0.001461, 0.002591); // UVA and UVB responses

  //~~~~~~~~~~~~~~~~~~~~~~~~~~End UV sensor init~~~~~~~~~~~~~~~~~~~~~~
  
}

void loop() {
  Serial.print("UVA: "); Serial.println(uv_sensor.readUVA());
  Serial.print("UVB: "); Serial.println(uv_sensor.readUVB());
  Serial.print("UV Index: "); Serial.println(uv_sensor.readUVI());

  delay(read_delay);
}

/**
 * Sleep mode
 */
void sleep() {
    set_sleep_mode(SLEEP_MODE_IDLE);
    
    // Set Power Reduction register to disable timer 
    PRR = PRR | 0b00100000;
    
    power_adc_disable();
    power_spi_disable();
    power_timer0_disable();
    power_timer1_disable();
    power_timer2_disable();
    power_twi_disable();

    // Enter sleep mode
    sleep_enable();
    sleep_mode();
    
    // Return from sleep
    sleep_disable();
    
    // Re-enable timer
    PRR = PRR & 0b00000000;
    
    power_all_enable();
}