
//this is a modified version of the http server example in the wifly harlequin lib 

#include <WiFlyHQ.h> //harlequin wifly lib : https://github.com/harlequin-tech/WiFlyHQ
#include <SoftwareSerial.h>'
#include <Servo.h> 

int ledpin = 9;
int servoPin = 4;
char buf[80] ;
//rx,tx I'm using the arduino wireless sd shield, bended pins 0 and 1 on the shield and put the wifly module on it
// it wouldnt use the hardware serial and connected pins 0 and 1 on the shield to pins 8 and 9  
SoftwareSerial wifiSerial(8,9); 
WiFly wifly;  //The wireless RX-XV module
Servo myservo;
String command = "" ;  // command that comes in from android as a string it has the servo value

/* Change these to match your WiFi network */
const char mySSID[] = "TestNetwork";
const char myPassword[] = "pass";
const char ip[] = "192.168.1.11";
const char gateway[] = "192.168.1.1";

void setup()
{
  myservo.attach(servoPin);
  pinMode(ledpin, OUTPUT);
  Serial.begin(9600); 
  Serial.println(F("Starting"));
  Serial.print(F("Free memory: "));
  Serial.println(wifly.getFreeMemory(),DEC);
  wifiSerial.begin(19200); //WHY CANT I USE ANYTHING GREATER THAN 9600 BAUD RATE? WHY????????
  if (!wifly.begin(&wifiSerial, &Serial)) {
    Serial.println(F("Failed to start wifly"));
    wifly.setPort(2000);
    wifly.terminal();
  }

  /* Join wifi network if not already associated */
  if (!wifly.isAssociated()) {
    /* Setup the WiFly to connect to a wifi network */
    Serial.println(F("Joining network"));
    wifly.setPort(2000);

    wifly.enableDHCP();     
    wifly.setSSID(mySSID);
    wifly.setPassphrase(myPassword);
    wifly.setGateway(gateway);
    wifly.save();

    if (wifly.join()) {
      Serial.println(F("Joined wifi network"));
    } 
    else {
      Serial.println(F("Failed to join wifi network"));
      wifly.terminal();
    }
  } 
  else {
    Serial.println(F("Already joined network"));
  }

  wifly.setBroadcastInterval(0);	// Turn off UPD broadcast

  //wifly.terminal();

  Serial.print(F("MAC: "));
  Serial.println(wifly.getMAC(buf, sizeof(buf)));
  Serial.print(F("IP: "));
  Serial.println(wifly.getIP(buf, sizeof(buf)));

  wifly.setDeviceID("arduino-wifly");

  if (wifly.isConnected()) {
    Serial.println(F("Old connection active. Closing"));
    wifly.close();
  }

  wifly.setProtocol(WIFLY_PROTOCOL_TCP);
  wifly.setPort(2000);
  /* local port does not take effect until the WiFly has rebooted (2.32) */
  wifly.save();
  Serial.println(F("Set port to 2000, rebooting to make it work"));
  wifly.reboot();
  delay(3000);

  Serial.println(F("Ready"));
  Serial.println(wifly.getIP(buf, sizeof(buf)));
  
}


void loop()
{

  if (wifly.available() > 0) {
  
    int turn = wifly.parseInt();
    int dir = wifly.parseInt();
   
    if (wifly.read() == '\n') {
      
      Serial.print(turn);
      Serial.println(dir);
      
     
      //turn = constrain(turn, 0, 180);
      //dir =  constrain(dir, 1000, 1900);

      //myservo.write(turn);
      //myesc.writeMicroseconds(dir);
    }    
  }
}
