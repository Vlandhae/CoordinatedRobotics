/*
 * @Author: ELEGOO
 * @Date: 2019-10-22 11:59:09
 * @LastEditTime: 2020-06-28 14:55:26
 * @LastEditors: Changhua
 * @Description: SmartRobot robot tank
 * @FilePath: 
 */
#include <avr/wdt.h>
//#include <base64.hpp>
#include "DeviceDriverSet_xxx0.h"
#include "ApplicationFunctionSet_xxx0.h"
#include "math.h"


#define RX 0
#define TX 1

// Serial Communication
unsigned char buf[64]{0};
int buflen = 16;
uint8_t pos_buf = 0;
unsigned char nextStart;
int len;

void setup()
{
  Serial.begin(9600);
 
  Application_FunctionSet.ApplicationFunctionSet_Init();
  wdt_enable(WDTO_2S);
}

void loop()
{
  wdt_reset();
  Application_FunctionSet.ApplicationFunctionSet_SensorDataUpdate();
  Application_FunctionSet.ApplicationFunctionSet_KeyCommand();
  Application_FunctionSet.ApplicationFunctionSet_Obstacle();
  Application_FunctionSet.ApplicationFunctionSet_Blind();
  Application_FunctionSet.ApplicationFunctionSet_Rocker();
  Application_FunctionSet.ApplicationFunctionSet_Standby();
  Application_FunctionSet.ApplicationFunctionSet_IRrecv();
  Application_FunctionSet.WebSocketCarControl();
  Application_FunctionSet.DistanceCalculation();
  
  if(Serial.available())
  {
    while(Serial.available() > 0)
    {
      const auto len = Serial.readBytesUntil(';', buf, buflen);
      nextStart = Application_FunctionSet.handleWebsocketMessage(buf, len);
      
      // check if any partial data remains in the buffer
      if(nextStart != (pos_buf + len) )
      {
        // move the remaining data to the start of the buffer
        memcpy(buf, buf + nextStart, (pos_buf + len) - nextStart );
        pos_buf = (pos_buf + len) - nextStart;
      }else
      {
        pos_buf = 0;
      }
      for(int i = pos_buf; i < buflen; ++i)
      {
        buf[i] = 0;
      }
    }
  }
}

  

