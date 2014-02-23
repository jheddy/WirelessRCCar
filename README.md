Wireless RC Car
=============

Driving a Hobby RC car using android + wifly RN-XV +arduino Uno

![alt arduino](http://imageshack.com/a/img31/8643/1fo9.jpg) ![alt android](http://imageshack.com/a/img59/6504/t7rr.png)



Wireless SD shield stacked on top of arduino Uno and the RX-XV module stacked on top of it. Pins 0 and 1 on the Wireless SD shield 
are bent so that it doesn't use the Arduino hardware serial and instead I connected them to pins 8 and 9 and used software serial for it,
 that leaves the hardware serial open for debugging output.
