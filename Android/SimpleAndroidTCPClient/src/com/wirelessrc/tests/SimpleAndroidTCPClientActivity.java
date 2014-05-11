/*
 * Android Tcp Client to connect to arduino TCP server and send pwm
 * values to control an RC car's speed controller and servo 
 * 
 * 
 * this code was written by......
 * 20.04.2012
 * by Laurid Meyer
 * http://www.lauridmeyer.com
 * 
 * And will be modified by me Jheddy Melendez to control my RC car
 * over wireless  
 * 
 */
package com.wirelessrc.tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import com.wirelessrc.tests.R;
import com.MobileAnarchy.Android.Widgets.Joystick.DualJoystickView;
import com.MobileAnarchy.Android.Widgets.Joystick.JoystickMovedListener;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SimpleAndroidTCPClientActivity extends Activity {
	TextView textlog;//Log for outputs
	
	Button buttonConnect;//(dis)connect Button
	//SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar..
	EditText ip; //server ip to connect to
	EditText port;
	DualJoystickView joystick; //joystick to send pwm values
	//JoystickView joystick2;
	
	Boolean connected=false;//stores the connectionstatus
	
	DataOutputStream dataOutputStream = null;//outputstream to send commands
	Socket socket = null;//the socket for the connection
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    
        //connect the view and the objects
	    buttonConnect = (Button)findViewById(R.id.connect);
	    textlog = (TextView)findViewById(R.id.textlog);
	   // seekBar = (SeekBar)findViewById(R.id.seekbar);
	    //seekBarValue = (TextView)findViewById(R.id.seekbarvalue);
	    //ip = (EditText)findViewById(R.id.ipaddr);
	    
	    joystick = (DualJoystickView) findViewById(R.id.dualjoystickView);
	    joystick.setOnJostickMovedListener(null, null);
	    //joystick2 = new JoystickView(this);
	    //RelativeLayout mainLayout1 = (RelativeLayout) findViewById(R.id.joysticklayout);

	    //mainLayout1.addView(joystick);
	    //mainLayout1.addView(joystick2);
	    
	    textlog.setMovementMethod(new ScrollingMovementMethod());
	    
	    	    
	    
	    
	    textlog.setText("Starting Client");//log that the App launched
	    changeConnectionStatus(false);//change connectionstatus to "disconnected"
	    
	    //Eventlisteners
	    buttonConnect.setOnClickListener(buttonConnectOnClickListener);
	    //seekBar.setOnSeekBarChangeListener(seekbarchangedListener);
	    joystick.setOnJostickMovedListener(joystickmovedlistener,joystickmovedlistener);
	    //joystick2.setOnJostickMovedListener(joystickmovedlistener);
    }
    
    
    // ----------------------- Joystick EVENTLISTENER - begin ----------------------------
    JoystickMovedListener joystickmovedlistener = new JoystickMovedListener(){

		@Override
		public void OnMoved(int pan, int tilt) {
			//use pan value for servo and titlt for speed controller
			Log.d("JOYSTICK", "Pan: "+ pan +" Tilt: "+tilt); 
			
		}

		@Override
		public void OnReleased() {
			Log.d("JOYSTICK", "Joystick Released");//send stop signal to car
			
		}

		@Override
		public void OnReturnedToCenter() {
			Log.d("JOYSTICK", "Joystick Back to center");//send stop signal to car
			
		}};
    
    
    
    
    // ----------------------- Joystick EVENTLISTENER - end ----------------------------

    
    
    
    
    
    
    
    
    
    
    // ----------------------- SEEKBAR EVENTLISTENER - begin ----------------------------
    SeekBar.OnSeekBarChangeListener seekbarchangedListener = new SeekBar.OnSeekBarChangeListener(){
    	//Methd is fired everytime the seekbar is changed
    	@Override
 	   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    		String valueOfseekbar = String.valueOf(progress);//save the value of the seekbar in a string
    		seekBarValue.setText(valueOfseekbar);//update the value in the textfield
    		
	 	    if(connected){//if the socket is connected
	 	    	try {
	 	    		//send a string to the Arduino Server in the form of "set: -seekbarvalue- \n"
	 	    		dataOutputStream.writeBytes("set:"+valueOfseekbar+'\n');
	 	    	}catch (UnknownHostException e) {//catch and
	 	    		outputText(e.getMessage());//display errors
	 			} catch (IOException e) {//catch and
	 				outputText(e.getMessage());//display errors
	 			}
	 	    }
 	   }

 	   @Override
 	   public void onStartTrackingTouch(SeekBar seekBar) {
 	   }

 	   @Override
 	   public void onStopTrackingTouch(SeekBar seekBar) {
 	   }
    };
    // ----------------------- SEEKBAR EVENTLISTENER - end ----------------------------
    
    
    
    // ----------------------- CONNECTION BUTTON EVENTLISTENER - begin ----------------------------
    Button.OnClickListener buttonConnectOnClickListener = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			if(!connected){//if not connected yet
				outputText("connecting to Server");
				 try {//try to create a socket and outputstream
					  socket = new Socket("192.168.1.11", 2000);//create a socket
					  dataOutputStream = new DataOutputStream(socket.getOutputStream());//and stream
					  outputText("successfully connected");//output the connection status
					  changeConnectionStatus(true);//change the connection status
				 } catch (UnknownHostException e) {//catch and
					  outputText(e.getMessage());//display errors
					  changeConnectionStatus(false);
				 } catch (IOException e) {//catch and
					 outputText(e.getMessage());//display errors
					 changeConnectionStatus(false);
				 } catch (NullPointerException e) {//catch and
					 outputText("Server Not Reachable");//display errors
					 changeConnectionStatus(false);
				 }
			}else{
				outputText("disconnecting from Server...");
				try {//try to close the socket
					  socket.close();
					  outputText("successfully disconnected");
					  changeConnectionStatus(false);//change the connection status
				 } catch (UnknownHostException e) {//catch and
					  outputText(e.getMessage());//display errors
				 } catch (IOException e) {//catch and
					  outputText(e.getMessage());//display errors
				 }
			}
	}};
	// ----------------------- CONNECTION BUTTON EVENTLISTENER - END ----------------------------
	
	
	
	// Method changes the connection status
	public void changeConnectionStatus(Boolean isConnected) {
		connected=isConnected;//change variable
		//seekBar.setEnabled(isConnected);//enable/disable seekbar
		if(isConnected){//if connection established
			buttonConnect.setText("disconnect");//change Buttontext
		}else{
			buttonConnect.setText("connect");//change Buttontext
		}
	}
	
	//Method appends text to the textfield and adds a newline character
	public void outputText(String msg) {
		textlog.append(msg+"\n");
	}
	
	
	
}