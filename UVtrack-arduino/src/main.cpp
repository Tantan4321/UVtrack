#include <Arduino.h>

#include "Adafruit_VEML6075.h"

Adafruit_VEML6075 uv_sensor = Adafruit_VEML6075();

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

  uv_sensor.setHighDynamic(true);

  

  
}

void loop() {
  // put your main code here, to run repeatedly:
}