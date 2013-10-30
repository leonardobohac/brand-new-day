package com.example.brandnewday;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.preference.PreferenceManager;


public class MyApplication extends Application{
	private Context context;
    private SharedPreferences preferences;
	int alarm1Hour, alarm2Hour, alarm3Hour;
	int alarm1Minute, alarm2Minute, alarm3Minute;
	int alarm1Snooze, alarm2Snooze, alarm3Snooze;
	boolean alarm1Activated, alarm2Activated, alarm3Activated;
	private ArrayList<Uri> audioUris = new ArrayList<Uri>();
	String stringAudioUris;
	private ArrayList<String> audioPaths= new ArrayList<String>();
	String stringAudioPaths;
	private PendingIntent pendingIntent;

	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	
	private int[] alarmHours = {7, 8, 9};
	private int[] alarmMinutes = {15, 30 ,45};
	private int[] alarmSnoozes = {0, 0, 0};
	private boolean[] alarmActivated = {false, false, false, false};
	
	public MyApplication(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        getHoursPreferences();
        getMinutesPreferences();
        getSnoozesPreferences();
        getActivatedPreferences();
    }
	
	public MyApplication() {
		super();
	}
	
	

	
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

	public ArrayList<Uri> getAudioUris() {
		return audioUris;
	}
	
	/*public ArrayList<Uri> getAudioUris() {
		return stringToUriArray(stringAudioUris);
	}*/
	


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
			
			/*Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
			intent.putExtra("hour", alarmHours[index]);
			intent.putExtra("minute", alarmMinutes[index]);
			intent.putExtra("snoozeTime", alarmSnoozes[index]);*/
			
			
			//this will cancel the old alarm request and set the new one
			//PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), index, intent, 0); 
			
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
		    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent,0);
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
	
	public void activateSnooze(int index, int[] alarmSnoozes) {
		
		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("snooze", alarmSnoozes[index]);
		
		//this will cancel the old alarm request and set the new one
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), index, intent, 0); 
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar rightNow = Calendar.getInstance();
		
		rightNow.setTimeInMillis(System.currentTimeMillis());
		rightNow.add(Calendar.MINUTE, alarmSnoozes[index]);

		alarmManager.set(AlarmManager.RTC_WAKEUP, rightNow.getTimeInMillis(), pendingIntent);
	}
	

	public void getHoursPreferences() {
		preferences = context.getSharedPreferences("pref",MODE_PRIVATE);
		alarmHours[ALARM_1_INDEX] = preferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = preferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = preferences.getInt("alarm3Hour", 9);
	}
	
	public void getMinutesPreferences() {
		alarmMinutes[ALARM_1_INDEX] = preferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = preferences.getInt("alarm2Minute", 30);
		alarmMinutes[ALARM_3_INDEX] = preferences.getInt("alarm3Minute", 30);
	}
	
	public void getSnoozesPreferences() {
		alarmSnoozes[ALARM_1_INDEX] = preferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = preferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = preferences.getInt("alarm3Snooze", 0);
	}
	
	public void getActivatedPreferences() {
		alarmActivated[ALARM_1_INDEX] = preferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = preferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = preferences.getBoolean("alarm3Activated", false);
	}
	
	
}
