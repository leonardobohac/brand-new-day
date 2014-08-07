package com.example.brandnewday;
import java.text.Format;
import java.text.SimpleDateFormat;
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
import android.os.Handler;
import android.util.Log;


public class MyApplication extends Application{
	
	public static ArrayList <PendingIntent> intentArray = new ArrayList <PendingIntent>();
	private ArrayList<String> audioPaths = new ArrayList<String>();
	private ArrayList<Uri> audioUris = new ArrayList<Uri>();
	private String audioArrayInString = new String();
	PendingIntent pendingIntent;

	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	
	static final String sun = "1";
	static final String mon = "2";
	static final String tue = "3";
	static final String wed = "4";
	static final String thu = "5";
	static final String fri = "6";
	static final String sat = "7";
	
	
	public ArrayList<String> current_alarm_days;
	
	
	
	public void activateAlarm(final int index, ArrayList<ArrayList<String>> alarm_days, final int[] alarmHours, final int[] alarmMinutes, final int[] alarmSnoozes) {
		deactivateAlarm(index);
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
		current_alarm_days = alarm_days.get(index);
		
		if(current_alarm_days.size() == 0){
			Log.d("days size", "0");
			// default alarm is for tomorrow and all the following days
			int default_alarm_id = 0;
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

		    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), default_alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    //alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pendingIntent);
		    Log.d("pi", pendingIntent.toString());
		}
		
		else{
			for(int i=0; i<current_alarm_days.size(); i++){
				int alarm_day = Integer.parseInt(current_alarm_days.get(i));
				Calendar alarmCalendar = Calendar.getInstance();
				Calendar now = Calendar.getInstance();
				int today = now.get(Calendar.DAY_OF_WEEK);
				
				if(alarm_day < today){
					alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
					alarmCalendar.set(Calendar.DAY_OF_WEEK, alarm_day);
					alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHours[index]);
					alarmCalendar.set(Calendar.MINUTE, alarmMinutes[index]);
					alarmCalendar.set(Calendar.SECOND,0);
				}
				
				else if (alarm_day == today){
					now.set(Calendar.SECOND,0);
					long now_time = now.getTimeInMillis();
					
					alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHours[index]);
					alarmCalendar.set(Calendar.MINUTE, alarmMinutes[index]);
					alarmCalendar.set(Calendar.SECOND,0);
					long alarm_time = alarmCalendar.getTimeInMillis();
					
					if(alarm_time <= now_time){
						alarmCalendar.add(Calendar.WEEK_OF_YEAR, 1);
						alarmCalendar.set(Calendar.DAY_OF_WEEK, alarm_day);
					}
					else {
						alarmCalendar.set(Calendar.DAY_OF_WEEK, alarm_day);
					}
				}
					
				else {
					alarmCalendar.set(Calendar.DAY_OF_WEEK, alarm_day);
					alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHours[index]);
					alarmCalendar.set(Calendar.MINUTE, alarmMinutes[index]);
					alarmCalendar.set(Calendar.SECOND,0);
				}
					
				int alarm_id = 10*index + alarm_day;  // identifies the alarm based on it index and day selected
				
				Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
				intent.putExtra("index", index);
				intent.putExtra("snooze", alarmSnoozes[index]);
	
			    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm_id, intent, 0);
			    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
			    
			    Log.d("day", Integer.toString(alarm_day));
			    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Log.d("calendar", formatter.format(alarmCalendar.getTime()));
			    
			}
		}
	}
	
	public void deactivateAlarm(int index){
		Intent intent1 = new Intent(getApplicationContext(), AlarmReceiver.class);
		int default_alarm_id = 0;
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), default_alarm_id, intent1, PendingIntent.FLAG_UPDATE_CURRENT); 
		AlarmManager alarmManager1 = (AlarmManager)getSystemService(ALARM_SERVICE);
    	alarmManager1.cancel(pendingIntent1);
    	
		for(int i=1; i<=7; i++){
			Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
			int alarm_id = 10*index + i;
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    	alarmManager.cancel(pendingIntent);
		}
	}
	

	/*public void activateAlarm(int index, int[] alarmHours, int[] alarmMinutes, int[] alarmSnoozes) {
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
*/
	
	
	/*public int[] getAlarmHours() {
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
	}*/

	
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
	
	public String serializeStrings(ArrayList<String> pathsArray) throws Exception {
		ArrayList<String> stringArray = new ArrayList<String>();
		for(int i=0; i < pathsArray.size(); i+=1) {
			stringArray.add((pathsArray.get(i)).toString());
		}
		String string = ObjectSerializer.serialize(stringArray);
		return string;
		
	}
	
	public ArrayList<String> deserializeStrings(String string) throws Exception {
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
	
	
	
	public void activateSnooze(int snooze) {
		int snooze_id = 100; // no reason why
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTimeInMillis(System.currentTimeMillis());
		rightNow.add(Calendar.MINUTE, snooze);

		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		intent.putExtra("snooze", snooze);
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), snooze_id, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
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
