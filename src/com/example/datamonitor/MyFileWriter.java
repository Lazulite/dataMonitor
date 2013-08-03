package com.example.datamonitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.example.datamonitor.*;


public class MyFileWriter{
	
	
	 public File createFile(String sensortype){
			try {
				File card = Environment.getExternalStorageDirectory();
				File directory = new File(card.getAbsolutePath()
						+ "/myData/test/"+sensortype);
				directory.mkdirs();

				Date time = new Date();
				String fileName = getSimpleTime(time) + sensortype +"_.csv";

				File sampleFile = new File(directory, fileName);
				sampleFile.createNewFile();
				
				writeFile(sampleFile, "Timestamp,X-axis,Y-axis,Z-axis,yymmddhhmmss,ActionType");
				return sampleFile;
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("WRITEACCELEROMETER", "File write failed");
				}
			return null;
		}
	    

		public String getSimpleTime(Date t) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String currentDateandTime = sdf.format(t);
			return currentDateandTime;
		}
	    
	    public void writeFile(File logFile,String text){
	    	try{
	    		BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
			    buf.append(text);
			    buf.newLine();
			    buf.close();
	    	//fileWriter.write(String.valueOf(sensorValue[3])+","+String.valueOf(sensorValue[0])+","+String.valueOf(sensorValue[1])+","+String.valueOf(sensorValue[2])+","+String.valueOf(sensorValue[3])+","+"\n");
	    	}catch (IOException e) {
				e.printStackTrace();
			}
	    }

}
