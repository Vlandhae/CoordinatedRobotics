/*
 * @Author: ELEGOO
 * @Date: 2019-10-22 11:59:09
 * @LastEditTime: 2020-06-19 15:46:13
 * @LastEditors: Changhua
 * @Description: SmartRobot robot tank
 * @FilePath: 
 */
#ifndef _ApplicationFunctionSet_xxx0_H_
#define _ApplicationFunctionSet_xxx0_H_

#include <arduino.h>

class ApplicationFunctionSet
{
public:
  void ApplicationFunctionSet_Init(void);
  void DrivingDistance(int lr);
  void ApplicationFunctionSet_Obstacle(void);
  void ApplicationFunctionSet_Standby(void);
  void ApplicationFunctionSet_Spy(void);  
  void ApplicationFunctionSet_Blind(void);         
  void ApplicationFunctionSet_Rocker(void);
  void WebSocketCarControl(void);
  void ApplicationFunctionSet_SensorDataUpdate(void);   //Sensor Data Update
  void ApplicationFunctionSet_Bootup(void);
  void ApplicationFunctionSet_KeyCommand(void); 
  void ApplicationFunctionSet_IRrecv(void);
  void DistanceCalculation(void);
  void SetOccupancyGrid(void);
  boolean GetObstacleDistance(float y_tmp);
  void handleRemoteCommand(unsigned char* begin, int length);
  float handleWebsocketMessage(unsigned char* payload, int length);
  void setDoIt(void);

private:
  volatile float VoltageData_V;
  volatile uint16_t UltrasoundData_mm; //超声波数据
  volatile uint16_t UltrasoundData_cm; //超声波数据
  boolean VoltageDetectionStatus = false;
  boolean UltrasoundDetectionStatus = false;
public:
  const float VoltageDetection = 7.00;
  boolean Car_LeaveTheGround = true;
  const int ObstacleDetection = 20;
  uint8_t Rocker_CarSpeed = 250;
  uint8_t Rocker_temp;
};
extern ApplicationFunctionSet Application_FunctionSet;
#endif
