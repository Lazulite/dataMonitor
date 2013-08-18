package com.example.datamonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ResponseCache;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.datamonitor.jsonlib.*;
import com.example.datamonitor.services.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	
	//Debug
	private static final String TAG="MainActivity";
	private static final String DEBUG="BUGBUGBUG";
	
	
	//HxM Member Field
	private String mHxMName = null;
	private String mHxMAddress = null;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothService mBluetoothService = null;
	private Boolean status=false;
	
	//BT variables
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private long timestamp=0;
    private boolean firstBeat=false;
    int counter=1;
    //Layout Views
	private TextView mStatus,response;
    //File writer
    private File hRFile;
    private MyFileWriter myFileWriter = new MyFileWriter();
    
    private WakeLock wakeLock;
    
    /*private Object[] activities = { 
    		"Compass",				Compass.class,
    		//"Orientation",			Orientation.class,
    		"Accelerometer",		Accelerometer.class,
    		//"Magnetic Field",		MagneticField.class,	
    		//"Temperature",			Temperature.class,
    };*/
    
    
    //HTTP
    private String baseURL = "http://wikihealth.bigdatapro.org/api/#!";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		EditText actionTag = (EditText) findViewById(R.id.actiontag);
		EditText targetFile = (EditText) findViewById(R.id.filename);
		mStatus = (TextView) findViewById(R.id.btstatus);
		response = (TextView) findViewById(R.id.Response);
		
		Button HRon = (Button) findViewById(R.id.b_HROn);
		Button strt = (Button) findViewById(R.id.b_start);
		Button stop = (Button) findViewById(R.id.b_stop);
		Button login = (Button) findViewById(R.id.b_login);
		Button post = (Button) findViewById(R.id.b_post);
		Button postall = (Button) findViewById(R.id.b_postall);
		
		HRon.setOnClickListener(this);
		strt.setOnClickListener(this);
		stop.setOnClickListener(this);
		login.setOnClickListener(this);
		post.setOnClickListener(this);
		postall.setOnClickListener(this);
		/*
		Intent intent=new Intent(this,MonitorService.class);
		startService(intent);*/
				
		//Heart Rate Bluetooth initialize	
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        Log.e(DEBUG, "onCreat()");
        
        //TODO auto-tag listen EditText 
		
	}
		
		@Override
		public void onClick(View v) {

			Log.e(DEBUG, "onCLickListener()");
			Intent intent = new Intent(this,MonitorService.class);
			
			
			switch(v.getId()){
			
				case R.id.b_HROn:
					if(!status) {
						mBluetoothService.reset();
						status=true;
					}
					else{
						hRFile=myFileWriter.createFile("HR");
						connectToHxM();
					}						
					status=false;
					
					Log.e(DEBUG, "connectToHxM()");
					break;
					
				case R.id.b_start:
					Log.e(DEBUG, "buttonSTART");	
		
					
					Log.e(DEBUG, "BeforeStartService");
					//Log.e(DEBUG, intent.toString());
					startService(intent);
					Log.e(DEBUG, "AfterstartService");
					//bindService();
					break;
					
				case R.id.b_stop:
					//unBind();
					stopService(intent);
					break;
				case R.id.b_login:
					try {
						
					JSONObject json=new JSONObject();

					json.put("loginid", "lei");
					json.put("password", "northern");
					json.put("expire_in_seconds", 999999999);
	
					Log.e("INPUTJSON",json.toString());
					
					String logURL="http://wikihealth.bigdatapro.org:55555/healthbook/v1/users/gettoken?api_key=special-key";
					
					wikiUpload wiki=new wikiUpload( logURL, "POST",json, Loginid.class);
					InputStream result;
					result = wiki.execute().get();
					/*BufferedReader reader = new BufferedReader(new InputStreamReader(result));
			        String line;
			        StringBuilder builder = new StringBuilder();
			        while ((line = reader.readLine()) != null) {
			          builder.append(line);
			        }
			        
					Log.e("BUILDER",builder.toString());
					response.setText(builder.toString());*/
					
					Gson gson=new Gson();
					Reader reader = new InputStreamReader(result);
					JsonParser parser = new JsonParser();
				    JsonObject obj = parser.parse(reader).getAsJsonObject();			
				    Log.e("RESPONSE",obj.toString());
					//LoginResponse loginResponse=gson.fromJson(obj.get("result"), LoginResponse.class);
					//Log.e("RESPONSE",LoginResponse.gettoken());
					//response.setText(loginResponse.gettoken());
			       break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(JSONException e){
					e.printStackTrace();
				}
					
				case R.id.b_post:
					//post();
					break;
				case R.id.b_postall:
					//postall();
					break;
			}
			
		}

	
	protected void onStart() {
        super.onStart();
        Log.e(DEBUG, "onStart()");
        
        // If BT is not on, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
          	Toast.makeText(getApplicationContext(), "Please enable Bluetooth...", Toast.LENGTH_LONG).show();
        	Log.d(TAG, "onStart: Blueooth adapter detected, but it's not enabled");
        // Otherwise, setup the chat session
        } else {
            if (mBluetoothService == null)  status=true;
        }
        
        PowerManager mgr = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
       
    }
	

	@Override
    protected synchronized void onResume() {
        super.onResume();
        Log.e(DEBUG, "onResume()");
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
        	//###
            if (mBluetoothService.getState() == R.string.BT_SERVICE_STATE_NONE) {
              // Start the Bluetooth chat services
              mBluetoothService.start();
            }
        }
        
        
    }
	
	
	@Override
    protected synchronized void onPause() {
        super.onPause();
        Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "-- ON STOP --");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        Intent intent = new Intent(this,MonitorService.class);
        stopService(intent);    
        wakeLock.release();
        if (mBluetoothService != null) mBluetoothService.stop();
        Log.e(TAG, "--- ON DESTROY ---");
    }
 
	    
	public void connectToHxM(){
		Log.e(DEBUG, "connectToHxM()");
		if (mBluetoothService == null) 
	    	setupHrm();
	    if ( getFirstConnectedHxm() ) {
	    	BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mHxMAddress);
	    	mBluetoothService.connect(device); 	// Attempt to connect to the device
	    }
	}
    
    
	public void setupHrm() {
		Log.e(DEBUG, "setupHrm()");
		 mBluetoothService = new BluetoothService(this, mHandler);
	}
	
	private boolean getFirstConnectedHxm() {
		Log.e(DEBUG, "getFirstConnectedHxm()");
		mHxMAddress = null;    	
    	mHxMName = null;
  	
	    BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	    Set<BluetoothDevice> bondedDevices = mBtAdapter.getBondedDevices();

	    if (bondedDevices.size() > 0) {
	        for (BluetoothDevice device : bondedDevices) {
	        	String deviceName = device.getName();
	        	if ( deviceName.startsWith("HXM") ) {
	        		mHxMAddress = device.getAddress();
	        		mHxMName = device.getName();
	        		Log.d(TAG,"getFirstConnectedHxm() found a device whose name starts with 'HXM', its name is "+mHxMName+" and its address is ++mHxMAddress");
	        		break;
	        	}
	        }
	    }    
	    return (mHxMAddress != null);
	}
	
	
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case R.string.BT_SERVICE_MSG_STATE: 
                Log.d(TAG, "handleMessage():  MESSAGE_STATE_CHANGE: " + msg.arg1);
                Log.e(DEBUG,"Handler");
                switch (msg.arg1) {
	                case R.string.BT_SERVICE_CONNECTED:
	                	if (mHxMName != null) {
	                		mStatus.setText("Connected to ");
	                		mStatus.append(mHxMName);
	                	}
	                	Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
	                    break;

	                case R.string.BT_SERVICE_CONNECTING:
	                    mStatus.setText("Connecting");
	                	Toast.makeText(getApplicationContext(), "Connecting..", Toast.LENGTH_SHORT).show();
	                    break;
	                    
	                case R.string.BT_SERVICE_STATE_NONE:
	                	mStatus.setText("Disconnected");
	                	Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
	                    break;
                }
                break;

            case R.string.BT_SERVICE_MSG_READ: {
            	/*
            	 * MESSAGE_READ will have the byte buffer in tow, we take it, build an instance
            	 * of a HrmReading object from the bytes, and then display it into our view
            	 */
                byte[] readBuf = (byte[]) msg.obj;
//                Date date= new Date();
//				String timestamp=String.valueOf(date.getTime());
              
                HrmReading hrm = new HrmReading( readBuf );
                hrm.displayRaw();
                long hrv=hrm.hbTime1-hrm.hbTime2;
                if(Math.abs(hrv)>60000){
                	hrv=hrm.hbTime1+65535-hrm.hbTime2;
                }
              
                myFileWriter.writeFile(hRFile, String.valueOf(hrm.hrmtimestamp)+","+String.valueOf(hrm.heartRate)+","+String.valueOf(hrv)+","+String.valueOf(timestamp));
                if(firstBeat){
              	  timestamp=timestamp+hrv;
                }
                break;
            }
                
            case R.string.BT_SERVICE_MSG_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(null),Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        
	        case R.id.quit:
	        	finish();	
	        	return true;
	        case R.id.about: {
	            showAbout();
	            break;
	        }
        }
		return false;
	}
	
	
	private void showAbout() {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
				.setIcon(R.drawable.appicon)
				.setTitle(R.string.app_name)
				.setMessage(
						"Author: LWang \n" + "Email: luckytobegin@gmail.com").create();
				/*.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create();*/
		alertDialog.show();
    }
	 
	 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 public class HrmReading {
        public final int STX = 0x02;
        public final int MSGID = 0x26;
        public final int DLC = 55;
        public final int ETX = 0x03;
    	
    	private static final String TAG = "HrmReading";

    	int serial;
        byte stx;
        byte msgId;
        byte dlc;
        int firmwareId;
        int firmwareVersion;
        int hardWareId;
        int hardwareVersion;
        int batteryIndicator;
        int heartRate;
        int heartBeatNumber;
        long hbTime1;
        long hbTime2;
        long hbTime3;
        long hbTime4;
        long hbTime5;
        long hbTime6;
        long hbTime7;
        long hbTime8;
        long hbTime9;
        long hbTime10;
        long hbTime11;
        long hbTime12;
        long hbTime13;
        long hbTime14;
        long hbTime15;
      /*  long reserved1;
        long reserved2;
        long reserved3;
        long distance;
        long speed;
        byte strides;    
        byte reserved4;
        long reserved5;
        byte crc;
        byte etx;*/
        long hrmtimestamp;
        

        public HrmReading (byte[] buffer) {
        	int bufferIndex = 0;

        	Log.d ( TAG, "HrmReading being built from byte buffer");
        	
            try {
    			stx 				= buffer[bufferIndex++];
    			msgId 				= buffer[bufferIndex++];
    			dlc 				= buffer[bufferIndex++];
    			firmwareId 			= (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			firmwareVersion 	= (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hardWareId 			= (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hardwareVersion		= (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			batteryIndicator  	= (int)(0x000000FF & (int)(buffer[bufferIndex++]));
    			heartRate  			= (int)(0x000000FF & (int)(buffer[bufferIndex++]));
    			heartBeatNumber  	= (int)(0x000000FF & (int)(buffer[bufferIndex++]));
    			hbTime1				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime2				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime3				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime4				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime5				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime6				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime7				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime8				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime9				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime10			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime11			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime12			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime13			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime14			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			hbTime15			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			/*reserved1			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			reserved2			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			reserved3			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			distance			= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			speed				= (long) (int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			strides 			= buffer[bufferIndex++];    
    			reserved4 			= buffer[bufferIndex++];
    			reserved5 			= (long)(int)((0x000000FF & (int)buffer[bufferIndex++]) | (int)(0x000000FF & (int)buffer[bufferIndex++])<< 8);
    			crc 				= buffer[bufferIndex++];
    			etx 				= buffer[bufferIndex];*/
    			littledump();
    			if(!firstBeat && hbTime1!=0){
    				firstBeat=true;
    			}
    			  Date date= new Date();
    			  hrmtimestamp=date.getTime();
    			
    		} catch (Exception e) {
    	        Log.d(TAG, "Failure building HrmReading from byte buffer, probably an incopmplete or corrupted buffer");
    		}
                
    		
            Log.d(TAG, "Building HrmReading from byte buffer complete, consumed " + bufferIndex + " bytes in the process");
            //dump();
            }
        private void littledump(){
        	Log.d("data+hr", String.valueOf(heartRate));
			Log.d("data+hrb", String.valueOf(heartBeatNumber));
			
			System.out.print(" HRB : "+String.valueOf(heartBeatNumber));
			System.out.print(" HRV[1] : "+ String.valueOf(hbTime1));
			System.out.print(" HRV[2] : "+ String.valueOf(hbTime2));
			System.out.print(" HRV[3] : "+ String.valueOf(hbTime3));
			System.out.print(" HRV[4] : "+ String.valueOf(hbTime4));
			System.out.print(" HRV[5] : "+ String.valueOf(hbTime5));
			System.out.print(" HRV[6] : "+ String.valueOf(hbTime6));
			System.out.print(" HRV[7] : "+ String.valueOf(hbTime7));
			System.out.print(" HRV[8] : "+ String.valueOf(hbTime8));
			System.out.print(" HRV[9] : "+ String.valueOf(hbTime9));
			System.out.print(" HRV[10] : "+ String.valueOf(hbTime10));
			System.out.print(" HRV[11] : "+ String.valueOf(hbTime11));
			System.out.print(" HRV[12] : "+ String.valueOf(hbTime12));
			
			System.out.print("HRV[13] : "+ String.valueOf(hbTime13));
			System.out.print("HRV[14] : "+ String.valueOf(hbTime14));
			System.out.println("HRV[15] : "+ String.valueOf(hbTime15));
			
			
			/*Log.d("data+hr[1]", String.valueOf(hbTime1));
			Log.d("data+hr[1]", String.valueOf(hbTime2));
			Log.d("data+hr[1]", String.valueOf(hbTime3));
			Log.d("data+hr[1]", String.valueOf(hbTime4));
			Log.d("data+hr[1]", String.valueOf(hbTime5));
			Log.d("data+hr[1]", String.valueOf(hbTime6));
			Log.d("data+hr[1]", String.valueOf(hbTime7));
			Log.d("data+hr", String.valueOf(hbTime8));
			Log.d("data+hr[1]", String.valueOf(hbTime9));
			Log.d("data+hr", String.valueOf(hbTime10));
			Log.d("data+hr", String.valueOf(hbTime11));
			Log.d("data+hr", String.valueOf(hbTime12));
			Log.d("data+hr", String.valueOf(hbTime13));
			Log.d("data+hr", String.valueOf(hbTime14));
			Log.d("data+hr", String.valueOf(hbTime15));*/

        }

        
        /*
         * Display the HRM reading into the layout     
         */
            private void displayRaw() {  	  
       /*     	display	( R.id.stx,  stx );
            	display ( R.id.msgId,  msgId );
            	display ( R.id.dlc,  dlc );
            	display ( R.id.firmwareId,   firmwareId );
            	display ( R.id.firmwareVersion,   firmwareVersion );
            	display ( R.id.hardwareId,   hardWareId );
            	display ( R.id.hardwareVersion,   hardwareVersion );*/
            	//display ( R.id.batteryChargeIndicator,  (int)batteryIndicator );
            	display ( R.id.HR_data, (int)heartRate );
            	//display ( R.id.heartBeatNumber,  (int)heartBeatNumber );
            	/*display ( R.id.hbTimestamp1,   hbTime1 );
            	display ( R.id.hbTimestamp2,   hbTime2 );
            	display ( R.id.hbTimestamp3,   hbTime3 );
            	display ( R.id.hbTimestamp4,   hbTime4 );
            	display ( R.id.hbTimestamp5,   hbTime5 );
            	display ( R.id.hbTimestamp6,   hbTime6 );
            	display ( R.id.hbTimestamp7,   hbTime7 );
            	display ( R.id.hbTimestamp8,   hbTime8 );
            	display ( R.id.hbTimestamp9,   hbTime9 );
            	display ( R.id.hbTimestamp10,   hbTime10 );
            	display ( R.id.hbTimestamp11,   hbTime11 );
            	display ( R.id.hbTimestamp12,   hbTime12 );
            	display ( R.id.hbTimestamp13,   hbTime13 );
            	display ( R.id.hbTimestamp14,   hbTime14 );
            	display ( R.id.hbTimestamp15,   hbTime15 );*/
            /*	display ( R.id.reserved1,   reserved1 );
            	display ( R.id.reserved2,   reserved2 );
            	display ( R.id.reserved3,   reserved3 );
            	display ( R.id.distance,   distance );
            	display ( R.id.speed,   speed );
            	display ( R.id.strides,  (int)strides );
            	display ( R.id.reserved4,  reserved4 );
            	display ( R.id.reserved5,  reserved5 );
            	display ( R.id.crc,  crc );
            	display ( R.id.etx,  etx );    	    	    	
            	*/
            }    

        
        
        /*
         * dump() sends the contents of the HrmReading object to the log, use 'logcat' to view
         */    
        public void dump() {
        		Log.d(TAG,"HrmReading Dump");
        		Log.d(TAG,"...serial "+ ( serial ));
        		Log.d(TAG,"...stx "+ ( stx ));
        		Log.d(TAG,"...msgId "+( msgId ));
        		Log.d(TAG,"...dlc "+ ( dlc ));
        		Log.d(TAG,"...firmwareId "+ ( firmwareId ));
        		Log.d(TAG,"...sfirmwareVersiontx "+ (  firmwareVersion ));
        		Log.d(TAG,"...hardWareId "+ (  hardWareId ));
        		Log.d(TAG,"...hardwareVersion "+ (  hardwareVersion ));
        		Log.d(TAG,"...batteryIndicator "+ ( batteryIndicator ));
        		Log.d(TAG,"...heartRate "+ ( heartRate ));
        		Log.d(TAG,"...heartBeatNumber "+ ( heartBeatNumber ));
        		Log.d(TAG,"...shbTime1tx "+ (  hbTime1 ));
        		Log.d(TAG,"...hbTime2 "+ (  hbTime2 ));
        		Log.d(TAG,"...hbTime3 "+ (  hbTime3 ));
        		Log.d(TAG,"...hbTime4 "+ (  hbTime4 ));
        		Log.d(TAG,"...hbTime4 "+ (  hbTime5 ));
        		Log.d(TAG,"...hbTime6 "+ (  hbTime6 ));
        		Log.d(TAG,"...hbTime7 "+ (  hbTime7 ));
        		Log.d(TAG,"...hbTime8 "+ (  hbTime8 ));
        		Log.d(TAG,"...hbTime9 "+ (  hbTime9 ));
        		Log.d(TAG,"...hbTime10 "+ (  hbTime10 ));
        		Log.d(TAG,"...hbTime11 "+ (  hbTime11 ));
        		Log.d(TAG,"...hbTime12 "+ (  hbTime12 ));
        		Log.d(TAG,"...hbTime13 "+ (  hbTime13 ));
        		Log.d(TAG,"...hbTime14 "+ (  hbTime14 ));
        		Log.d(TAG,"...hbTime15 "+ (  hbTime15 ));
/*        		Log.d(TAG,"...reserved1 "+ (  reserved1 ));
        		Log.d(TAG,"...reserved2 "+ (  reserved2 ));
        		Log.d(TAG,"...reserved3 "+ (  reserved3 ));
        		Log.d(TAG,"...distance "+ (  distance ));
        		Log.d(TAG,"...speed "+ (  speed ));
        		Log.d(TAG,"...strides "+ ( strides ));
        		Log.d(TAG,"...reserved4 "+ ( reserved4 ));
        		Log.d(TAG,"...reserved5 "+ ( reserved5 ));
        		Log.d(TAG,"...crc "+ ( crc ));
        		Log.d(TAG,"...etx "+ ( etx )); */   	    	    	
        }    

        
        
/****************************************************************************
 * Some utility functions to control the formatting of HxM fields into the 
 * activity's view
 ****************************************************************************/
        
        
        /*
         * display a byte value
         */
    	private void display  ( int nField, byte d ) {   
    		String INT_FORMAT = "%x";
    		
    		String s = String.format(INT_FORMAT, d);

    		display( nField, s  );
    	}

    	/*
    	 * display an integer value
    	 */
    	private void display  ( int nField, int d ) {   
    		String INT_FORMAT = "%d";
    		
    		String s = String.format(INT_FORMAT, d);

    		display( nField, s  );
    	}

    	/*
    	 * display a long integer value
    	 */
    	private void display  ( int nField, long d ) {   
    		String INT_FORMAT = "%d";
    		
    		String s = String.format(INT_FORMAT, d);

    		display( nField, s  );
    	}

    	/*
    	 * display a character string
    	 */
    	private void display ( int nField, CharSequence  str  ) {
        	TextView tvw = (TextView) findViewById(nField);
        	if ( tvw != null )
        		tvw.setText(str);
        }
    }    	   
	

}
