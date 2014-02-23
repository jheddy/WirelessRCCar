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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SimpleAndroidTCPClientActivity extends Activity {
	TextView textlog;//Log for outputs
	
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar..
	EditText ip; //server ip to connect to
	JoystickView joystick; //joystick to send pwm values
	
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
	    seekBar = (SeekBar)findViewById(R.id.seekbar);
	    seekBarValue = (TextView)findViewById(R.id.seekbarvalue);
	    ip = (EditText)findViewById(R.id.ipaddr);
	    
	    
	    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.llout);
	    joystick = new JoystickView(this);
	    mainLayout.addView(joystick);
	    
	    
	    textlog.setText("Starting Client");//log that the App launched
	    changeConnectionStatus(false);//change connectionstatus to "disconnected"
	    
	    //Eventlisteners
	    buttonConnect.setOnClickListener(buttonConnectOnClickListener);
	    seekBar.setOnSeekBarChangeListener(seekbarchangedListener);
	    joystick.setOnJostickMovedListener(joystickmovedlistener);
    }
    
    
    // ----------------------- Joystick EVENTLISTENER - begin ----------------------------
    JoystickMovedListener joystickmovedlistener = new JoystickMovedListener(){

		@Override
		public void OnMoved(int pan, int tilt) {
			Log.d("JOYSTICK", "Pan: "+ pan +" Tilt: "+tilt);
			
		}

		@Override
		public void OnReleased() {
			Log.d("JOYSTICK", "Joystick Released");
			
		}

		@Override
		public void OnReturnedToCenter() {
			Log.d("JOYSTICK", "Joystick Back to center");
			
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
					  socket = new Socket(ip.getText().toString(), 2000);//create a socket
					  dataOutputStream = new DataOutputStream(socket.getOutputStream());//and stream
					  outputText("successfully connected");//output the connection status
					  changeConnectionStatus(true);//change the connection status
				 } catch (UnknownHostException e) {//catch and
					  outputText(e.getMessage());//display errors
					  changeConnectionStatus(false);
				 } catch (IOException e) {//catch and
					 outputText(e.getMessage());//display errors
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
		seekBar.setEnabled(isConnected);//enable/disable seekbar
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