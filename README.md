Wireless RC Car
=============

Driving a Hobby RC car using android + wifly RN-XV +arduino Uno

![alt arduino](http://imageshack.com/a/img31/8643/1fo9.jpg) ![alt android](http://imageshack.com/a/img59/6504/t7rr.png)


**remember to use a rotary encoder on the wheel or gps shield to measure speed and display it on the gui or maybe use a Hall effect sensor? http://playground.arduino.cc/Code/HallEffect

Wireless SD shield stacked on top of arduino Uno and the RX-XV module stacked on top of it. Pins 0 and 1 on the Wireless SD shield 
are bent so that it doesn't use the Arduino hardware serial and instead I connected them to pins 8 and 9 and used software serial for it,
 that leaves the hardware serial open for debugging output.
 
 So I basically wanted to control my RC car using Arduino and after doing some research I found one way to do it was to have arduino
 running as a TCP server and have and android App which would be the client send it PWM values for the Speed controller and the servo motor.
 
 I didn't want to need a wireless router in the way so what I did is I made android set up a wireless connection using the portable wifi
 hot-spot app and then bought an RN-XV wireless adapter for anduino and used it to connect arduino to the android's cellphone wireless network. 
 Once they are both on the network I can just send data from the android App to the Arduino via tcp make it drive me RC car.
 
 Using the wireless hot-spot app to have android create a wireless networks takes away the need to be in range of my home wifi signal and makes me 
 able to use this setup basically anywhere I want. All I have to do is send some values from android then convert those to valid PWM values and drive 
 my RC truck.
 
 
