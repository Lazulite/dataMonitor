package com.example.datamonitor.services;


import java.io.File;


import com.example.datamonitor.*;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class MonitorService extends Service{
	
		private static final String DEBUG_TAG = "MonitorService";
		private SensorManager sensorManager = null;
		private SensorEventListener sensorListener = null;
		private Sensor sensor = null;
		/*	private Object[] sensorValue = {
				"X-axis", "Y-axis","Z-axis r", "Timestamp"
		};
		*/
		private File accFile,compFile,mgFile,gysFile,tempFile;
		private MyFileWriter myfilewriter = new MyFileWriter();
		
		
		@Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
			
			
	        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        
	        sensorListener = new SensorEventListener(){
	    	    
	        	@Override
	    	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    	        // do nothing
	    	    }
	    	    
	        	@Override
	    	    public void onSensorChanged(SensorEvent event) {
	    	    	//Log.e("MonitorService","OnSensorChanged");
	    	    	String value="";
	    	    	
	    	    	switch (event.sensor.getType()){
	    	    	case Sensor.TYPE_ACCELEROMETER:
	    	    		value=String.valueOf(event.timestamp)+","+String.valueOf(event.values[0])+","+String.valueOf(event.values[1])+","+String.valueOf(event.values[2]);
		    	    	myfilewriter.writeFile(accFile,value);
		    			break;
		    			
	    	    	case Sensor.TYPE_ORIENTATION:	    	 
		    	    	value=String.valueOf(event.timestamp)+","+String.valueOf(event.values[0])+","+String.valueOf(event.values[1])+","+String.valueOf(event.values[2]);
		    	    	myfilewriter.writeFile(compFile,value);
		    	    	break;
		    	    	
	    	    	case Sensor.TYPE_MAGNETIC_FIELD:	    	 
		    	    	value=String.valueOf(event.timestamp)+","+String.valueOf(event.values[0])+","+String.valueOf(event.values[1])+","+String.valueOf(event.values[2]);
		    	    	myfilewriter.writeFile(mgFile,value);
		    	    	break;
		    	    	
	    	    	case Sensor.TYPE_TEMPERATURE:	    	 
		    	    	value=String.valueOf(event.timestamp)+","+String.valueOf(event.values[0])+","+String.valueOf(event.values[1])+","+String.valueOf(event.values[2]);
		    	    	myfilewriter.writeFile(tempFile,value);
		    	    	break;
		    	    	
	    	    	case Sensor.TYPE_GYROSCOPE:	    	 
		    	    	value=String.valueOf(event.timestamp)+","+String.valueOf(event.values[0])+","+String.valueOf(event.values[1])+","+String.valueOf(event.values[2]);
		    	    	myfilewriter.writeFile(gysFile,value);
		    	    	break;
	    	    	}
	    	    	
	    	    	}
	    	 };
	    	 sensorManager.registerListener(sensorListener, sensor,SensorManager.SENSOR_DELAY_NORMAL);
	    	    
	        return START_STICKY;
	    }
		
		@Override
		public void onCreate() {
			super.onCreate();
			
			accFile = myfilewriter.createFile("_Accelerometer");		
			compFile = myfilewriter.createFile("_Compass");
			mgFile = myfilewriter.createFile("_Magnetic_Field");
			tempFile = myfilewriter.createFile("_Temprature");
			gysFile = myfilewriter.createFile("_GYROSCOPE");
			Toast.makeText(getApplicationContext(), "Files Created", Toast.LENGTH_SHORT).show();
		
		}
		
	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }
	    
	    @Override
		public void onDestroy() {
	    	sensorManager.unregisterListener(sensorListener);
	    } 
		
}
