package com.example.brandnewday;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.pig.impl.util.ObjectSerializer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;


public class MyApplication extends Application{
	private int[] alarmHours = new int[3];
	private int[] alarmMinutes = new int[3];
	private int[] alarmSnoozes = new int[3];
	private boolean[] alarmActivated = new boolean[4];
	private ArrayList<String> audioPaths = new ArrayList<String>();
	private ArrayList<Uri> audioUris = new ArrayList<Uri>();
	private String audioArrayInString = new String();
	PendingIntent pendingIntent;

	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	
	
	public int[] getAlarmHours() {
		return alarmHours;
	}

	public int[] getAlarmMinutes() {
		return alarmMinutes;
	}

	public int[] getAlarmSnoozes() {
		return alarmSnoozes;
	}

	public boolean[] getAlarmActivated() {
		return alarmActivated;
	}

	
	public String getAudioArrayInString() {
		return audioArrayInString;
	}
	
	public ArrayList<Uri> getAudioUris() {
		return audioUris;
	}
	
	public ArrayList<String> getAudioPaths() {
		return audioPaths;
	}
	
	public ArrayList<Uri> deserialize(String string) throws Exception {
		@SuppressWarnings("unchecked")
		ArrayList<String> stringArray = (ArrayList<String>) ObjectSerializer.deserialize(string);
		ArrayList<Uri> uriArray = new ArrayList<Uri>();
		for(int i=0; i < stringArray.size(); i+=1) {
			uriArray.add(Uri.parse(stringArray.get(i)));
		}
		return uriArray;
	}
		
	public String serialize(ArrayList<Uri> uriArray) throws Exception {
		ArrayList<String> stringArray = new ArrayList<String>();
		for(int i=0; i < uriArray.size(); i+=1) {
			stringArray.add((uriArray.get(i)).toString());
		}
		String string = ObjectSerializer.serialize(stringArray);
		return string;
		
	}
	
	public String serializePaths(ArrayList<String> pathsArray) throws Exception {
		ArrayList<String> stringArray = new ArrayList<String>();
		for(int i=0; i < pathsArray.size(); i+=1) {
			stringArray.add((pathsArray.get(i)).toString());
		}
		String string = ObjectSerializer.serialize(stringArray);
		return string;
		
	}
	
	public ArrayList<String> deserializePaths(String string) throws Exception {
		@SuppressWarnings("unchecked")
		ArrayList<String> stringArray = (ArrayList<String>) ObjectSerializer.deserialize(string);
		ArrayList<String> pathsArray = new ArrayList<String>();
		for(int i=0; i < stringArray.size(); i+=1) {
			pathsArray.add((stringArray.get(i)));
		}
		return pathsArray;
	}

	public ArrayList<Uri> stringToUriArray(String uriInStringArray) {   
		uriInStringArray.replace("[", "");
		uriInStringArray.replace("]", "");
		ArrayList<Uri> uriArray = parseStringArrayToUriArray(uriInStringArray.split(","));
		
		return uriArray;	
	}
	
	public ArrayList<Uri> parseStringArrayToUriArray(String[] stringArray) {
		List<String> stringList = Arrays.asList(stringArray);
		ArrayList<Uri> uriArrayList = new ArrayList<Uri>();
		for(int i=0; i < stringList.size(); i += 1) {
			uriArrayList.set(i, Uri.parse(stringList.get(i)));
		}
		
		return uriArrayList;
	}
	
	
	
	public void activateAlarm(int index, int[] alarmHours, int[] alarmMinutes, int[] alarmSnoozes) {
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar alarm_time = Calendar.getInstance();
			Calendar rightNow = Calendar.getInstance();
			
			rightNow.setTimeInMillis(System.currentTimeMillis());
			rightNow.set(Calendar.SECOND, 0);
			
			alarm_time.setTimeInMillis(System.currentTimeMillis());
			alarm_time.set(Calendar.HOUR_OF_DAY, alarmHours[index]);
			alarm_time.set(Calendar.MINUTE, alarmMinutes[index]);
			alarm_time.set(Calendar.SECOND,0);
			
			if(rightNow.compareTo(alarm_time) == 0) {
				//equal alarms
				alarm_time.add(Calendar.DAY_OF_YEAR, 1); //set to next day
			}
			else if(rightNow.compareTo(alarm_time) == 1) {
				//rightNow is later 
				alarm_time.add(Calendar.DAY_OF_YEAR, 1); //set to next day
			}
				
			Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
			intent.putExtra("index", index);
			intent.putExtra("snooze", alarmSnoozes[index]);

		    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pendingIntent);
			
		    
		}
	
	public void deactivateAlarm(int index){
		//Toast.makeText(getApplicationContext(), "alarme desativado", 2).show();
		Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
		i.putExtra("hour", alarmHours[index]);
		i.putExtra("minute", alarmMinutes[index]);
		i.putExtra("snoozeTime", alarmSnoozes[index]);
		
		//this will cancel the old alarm
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, i, 0); 
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
    	alarmManager.cancel(pendingIntent);
	}
	
	public void activateSnooze(int index, int snooze) {		
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTimeInMillis(System.currentTimeMillis());
		rightNow.add(Calendar.MINUTE, snooze);

		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("snooze", snooze);
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
		alarmManager.set(AlarmManager.RTC_WAKEUP, rightNow.getTimeInMillis(), pendingIntent);
	}
	
	public void activateNap(int nap) {
		int index = ALARM_NAP_INDEX;
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTimeInMillis(System.currentTimeMillis());
		rightNow.add(Calendar.MINUTE, nap);

		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("snooze", 0);
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
		alarmManager.set(AlarmManager.RTC_WAKEUP, rightNow.getTimeInMillis(), pendingIntent);
	}
	
	public void deactivateNap(){
		//Toast.makeText(getApplicationContext(), "alarme desativado", 2).show();
		Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
		
		i.putExtra("snooze", 0);
		
		//this will cancel the old alarm
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ALARM_NAP_INDEX, i, 0); 
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
    	alarmManager.cancel(pendingIntent);
	}

}
