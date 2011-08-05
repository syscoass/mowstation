/*
  AnalogReadSerial
 Reads an analog input on pin 0, prints the result to the serial monitor 
 
 This example code is in the public domain.
 */

//P100NF04

int TURN_MIN =90;
int TURN_MAX =190;

int GO_MIN = 85;
int GO_MAX = 201;

int powerPin = 4;
int hornPin  = 8;
int slowDownPin  = 5;
int speedUpPin   = 2;

int serialLedPin = 12;
int powerLedPin = 13;

char serialCommand = '\0';

double centerVoltage = 127.5;
double udBounce = 0;
double lrBounce = 0;

int BOUNCE_WIDTH = 5;
double BOUNCE_RATE = 0.01;

//Motor Control Pins
int upDown_1 = 11;
int upDown_2 = 10;

int leftRight_1 = 9;
int leftRight_2 = 3;

boolean centered = true;
boolean lrcentered = true;
boolean udcentered = true;

boolean serialConnected = false;
int blinkWait = 0;

//Command Timeout
const int commandexpires = 100;  //milliseconds before direction command expires
unsigned long timenow;  // stores the present time for this cycle of loop
unsigned long lastime = 0;  // stores the last time voltage was checked
unsigned long lastcommand = 0;  //  last time we received a command
unsigned long serialtimeout = 0;  //  last time we received a command

byte command = 'S';

void setup() {
  //Initialize Motors
  analogWrite(leftRight_1,centerVoltage);  
  analogWrite(leftRight_2,centerVoltage);  
  
  analogWrite(upDown_1,centerVoltage);  
  analogWrite(upDown_2,centerVoltage);  
  
  Serial.begin(9600);
  
  //Initialize Motors
  analogWrite(upDown_1,centerVoltage);  
  analogWrite(upDown_2,centerVoltage); 
  
  analogWrite(leftRight_1,centerVoltage);  
  analogWrite(leftRight_2,centerVoltage);  
  
  pinMode(powerPin, OUTPUT);  
  pinMode(hornPin, OUTPUT);  
  pinMode(slowDownPin, OUTPUT);  
  pinMode(speedUpPin, OUTPUT);  
  
  pinMode(speedUpPin, OUTPUT);  
  
  pinMode(serialLedPin, OUTPUT); 
  pinMode(powerLedPin, OUTPUT); 
  
  digitalWrite(serialLedPin, LOW);   // set the LED on
  digitalWrite(powerLedPin, HIGH);   // set the LED on

  //Initialize Motors
  analogWrite(upDown_1,centerVoltage);  
  analogWrite(upDown_2,centerVoltage);  
  
  analogWrite(leftRight_1,centerVoltage);  
  analogWrite(leftRight_2,centerVoltage);  
  
  pinMode(upDown_1,OUTPUT);
  pinMode(upDown_2,OUTPUT);
  
  pinMode(leftRight_1,OUTPUT);
  pinMode(leftRight_2,OUTPUT);
  
  serialConnected = false;
  
  Serial.println("start");
  
  wait_for_connection();
}

void wait_for_connection()
{
   int count = 0;
   Serial.flush();
   while(count < 3)
   {
     if(Serial.available())
      {
         char b = Serial.read();
         
         if(b == 'Z')
         {
           count++;
         }
      }
   }
   
   Serial.flush();
   Serial.println("connected");
}

boolean decreaseMaxSpeed()
{
      digitalWrite(slowDownPin, HIGH);   // set the LED on
      delay(200);
      digitalWrite(slowDownPin, LOW);   // set the LED on
      
      return true;

}

boolean increaseMaxSpeed()
{
      digitalWrite(speedUpPin, HIGH);   // set the LED on
      delay(200);
      digitalWrite(speedUpPin, LOW);   // set the LED on
      
      return true;

}

boolean honkChairHorn(int length)
{
      digitalWrite(hornPin, HIGH);   // set the LED on
      delay(length);
      digitalWrite(hornPin, LOW);   // set the LED on
      
      return true;

}

boolean toggleChairPower()
{
        digitalWrite(powerPin, HIGH);   // set the LED on
        delay(200);
        digitalWrite(powerPin, LOW);   // set the LED on
        
        return true;
}

boolean isWheelchairOn()
{
  
  int sensorValue = analogRead(A0);
  if(sensorValue > 500)
  {
     return true; 
  }
  else{
     return false;
  }
  
}

void Stop()
{
  analogWrite(upDown_1,centerVoltage);  
  analogWrite(upDown_2,centerVoltage);  
  
  analogWrite(leftRight_1,centerVoltage);  
  analogWrite(leftRight_2,centerVoltage); 
  
  command = 'S';
  Serial.println("stop");
  
  centered = true;
  lrcentered = true;
  udcentered = true;
}

char parseData[10] ={0};
char parseCommand = '\0';
int  parseCount = 0;

void wait_for_end()
{
   boolean endFound = false;
  
   while(!endFound)
  {
   
     if(Serial.available())
     {
        char b = Serial.read(); 
        if(b == 'E')
        {
           endFound = true;
           Serial.println("ok");
        }
     }
   } 
}

int wait_for_value()
{
   boolean endFound = false;
   
  char tparseData[10] ={0};
  int count = 0;
  
   while(!endFound)
  {
   
     if(Serial.available())
     {
        char b = Serial.read(); 
        if(b == 'E')
        {
           endFound = true;
           Serial.println("ok");
           
           return atoi(tparseData);
        }else
        { 
          if(count < 10)
          {
            tparseData[count] = b;
          }
          
          count++;
        }
     }
   } 
   
   return 127;
}

void loop() {
  

  timenow = millis();
  if (timenow - lastcommand >= commandexpires) if(command != 'S'){
    Stop();  // stop if x time has passed since last command
    serialConnected = false;
  }else if(timenow - serialtimeout >= commandexpires)
  {
    serialConnected = false;  
  }
  
  while(Serial.available() > 0) {  
    serialtimeout = timenow;
    serialConnected = true;
    
    digitalWrite(serialLedPin, HIGH);   // set the LED on
  
    // read the incoming byte:  
    parseCommand = Serial.read(); 
   
      if(parseCommand == '1') // '1' CHAIR ON
      {
           toggleChairPower();
           wait_for_end();
      }
      else if(parseCommand == '3') // '3' HORN
      {
           honkChairHorn(400);
           wait_for_end();
      }
      else if(parseCommand == '4') // '4' --MAX SPEED
      {
           decreaseMaxSpeed();
           wait_for_end();
      }
      else if(parseCommand == '5') // '5' ++MAX SPEED
      {
           increaseMaxSpeed();
           wait_for_end();
      }
      else if(parseCommand == '?') // '5' ++MAX SPEED
      {
           if(isWheelchairOn())
           {
              Serial.println("yes");
           }else{
              Serial.println("no");
           }
           wait_for_end();
      }
      else if(parseCommand == 'F')
      {
          
          int value = wait_for_value();
          
          if(value > GO_MAX)
          {
            value = GO_MAX;
          }else if(value < GO_MIN)
          {
             value = GO_MIN; 
          }
          
          analogWrite(upDown_1,value); 
          analogWrite(upDown_2,value);  
         // Serial.print("UpDown:");
         // Serial.println(value);
          lastcommand = timenow;
          
          centered = false;
          
          command = 'F';
          udcentered = false;
      }
      else if(parseCommand == 'T') 
      {
          int value = wait_for_value();
          if(value > TURN_MAX)
          {
            value = TURN_MAX;
          }else if(value < TURN_MIN)
          {
             value = TURN_MIN; 
          }
              
          analogWrite(leftRight_1,value);
          analogWrite(leftRight_2,value);  
         // Serial.print("LeftRight:");
         // Serial.println(value);
          lastcommand = timenow;
          
          centered = false;
          
          command = 'T';
          lrcentered = false;
      }
      
      parseCommand = ' ';
  }
  
  digitalWrite(serialLedPin, LOW);   // set the LED off
  
  if(!serialConnected)
  {
     blinkWait = blinkWait+1;
     if(blinkWait%20000 == 0)
     {
       blinkWait = 0;
       digitalWrite(powerLedPin, HIGH);
     }else if(blinkWait%5000 == 0)
     {
       digitalWrite(powerLedPin, LOW);
     }
   }else{
     digitalWrite(powerLedPin, HIGH);
   }
}
