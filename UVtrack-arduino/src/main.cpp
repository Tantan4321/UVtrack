#include <Arduino.h>

#include "Adafruit_VEML6075.h"

Adafruit_VEML6075 uv_sensor = Adafruit_VEML6075();

const int read_delay = 1000; //sensor read delay time

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  if(!uv_sensor.begin()){
    Serial.println("Failed to communicate with UV sensor!");
  }else{
    Serial.println("UV sensor found.");
  }

  uv_sensor.setIntegrationTime(VEML6075_100MS);
  
  //DEBUGGING vvv
  Serial.print("Integration time set to ");
  switch (uv_sensor.getIntegrationTime()) {
    case VEML6075_50MS: Serial.print("50"); break;
    case VEML6075_100MS: Serial.print("100"); break;
    case VEML6075_200MS: Serial.print("200"); break;
    case VEML6075_400MS: Serial.print("400"); break;
    case VEML6075_800MS: Serial.print("800"); break;
  }
  Serial.println("ms"); 

  //Reading rate configs
  uv_sensor.setHighDynamic(true);
  uv_sensor.setForcedMode(false);

  //calibration coeffs
  uv_sensor.setCoefficients(2.22, 1.33,  // UVA_A and UVA_B coefficients
                            2.95, 1.74,  // UVB_C and UVB_D coefficients
                            0.001461, 0.002591); // UVA and UVB responses
  
}

void loop() {
  Serial.print("UVA reading: "); Serial.println(uv_sensor.readUVA());
  Serial.print("UVB reading: "); Serial.println(uv_sensor.readUVB());
  Serial.print("UV Index reading: "); Serial.println(uv_sensor.readUVI());

  delay(read_delay);
}