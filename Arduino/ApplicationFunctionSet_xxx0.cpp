/*
 * @Author: ELEGOO
 * @Date: 2019-10-22 11:59:09
 * @LastEditTime: 2020-06-28 14:10:45
 * @LastEditors: Changhua
 * @Description: SmartRobot robot tank
 * @FilePath:
 */
#include <hardwareSerial.h>
#include <stdio.h>
#include <string.h>
#include "ApplicationFunctionSet_xxx0.h"
#include "DeviceDriverSet_xxx0.h"
#include <math.h>
#include <avr/wdt.h>
#include <base64.hpp>

#include "ArduinoJson-v6.11.1.h" //ArduinoJson
#include "MPU6050_getdata.h"

#define RX 0
#define TX 1

#define _is_print 1
#define _Test_print 0

ApplicationFunctionSet Application_FunctionSet;

// Interrupt counter of wheels
int count_l;
int count_r;

// loop counter of main
int loop_count;

// wheel characteristics
float d /*diameter*/, s /*scope*/;
// distance
float d_l, d_r, d_tick /*distance per tick*/, d_avg;
float d_tmp;    // temp distance

// position x and y
float pos[2];
// Yaw
static float Yaw;
int yaw_servo;

// Obstacle
float d_UltraSonic = 110;  // distance ultrasonic-wheels: 60 mm
float obstacle_dist;
float obstacle_pos[2];
int obst[2];
int obst_m[100][2];

// Yaw of servo but in vehicle-yaw
float yaw_ges;
float pos_servo[2];
float d_c_s = 80;   // distance center-servo
float d_s_u = 20;   // distance servo-ultrasonic
float d_s_o;        // distance servo-obstacle


MPU6050_getdata AppMPU6050getdata;
DeviceDriverSet_Motor AppMotor;
DeviceDriverSet_ULTRASONIC AppULTRASONIC;
DeviceDriverSet_Servo AppServo;
DeviceDriverSet_Voltage AppVoltage;
DeviceDriverSet_Key AppKey;
DeviceDriverSet_IRrecv AppIRrecv;

/*f(x) int */
static boolean
function_xxx(long x, long s, long e) //f(x)
{
  if (s <= x && x <= e)
    return true;
  else
    return false;
}

enum SmartRobotCarMotionControl
{
  Forward,       //(1)
  Backward,      //(2)
  Left,          //(3)
  Right,         //(4)
  LeftForward,   //(5)
  LeftBackward,  //(6)
  RightForward,  //(7)
  RightBackward, //(8)
  stop_it        //(9)
};               //direction方向:（1）、（2）、 （3）、（4）、（5）、（6）


enum SmartRobotCarFunctionalModel
{
  Standby_mode,          
  TraceBased_mode,        
  ObstacleAvoidance_mode, 
  Follow_mode,           
  Rocker_mode,            
  Spy_mode,
  Blind_mode
};

struct Application_xxx
{
  SmartRobotCarMotionControl Motion_Control;
  SmartRobotCarFunctionalModel Functional_Mode;
};

Application_xxx Application_SmartRobotCarxxx0;;
void ApplicationFunctionSet_SmartRobotCarLinearMotionControl(SmartRobotCarMotionControl direction, uint8_t directionRecord, uint8_t speed, uint8_t Kp, uint8_t UpperLimit);
void ApplicationFunctionSet_SmartRobotCarMotionControl(SmartRobotCarMotionControl direction, uint8_t is_speed);



// Interrupts
static void Left_Tick()
{
  count_l++;
}

static void Right_Tick()
{
  count_r++;
}

void ApplicationFunctionSet::ApplicationFunctionSet_Init(void)
{
  ApplicationFunctionSet Application_FunctionSet;
  attachInterrupt(digitalPinToInterrupt(18), Left_Tick, RISING);
  attachInterrupt(digitalPinToInterrupt(19), Right_Tick, RISING);
  count_l = 0;
  count_r = 0;
  loop_count = 0;

  d = 65;         // diameter of wheel is 65 mm;
  s = M_PI * d;   // scope = 204.2 mm
  d_tick = s/20;  // 20 holes, 20 ticks is one rotation (10.21 mm)
  yaw_servo = 90;

  d_l = 0;
  d_r = 0;
  d_avg = 0;
  d_tmp = 0;
  bool res_error = true;
  Serial.begin(9600);
  AppVoltage.DeviceDriverSet_Voltage_Init();
  AppMotor.DeviceDriverSet_Motor_Init();
  AppULTRASONIC.DeviceDriverSet_ULTRASONIC_Init();
  res_error = AppMPU6050getdata.MPU6050_dveInit();
  AppMPU6050getdata.MPU6050_calibration();
  AppIRrecv.DeviceDriverSet_IRrecv_Init();
  AppKey.DeviceDriverSet_Key_Init();
  AppServo.DeviceDriverSet_Servo_Init(yaw_servo);

  Application_SmartRobotCarxxx0.Functional_Mode = Rocker_mode;
}

void ApplicationFunctionSet::loop(void)
{
  loop_count++;
}

int tmp_l=1;
int tmp_r=1;
SmartRobotCarMotionControl dir_tmp;

void ApplicationFunctionSet::DistanceCalculation(void)
{
  SmartRobotCarMotionControl direction = Application_SmartRobotCarxxx0.Motion_Control;
  
  if(Application_SmartRobotCarxxx0.Functional_Mode == Spy_mode)
  {
    direction = dir_tmp;
  }

  AppMPU6050getdata.MPU6050_dveGetEulerAngles(&Yaw);
  Yaw = Yaw / 180 * PI;

  switch (direction)
  {
  case /* constant-expression */ Forward:
    tmp_l = 1;
    tmp_r = 1;
    break;
  case /* constant-expression */ Backward:
    tmp_l = -1;
    tmp_r = -1;
    break;
  default:
    break;
  }
  
  d_l = d_tick*count_l*tmp_l;
  d_r = d_tick*count_r*tmp_r;
  d_avg = (d_l+d_r)/2;
  pos[0] += d_avg * cos(Yaw);
  pos[1] += d_avg * sin(Yaw);
  count_l = 0;
  count_r = 0;

  return;
}

void sendPositionData(){
  Serial.write('P');
  Serial.write(obst[0]);
  Serial.write(obst[1]);
  Serial.write('1');
  Serial.write(';');
}

void ApplicationFunctionSet::SetOccupancyGrid(void)
{
  AppMPU6050getdata.MPU6050_dveGetEulerAngles(&Yaw);
  Yaw = Yaw / 180 * PI;

  // Position des Servos
  pos_servo[0] = (d_c_s * cos(Yaw)) + pos[0];
  pos_servo[1] = (d_c_s * sin(Yaw)) + pos[1];

  yaw_ges = 90 - yaw_servo;  

  d_s_o = d_s_u + obstacle_dist*10;

  // x, y des Objekts
  obstacle_pos[0] = pos_servo[0] + (cos(yaw_ges) * d_s_o);
  obstacle_pos[1] = pos_servo[1] + (sin(yaw_ges) * d_s_o);


  int step_x = (obstacle_pos[0]/10) / 5 + 1;
  int step_y = (obstacle_pos[1]/10) / 5 + 1;

  if (abs(step_x) > 50 || abs(step_y) > 50)
  {
    Serial.println("Out of bounds");
  }
  else{
    //occ_grid[step_x + sizeof(occ_grid[0])/2][step_y + sizeof(occ_grid[0])/2] = true;
    obst[0] = step_x + 50;
    obst[1] = step_y + 50;
    //Serial.println(obst[0]);
    sendPositionData();
    // an Server
  }

  return;
}


boolean ApplicationFunctionSet::GetObstacleDistance(float y_tmp)
{
  float y = y_tmp;
  float pos_tmp[2];
  float pos_servo_tmp[2];
  float d_s_o_tmp;

  int step_x;
  int step_y;

  pos_servo_tmp[0] = (d_c_s * cos(Yaw)) + pos[0];
  pos_servo_tmp[1] = (d_c_s * sin(Yaw)) + pos[1];

  for(int step = 1; step <= 4; step ++)
  {
    d_s_o_tmp = step*70;
    
    pos_tmp[0] = pos_servo_tmp[0] + (cos(y) * d_s_o_tmp);
    pos_tmp[1] = pos_servo_tmp[1] + (sin(y) * d_s_o_tmp);

    step_x = (pos_tmp[0]/10) / 5 + 1;
    step_y = (pos_tmp[1]/10) / 5 + 1;

    Serial.println(obst_m[0][0]);
    Serial.println(obst_m[0][1]);
    Serial.println(step_x);
    Serial.println(step_y);
    
  } 

  return false;

}

static void delay_xxx(uint16_t _ms)
{
  wdt_reset();
  for (unsigned long i = 0; i < _ms; i++)
  {
    delay(1);
  }
}

// Obstacle Avoidance Mode / Spy mode

void ApplicationFunctionSet::ApplicationFunctionSet_Obstacle(void)
{

  static boolean first_is = true;
  // if (Application_SmartRobotCarxxx0.Functional_Mode == ObstacleAvoidance_mode)
  if (Application_SmartRobotCarxxx0.Functional_Mode == Spy_mode)
  {
    uint8_t switc_ctrl = 0;
    uint16_t get_Distance;
    if (first_is == true) //Enter the mode for the first time, and modulate the steering gear to 90 degrees
    {
      yaw_servo = 90;
      AppServo.DeviceDriverSet_Servo_control(yaw_servo /*Position_angle*/);
      first_is = false;
    }

    AppULTRASONIC.DeviceDriverSet_ULTRASONIC_Get(&get_Distance /*out*/);


    if (function_xxx(get_Distance, 0, 20))
    {
      obstacle_dist = get_Distance;
      ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);
      dir_tmp = stop_it;
      Application_FunctionSet.SetOccupancyGrid();
      for (uint8_t i = 1; i < 6; i += 2) //1、3、5 Omnidirectional detection of obstacle avoidance status
      {
        yaw_servo = 30 * i;
        AppServo.DeviceDriverSet_Servo_control(yaw_servo /*Position_angle*/);
        delay_xxx(1);
        AppULTRASONIC.DeviceDriverSet_ULTRASONIC_Get(&get_Distance /*out*/);


        if (function_xxx(get_Distance, 0, 20))
        {
          ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);
          dir_tmp = stop_it;
          obstacle_dist = get_Distance;
          Application_FunctionSet.SetOccupancyGrid();
          if (5 == i)
          {
            ApplicationFunctionSet_SmartRobotCarMotionControl(Backward, 50);
            dir_tmp = Backward;
            delay_xxx(500);
            ApplicationFunctionSet_SmartRobotCarMotionControl(Right, 50);
            dir_tmp = Right;
            delay_xxx(50);
            first_is = true;
            break;
          }
        }
        else
        {
          switc_ctrl = 0;
          switch (i)
          {
          case 1:
            ApplicationFunctionSet_SmartRobotCarMotionControl(Right, 50);
            dir_tmp = Right;
            delay_xxx(50);
            break;
          case 3:
            ApplicationFunctionSet_SmartRobotCarMotionControl(Forward, 50);
            dir_tmp = Forward;
            delay_xxx(50);
            break;
          case 5:
            ApplicationFunctionSet_SmartRobotCarMotionControl(Left, 50);
            dir_tmp = Left;
            delay_xxx(50);
            break;
          }

          first_is = true;
          break;
        }

      }
    }
    else //if (function_xxx(get_Distance, 20, 50))
    {
      ApplicationFunctionSet_SmartRobotCarMotionControl(Forward, 50);
      dir_tmp = Forward;
    }
  }
  else
  {
    first_is = true;
  }

}

boolean do_it = false;
void ApplicationFunctionSet::setDoIt(void)
{
  do_it = true;
}

int matrix_i = 0;
void ApplicationFunctionSet::ApplicationFunctionSet_Blind(void)
{
  
  static boolean first_is = true;
  
  if(Application_SmartRobotCarxxx0.Functional_Mode == Blind_mode)
  {
    
    uint16_t get_Distance;
    float y_tmp;

    if (first_is == true)
    {
      //obst_m[:][:] = 0;
      matrix_i = 0;
      pos[0] = 0;
      pos[1] = 0;
      ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);
      dir_tmp = stop_it;
      
      Serial.println("test");
      //AppServo.DeviceDriverSet_Servo_control(90 /*Position_angle*/);
      // Geradeaus nach vorne blicken
      y_tmp = Yaw;
      first_is = false;
    }

    if(do_it == true)
    {
      ApplicationFunctionSet_SmartRobotCarMotionControl(Forward, 50);
      dir_tmp = Forward;
      get_Distance = Application_FunctionSet.GetObstacleDistance(y_tmp);

      if (get_Distance == true)
      {
        ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);

        for (uint8_t i = 1; i < 6; i += 2) //1、3、5 Omnidirectional detection of obstacle avoidance status
        {
          y_tmp = 30 * i;
          delay_xxx(1);
          get_Distance = Application_FunctionSet.GetObstacleDistance(y_tmp);


          if (get_Distance == true)
          {
            ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);
            if (5 == i)
            {
              ApplicationFunctionSet_SmartRobotCarMotionControl(Backward, 50);
              delay_xxx(500);
              ApplicationFunctionSet_SmartRobotCarMotionControl(Right, 50);
              delay_xxx(50);
              first_is = true;
              break;
            }
          }
          else
          {
            switch (i)
            {
            case 1:
              ApplicationFunctionSet_SmartRobotCarMotionControl(Right, 50);
              delay_xxx(50);
              break;
            case 3:
              ApplicationFunctionSet_SmartRobotCarMotionControl(Forward, 50);
              delay_xxx(50);
              break;
            case 5:
              ApplicationFunctionSet_SmartRobotCarMotionControl(Left, 50);
              delay_xxx(50);
              break;
            }

            first_is = true;
            break;
          }

        }
      }
      else
      {
        ApplicationFunctionSet_SmartRobotCarMotionControl(Forward, 50);
      }
    }
    else{
      return;
    }
  }
  else
  {
    first_is = true;
    do_it = false;
  }
  return;
}

static void ApplicationFunctionSet_SmartRobotCarLinearMotionControl(SmartRobotCarMotionControl direction, uint8_t directionRecord, uint8_t speed, uint8_t Kp, uint8_t UpperLimit)
{
  static float yaw_So = 0;
  static uint8_t en = 110;
  static unsigned long is_time;
  if (en != directionRecord || millis() - is_time > 10)
  {
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_void, /*speed_A*/ 0,
                                           /*direction_B*/ direction_void, /*speed_B*/ 0, /*controlED*/ control_enable); //Motor control
    AppMPU6050getdata.MPU6050_dveGetEulerAngles(&Yaw);
    is_time = millis();
  }
  //if (en != directionRecord)
  if (en != directionRecord)
  {
    en = directionRecord;
    yaw_So = Yaw;
  }
  //Add proportional constant Kp to increase rebound effect
  int R = (Yaw - yaw_So) * Kp + speed;
  if (R > UpperLimit)
  {
    R = UpperLimit;
  }
  else if (R < 10)
  {
    R = 10;
  }
  int L = (yaw_So - Yaw) * Kp + speed;
  if (L > UpperLimit)
  {
    L = UpperLimit;
  }
  else if (L < 10)
  {
    L = 10;
  }
  if (direction == Forward) //前进
  {
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_just, /*speed_A*/ R,
                                           /*direction_B*/ direction_just, /*speed_B*/ L, /*controlED*/ control_enable);
  }
  else if (direction == Backward) //后退
  {
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_back, /*speed_A*/ L,
                                           /*direction_B*/ direction_back, /*speed_B*/ R, /*controlED*/ control_enable);
  }
}

static void ApplicationFunctionSet_SmartRobotCarMotionControl(SmartRobotCarMotionControl direction, uint8_t is_speed)
{

  static uint8_t directionRecord = 0;
  uint8_t Kp, UpperLimit;
  uint8_t speed = is_speed;

  //COntrolling the Modes
  switch (Application_SmartRobotCarxxx0.Functional_Mode)
  {
  case Rocker_mode:
    Kp = 10;
    UpperLimit = 100;
    break;
  case Spy_mode:
    Kp = 2;
    UpperLimit = 50;
    break;
  case Blind_mode:
    Kp = 2;
    UpperLimit = 50;
    break;
  default:
    Kp = 10;
    UpperLimit = 255;
    break;
  }

  switch (direction)
  {
  case Forward:
    if (Application_SmartRobotCarxxx0.Functional_Mode == TraceBased_mode)
    {
      AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_just, /*speed_A*/ speed,
                                             /*direction_B*/ direction_just, /*speed_B*/ speed, /*controlED*/ control_enable); //Motor control
     
    }
    else
    {

      ApplicationFunctionSet_SmartRobotCarLinearMotionControl(Forward, directionRecord, speed, Kp, UpperLimit);
      // DistanceCalculation(Forward);
      directionRecord = 1;
    }

    break;
  case Backward:
    if (Application_SmartRobotCarxxx0.Functional_Mode == TraceBased_mode)
    {

      AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_back, /*speed_A*/ speed,
                                             /*direction_B*/ direction_back, /*speed_B*/ speed, /*controlED*/ control_enable); //Motor control

    }
    else
    {

      ApplicationFunctionSet_SmartRobotCarLinearMotionControl(Backward, directionRecord, speed, Kp, UpperLimit);
      // DistanceCalculation(Backward);
      directionRecord = 2;
    }

    break;
  case Left:
    directionRecord = 3;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_just, /*speed_A*/ UpperLimit,
                                           /*direction_B*/ direction_back, /*speed_B*/ UpperLimit, /*controlED*/ control_enable); //Motor control

    break;
  case Right:
    directionRecord = 4;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_back, /*speed_A*/ UpperLimit,
                                           /*direction_B*/ direction_just, /*speed_B*/ UpperLimit, /*controlED*/ control_enable); //Motor control

    break;
  case LeftForward:
    directionRecord = 5;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_just, /*speed_A*/ speed,
                                           /*direction_B*/ direction_just, /*speed_B*/ speed / 2, /*controlED*/ control_enable); //Motor control
    break;
  case LeftBackward:
    directionRecord = 6;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_back, /*speed_A*/ speed,
                                           /*direction_B*/ direction_back, /*speed_B*/ speed / 2, /*controlED*/ control_enable); //Motor control
    break;
  case RightForward:
    directionRecord = 7;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_just, /*speed_A*/ speed / 2,
                                           /*direction_B*/ direction_just, /*speed_B*/ speed, /*controlED*/ control_enable); //Motor control
    break;
  case RightBackward:
    directionRecord = 8;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_back, /*speed_A*/ speed / 2,
                                           /*direction_B*/ direction_back, /*speed_B*/ speed, /*controlED*/ control_enable); //Motor control
    break;
  case stop_it:
    directionRecord = 9;
    AppMotor.DeviceDriverSet_Motor_control(/*direction_A*/ direction_void, /*speed_A*/ 0,
                                           /*direction_B*/ direction_void, /*speed_B*/ 0, /*controlED*/ control_enable); //Motor control
    //DistanceCalculation(stop_it);
    break;
  default:
    directionRecord = 10;
    break;
  }
}


uint8_t command_ws;
void ApplicationFunctionSet::WebSocketCarControl()
{
  switch ( command_ws )
  {
    case 0:
      break;
    case /* constant-expression */ 1:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Forward;
      break;
    case /* constant-expression */ 2:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Backward;
      break;
    case /* constant-expression */ 3:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Left;
      break;
    case /* constant-expression */ 4:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Right;
      break;
    case /* constant-expression */ 5:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Rocker_mode;
      break;
    case /* constant-expression */ 6:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Spy_mode;
      break;
    case /* constant-expression */ 7:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Blind_mode;
      break;
    default:
      Application_SmartRobotCarxxx0.Functional_Mode = Standby_mode;
      break;
  }

  
}

/*Rocker control mode*/
void ApplicationFunctionSet::ApplicationFunctionSet_Rocker(void)
{
  if (Application_SmartRobotCarxxx0.Functional_Mode == Rocker_mode)
  {
    ApplicationFunctionSet_SmartRobotCarMotionControl(Application_SmartRobotCarxxx0.Motion_Control /*direction*/, Rocker_CarSpeed /*speed*/);

  }
}

/*
 Robot car update sensors' data:Partial update (selective update)
*/
void ApplicationFunctionSet::ApplicationFunctionSet_SensorDataUpdate(void)
{

  // AppMotor.DeviceDriverSet_Motor_Test();
  { /*Battery voltage status update*/
    static unsigned long VoltageData_time = 0;
    static int VoltageData_number = 1;
    if (millis() - VoltageData_time > 10) //read and update the data per 10ms
    {
      VoltageData_time = millis();
      VoltageData_V = AppVoltage.DeviceDriverSet_Voltage_getAnalogue();
      if (VoltageData_V < VoltageDetection)
      {
        VoltageData_number++;
        if (VoltageData_number == 500) //Continuity to judge the latest voltage value multiple
        {
          VoltageDetectionStatus = true;
          VoltageData_number = 0;
        }
      }
      else
      {
        VoltageDetectionStatus = false;
      }
    }
  }
}

void ApplicationFunctionSet::ApplicationFunctionSet_Bootup(void)
{
  Application_SmartRobotCarxxx0.Functional_Mode = Standby_mode;
}

/*Standby mode*/
void ApplicationFunctionSet::ApplicationFunctionSet_Standby(void)
{
  static bool is_ED = true;
  static uint8_t cout = 0;
  if (Application_SmartRobotCarxxx0.Functional_Mode == Standby_mode)
  {
    //Application_FunctionSet.SetOccupancyGrid();

    //Serial.print("Standby");
    ApplicationFunctionSet_SmartRobotCarMotionControl(stop_it, 0);
    if (true == is_ED) //Used to zero yaw raw data(Make sure the car is placed on a stationary surface!)
    {
      static unsigned long timestamp; //acquire timestamp
      if (millis() - timestamp > 20)
      {
        if (cout > 10)
        {
          is_ED = false;
          AppMPU6050getdata.MPU6050_calibration();
        }
      }
    }
  }
}

/*Key command*/
void ApplicationFunctionSet::ApplicationFunctionSet_KeyCommand(void)
{
  uint8_t get_keyValue;

  static uint8_t temp_keyValue = keyValue_Max;
  AppKey.DeviceDriverSet_key_Get(&get_keyValue);

  if (temp_keyValue != get_keyValue)
  {
    temp_keyValue = get_keyValue;
    switch (get_keyValue)
    {
    case /* constant-expression */ 1:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Spy_mode;
      command_ws = 6;
      break;
    case /* constant-expression */ 2:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Blind_mode;
      Serial.write('O');
      Serial.write(';');
      Serial.println("ähem");
      command_ws = 7;
      break;
    case /* constant-expression */ 3:
      /* code */
      Application_SmartRobotCarxxx0.Functional_Mode = Standby_mode;
      command_ws = 5;
      break;
    default:

      break;
    }
  }
}


/*Infrared remote control*/
void ApplicationFunctionSet::ApplicationFunctionSet_IRrecv(void)
{
  SmartRobotCarMotionControl  m_old;
  uint8_t IRrecv_button;
  bool IRrecv_button_tmp;
  static bool IRrecv_en = false;
  
  if (AppIRrecv.DeviceDriverSet_IRrecv_Get(&IRrecv_button /*out*/))
  {
    IRrecv_en = true;
    IRrecv_button_tmp = IRrecv_en;
  }
  else{
    Application_SmartRobotCarxxx0.Motion_Control = stop_it;
  }
  if (true == IRrecv_en)
  {
    // Bewegungsrichtung ändern
    switch (IRrecv_button)
    {
    case /* constant-expression */ 1:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Forward;
       m_old = Application_SmartRobotCarxxx0.Motion_Control;
      // DistanceCalculation(Forward);
      break;
    case /* constant-expression */ 2:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Backward;
      // DistanceCalculation(Backward);
      break;
    case /* constant-expression */ 3:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Left;
      // DistanceCalculation(Left);
      break;
    case /* constant-expression */ 4:
      /* code */
      Application_SmartRobotCarxxx0.Motion_Control = Right;
      break;
    case /* constant-expression */ 5:
      /* code */

      Application_SmartRobotCarxxx0.Functional_Mode = Rocker_mode;
      break;
    case /* constant-expression */ 6:
      ///* code */ Application_SmartRobotCarxxx0.Functional_Mode = ObstacleAvoidance_mode;
      /* code */ Application_SmartRobotCarxxx0.Functional_Mode = Spy_mode;
      break;
    case /* constant-expression */ 7:
      /* code */ Application_SmartRobotCarxxx0.Functional_Mode = Blind_mode;
      break;
    case /* constant-expression */ 9:
    {
      if (Rocker_CarSpeed < 255)
      {
        Rocker_CarSpeed += 5;
      }
    }
    break;
    case /* constant-expression */ 10:
    {
      Rocker_CarSpeed = 250;
    }
    break;
    case /* constant-expression */ 11:
    {
      if (Rocker_CarSpeed > 50)
      {
        Rocker_CarSpeed -= 5;
      }
    }
    break;

    default:
      Application_SmartRobotCarxxx0.Functional_Mode = Standby_mode;
      break;
    }
    /*achieve time-limited control on movement direction part*/
    if (IRrecv_button < 5)
    {
      Application_SmartRobotCarxxx0.Functional_Mode = Rocker_mode;
      if (millis() - AppIRrecv.IR_PreMillis > 300)
      {
        IRrecv_en = false;
        Application_SmartRobotCarxxx0.Functional_Mode = Rocker_mode;
        AppIRrecv.IR_PreMillis = millis();
      }
    }
    else
    {
      IRrecv_en = false;
      AppIRrecv.IR_PreMillis = millis();
    }
    m_old = Application_SmartRobotCarxxx0.Motion_Control;
  }
}

// Server Functions

void ApplicationFunctionSet::handleRemoteCommand(unsigned char* begin, int length)
{
  if(length != 2)
  {
    return;
  }
  unsigned char command1 = begin[1];
  
    //Handle comand
    switch(command1)
    {
      case 'I':
        //IDLE
        command_ws = 5;
        break;
      case 'U':
        //Up
        command_ws = 1;
        break;
      case 'D':
        //Down
        command_ws = 2;
        break;
      case 'L':
        //Left
        command_ws = 3;
        break;
      case 'R':
        //Right
        command_ws = 4;
        break;
      case 'Z':
        command_ws = 0;
        break;
      case 'S':
        command_ws = 6;
        break;
      case 'B':
        command_ws = 7;
        break;
      default:
        break;
    }
}


void createMatrix(char x, char y)
{
  obst_m[matrix_i][0] = x;
  obst_m[matrix_i][1] = y;
  Serial.println(obst_m[matrix_i][0]);
  Serial.println(obst_m[matrix_i][1]);
  matrix_i ++;
}

char type;
float ApplicationFunctionSet::handleWebsocketMessage(unsigned char* payload, int length)
{
  String current = "";
  int i = 0;
  unsigned char* begin;
  int currentLength;
  int nextStart = 0;
  while(i < length)
  {
    char c;
    begin = payload + i;
    currentLength = 0;
    while( i < length && (c = payload[i++]) != ';')
    {
      //Serial.printf("%d %02x%c\n", i,c,c);
      current += c;
      currentLength += 1;
    }
    //TODO update
    if(i != 0 && payload[i-1] == ';'){
      nextStart = i;
    }
    type = current.charAt(0);
    switch(type)
    {
      case 'M':
        createMatrix(current.charAt(1), current.charAt(2));
        break;
      case 'C':
        Application_FunctionSet.handleRemoteCommand(begin, currentLength);
        break;
      default:
        Serial.println("Unknown message type");
        break;
    }
    current = "";
  }
  return nextStart;
}



