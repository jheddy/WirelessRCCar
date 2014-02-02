/*
 * Simple Android -> Arduino TCP Client
 * 
 * 20.04.2012
 * by Laurid Meyer
 * 
 * http://www.lauridmeyer.com
 * 
 */
package com.lauridmeyer.tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SimpleAndroidTCPClientActivity extends Activity {
	TextView textlog;//Log for outputs
	
	Button buttonConnect;//(dis)connect Button
	SeekBar seekBar;//Seekbar to control the Servo
	TextView seekBarValue;//Textfield displaing the Value of the seekbar
	
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
	    
	    textlog.setText("Starting Client");//log that the App launched
	    changeConnectionStatus(false);//change connectionstatus to "disconnected"
	    
	    //Eventlisteners
	    buttonConnect.setOnClickListener(buttonConnectOnClickListener);
	    seekBar.setOnSeekBarChangeListener(seekbarchangedListener);
    }
    
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
	 	    		dataOutputStream.writeBytes("prog-"+progress+"p");
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
					  socket = new Socket("192.168.43.9", 2000);//create a socket
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