#include <Servo.h>

/*
 RC PulseIn Serial Read out
 By: Nick Poole
 SparkFun Electronics
 Date: 5
 License: CC-BY SA 3.0 - Creative commons share-alike 3.0
 use this code however you'd like, just keep this license and
 attribute. Let me know if you make hugely, awesome, great changes.
 */
#define echoPin 7 // Echo Pin
#define trigPin 8 // Trigger Pin
#define LEDPin 13 // Onboard LED

Servo esc;
int throttlePin = 6;
int throttle;
int ch3;
boolean stopped = false;
int maximumRange = 200; // Maximum range needed
int minimumRange = 0; // Minimum range needed
long duration, distance; // Duration used to calculate distance


void setup() {
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(3, OUTPUT); // Set our input pins as such

  esc.attach(9);

  Serial.begin(9600); // Pour a bowl of Serial

}

void loop() {

  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2); 

  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 

  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);

  distance = duration/58.2;

  if((distance >= 0 && distance < 40) || distance > 500){
    esc.write(0);
  }

  ch3 = pulseIn(6, HIGH, 25000); // Read the pulse width of 
  throttle = map(ch3, 1100, 1890, 0, 179); 
  esc.write(throttle);

}
// 
//
// if((distance >0 && distance <= 30) && !stopped )
// {
//   analogWrite(3,10);
//   stopped = true ; 
//   //stop it
// }

// analogWrite(3,map(ch3,1000,1900,0,255));



//void readCommands()
//{
//
//  ch3 = pulseIn(6, HIGH, 25000); // Read the pulse width of 
// 
//  Serial.println("");
//
//  
//  if(ch1> 1400 && ch1<= 1500)
//  {
//   Serial.println("LEFT STICK MIDDLE");
//  }
//
//  if( ch1>= 1000 && ch1<= 1400)
//  {
//   Serial.println("LEFT STICK RIGHT TURN");
//  }
//
//  if(ch1<= 1900 && ch1>= 1500)
//  {
//   Serial.println("LEFT STICK LEFT TURN");
//  }
//
//  if(ch3>= 1400 && ch3< 1500)
//  {
//   Serial.println("RIGHT STICK MIDDLE");
//  }
//
//  if(ch3 >= 1000 && ch3<= 1400)
//  {
//   Serial.println("RIGHT STICK UP");
//  }
//
//  if(ch3>= 1500 && ch3<= 1900)
//  {
//   Serial.println("RIGHT STICK DOWN");
//  }
////delay(300);
////  int move = map(ch1, 1100,2000, -500, 500);
//  //    move = constrain(move, 0, 255);
//  //analogWrite(9, move);
//
//}

