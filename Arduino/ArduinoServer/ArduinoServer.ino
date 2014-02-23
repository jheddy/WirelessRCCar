
//this is a modified version of the http server example in the wifly harlequin lib 

#include <WiFlyHQ.h> //harlequin wifly lib : https://github.com/harlequin-tech/WiFlyHQ
#include <SoftwareSerial.h>'
#include <Servo.h> 

int ledpin = 6;
int servoPin = 4;
char buf[80] ;
SoftwareSerial wifiSerial(8,9); //rx,tx I'm using the arduino wireless sd shield, bended pins 0 and 1 on the shield and put the wifly module on it
                                // so it wouldnt use the hardware serial and connected pins 0 and 1 on the shield to pins 8 and 9   
WiFly wifly;
Servo myservo;

/* Change these to match your WiFi network */
const char mySSID[] = "test2";
const char myPassword[] = "supdudehey";

int convertToInt(String value){
  char buf[value.length()];
  value.toCharArray(buf,value.length());
  return atoi(buf);
}


void setup()
{
    myservo.attach(servoPin);
     pinMode(ledpin, OUTPUT);
    Serial.begin(9600);
    Serial.println(F("Starting"));
    Serial.print(F("Free memory: "));
    Serial.println(wifly.getFreeMemory(),DEC);
    wifiSerial.begin(9600);
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
	wifly.save();

	if (wifly.join()) {
	    Serial.println(F("Joined wifi network"));
	} else {
	    Serial.println(F("Failed to join wifi network"));
	    wifly.terminal();
	}
    } else {
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
}

String command = "" ;
void loop()
{
     
    if (wifly.available() > 0) {
        
         char c = wifly.read();
         command+=c ; 
         
         if(c == '\n')
         {
          if(command.indexOf("set:")==0){//if the command begins with "set:"
            String value=command;//store the command into a new String
            value.replace("set:", " "); //replace the "set:" from the command string
            Serial.println("Set a Servo:"+value);//output the servo value
            myservo.write(convertToInt(value));//set the servo to the recieved value
            analogWrite(ledpin,convertToInt(value) );
            
          }
          command="";//reset the commandline String
          
         }
           
  }
}







