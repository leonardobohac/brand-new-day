package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BrandNewDay extends Activity {
	
	private int Alarm1DefaultHour = 7;
	private int Alarm1DefaultMinute = 30;
	private int Alarm1DefaultSnooze = 0;
	private boolean Alarm1DefaultActive = false;
	private int Alarm2DefaultHour = 8;
	private int Alarm2DefaultMinute = 30;
	private int Alarm2DefaultSnooze = 0;
	private boolean Alarm2DefaultActive = false;
	private int Alarm3DefaultHour = 9;
	private int Alarm3DefaultMinute = 30;
	private int Alarm3DefaultSnooze = 0;
	private boolean Alarm3DefaultActive = false;
	
	private PendingIntent pendingIntent;
	static final int SET_ALARM_REQUEST = 1;
	NumberFormat formatter = new DecimalFormat("00");
	
	static final String STATE_ALARM1_HOUR = "alarm1_hour";
	static final String STATE_ALARM2_HOUR = "alarm2_hourl";
	static final String STATE_ALARM3_HOUR = "alarm3_hour";
	static final String STATE_ALARM1_MINUTE = "alarm1_minute";
	static final String STATE_ALARM2_MINUTE = "alarm2_minute";
	static final String STATE_ALARM3_MINUTE = "alarm3_minute";
	static final String STATE_ALARM1_SNOOZE = "alarm1_snooze";
	static final String STATE_ALARM2_SNOOZE = "alarm2_snooze";
	static final String STATE_ALARM3_SNOOZE = "alarm3_snooze";
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	
	Alarm alarm1;
	Alarm alarm2;
	Alarm alarm3;
	Alarm[] alarms_list;
	ToggleButton alarm1ToggleButton;
	ToggleButton alarm2ToggleButton;
	ToggleButton alarm3ToggleButton;
	Button alarm1SettingsButton;
	Button alarm2SettingsButton;
	Button alarm3SettingsButton;
	Button playListButton;
	Boolean isActive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brand_new_day);
		
		alarm1 = new Alarm();
		alarm1.setIndex(ALARM_1_INDEX);
		alarm1.setIsAcive(Alarm1DefaultActive);
		alarm1.setHour(Alarm1DefaultHour);
		alarm1.setMinute(Alarm1DefaultMinute);
		alarm1.setSnooze(Alarm1DefaultSnooze);
		//setButtonText(ALARM_1_INDEX, Alarm1DefaultHour, Alarm1DefaultMinute);
	
		alarm2 = new Alarm();
		alarm2.setIndex(ALARM_2_INDEX);
		alarm2.setIsAcive(Alarm2DefaultActive);
		alarm2.setHour(Alarm2DefaultHour);
		alarm2.setMinute(Alarm2DefaultMinute);
		alarm2.setSnooze(Alarm2DefaultSnooze);
		//setButtonText(ALARM_2_INDEX, Alarm2DefaultHour, Alarm2DefaultMinute);
		
		alarm3 = new Alarm();
		alarm3.setIndex(ALARM_3_INDEX);
		alarm3.setIsAcive(Alarm3DefaultActive);
		alarm3.setHour(Alarm3DefaultHour);
		alarm3.setMinute(Alarm3DefaultMinute);
		alarm3.setSnooze(Alarm3DefaultSnooze);
		//setButtonText(ALARM_3_INDEX, Alarm3DefaultHour, Alarm3DefaultMinute);
		
		alarms_list = new Alarm[]{alarm1, alarm2, alarm3};
	   
	    alarm1ToggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		alarm2ToggleButton = (ToggleButton)findViewById(R.id.toggleButton2);
		alarm3ToggleButton = (ToggleButton)findViewById(R.id.toggleButton3);
		
	    alarm1SettingsButton = (Button)findViewById(R.id.alarm1_settings_button);
	    alarm2SettingsButton = (Button)findViewById(R.id.alarm2_settings_button);
	    alarm3SettingsButton = (Button)findViewById(R.id.alarm3_settings_button);
	    playListButton = (Button)findViewById(R.id.playlistButton);
	    
	    alarm1SettingsButton.setOnClickListener(settings1Listener);
	    alarm2SettingsButton.setOnClickListener(settings2Listener);
	    alarm3SettingsButton.setOnClickListener(settings3Listener);
	    playListButton.setOnClickListener(playMusicListener);
	    alarm1ToggleButton.setOnClickListener(alarm1ToggleButtonListener);
	    alarm2ToggleButton.setOnClickListener(alarm2ToggleButtonListener);
	    alarm3ToggleButton.setOnClickListener(alarm3ToggleButtonListener);
	}
	

	
	View.OnClickListener playMusicListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Intent i = new Intent(getApplicationContext(), WakingTime.class);
            startActivity(i);
	    }
	};
	
	View.OnClickListener settings1Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(alarm1ToggleButton.isChecked())
	    	{
	    		isActive = ALARM_ALREADY_ACTIVE;
	    	}
	    	else {
	    		isActive = ALARM_NOT_ACTIVE;
	    	}
	    	
	    	Intent i = new Intent(getApplicationContext(), AlarmSettings.class);
	    	i.putExtra("index", ALARM_1_INDEX);
	    	i.putExtra("isActive", isActive);
	    	i.putExtra("hour", alarm1.hour);
	    	i.putExtra("minute", alarm1.minute);
	    	i.putExtra("snoozeTime", alarm1.snooze);
            startActivityForResult(i, SET_ALARM_REQUEST);
	    }
	};
	
	View.OnClickListener settings2Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(alarm2ToggleButton.isChecked())
	    	{
	    		isActive = ALARM_ALREADY_ACTIVE;
	    	}
	    	else {
	    		isActive = ALARM_NOT_ACTIVE;
	    	}
	    	
	    	Intent i = new Intent(getApplicationContext(), AlarmSettings.class);
	    	i.putExtra("index", ALARM_2_INDEX);
	    	i.putExtra("isActive", isActive);
	    	i.putExtra("hour", alarm2.hour);
	    	i.putExtra("minute", alarm2.minute);
	    	i.putExtra("snoozeTime", alarm2.snooze);
            startActivityForResult(i, SET_ALARM_REQUEST);
	    }
	};
	
	View.OnClickListener settings3Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(alarm3ToggleButton.isChecked())
	    	{
	    		isActive = ALARM_ALREADY_ACTIVE;
	    	}
	    	else {
	    		isActive = ALARM_NOT_ACTIVE;
	    	}
	    	
	    	Intent i = new Intent(getApplicationContext(), AlarmSettings.class);
	    	i.putExtra("index", ALARM_3_INDEX);
	    	i.putExtra("isActive", isActive);
	    	i.putExtra("hour", alarm3.hour);
	    	i.putExtra("minute", alarm3.minute);
	    	i.putExtra("snoozeTime", alarm3.snooze);
            startActivityForResult(i, SET_ALARM_REQUEST);
	    }
	};

	View.OnClickListener alarm1ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		
	            if(alarm1ToggleButton.isChecked()){
	            	//activated toggle button
	            	activateAlarmFor(ALARM_1_INDEX, alarm1.hour, alarm1.minute, alarm1.snooze);
	            }
	            else {
	            	//toggle button is inactive
	            	deactivateAlarmFor(ALARM_1_INDEX);
	            }	
	    	}
	  	};
	
	  	View.OnClickListener alarm2ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	            if(alarm2ToggleButton.isChecked()){
	            	//activated toggle button
	            	activateAlarmFor(ALARM_2_INDEX, alarm2.hour, alarm2.minute, alarm2.snooze);
	            }
	            else {
	            	//deactivated toggle button
	            	deactivateAlarmFor(ALARM_2_INDEX);
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener alarm3ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	            if(alarm3ToggleButton.isChecked()){
	            	//activated toggle button
	            	activateAlarmFor(ALARM_3_INDEX, alarm3.hour, alarm3.minute, alarm3.snooze);
	            }
	            else {
	            	//deacivated toggle button
	            	deactivateAlarmFor(ALARM_3_INDEX);
	            }	
	    	}
	  	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			if(requestCode == SET_ALARM_REQUEST) {
				if(data != null ){
					int index = data.getExtras().getInt("index");
					boolean isActive = data.getExtras().getBoolean("isActive");
					int hour = data.getExtras().getInt("hour");
					int minute = data.getExtras().getInt("minute");
					int snoozeTime = data.getExtras().getInt("snoozeTime");
					
		            setButtonText(index, hour, minute);
					
					alarms_list[index].setHour(hour);
					alarms_list[index].setMinute(minute);
					alarms_list[index].setSnooze(snoozeTime);
					
					// If alarm was already active and changed by the user, it's needed to replace the old one by the new
					// without asking for the user to click the togglebutton
					if(isActive) 
						activateAlarmFor(index, hour, minute, snoozeTime);
				}
			}
		}
	}
		
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}				
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putInt(STATE_ALARM1_HOUR, alarm1.hour);
	    savedInstanceState.putInt(STATE_ALARM2_HOUR, alarm2.hour);
	    savedInstanceState.putInt(STATE_ALARM3_HOUR, alarm3.hour);
	    savedInstanceState.putInt(STATE_ALARM1_MINUTE, alarm1.minute);
	    savedInstanceState.putInt(STATE_ALARM2_MINUTE, alarm2.minute);
	    savedInstanceState.putInt(STATE_ALARM3_MINUTE, alarm3.minute);
	    savedInstanceState.putInt(STATE_ALARM1_SNOOZE, alarm1.snooze);
	    savedInstanceState.putInt(STATE_ALARM2_SNOOZE, alarm2.snooze);
	    savedInstanceState.putInt(STATE_ALARM3_SNOOZE, alarm3.snooze);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brand_new_day, menu);
		return true;
	}

        
	void setButtonText(int index, int hour, int minute) {
		String hour_string = formatter.format(hour);
		String minute_string = formatter.format(minute);
		if(index == 0) {
			alarm1ToggleButton.setText(hour_string + ':' + minute_string);
			alarm1ToggleButton.setTextOff(hour_string + ':' + minute_string);
		    alarm1ToggleButton.setTextOn(hour_string + ':' + minute_string);
		}
		else if(index == 1) {
			alarm2ToggleButton.setText(hour_string + ':' + minute_string);
			alarm2ToggleButton.setTextOff(hour_string + ':' + minute_string);
		    alarm2ToggleButton.setTextOn(hour_string + ':' + minute_string);
		}
		else if(index == 2) {
			alarm3ToggleButton.setText(hour_string + ':' + minute_string);
			alarm3ToggleButton.setTextOff(hour_string + ':' + minute_string);
		    alarm3ToggleButton.setTextOn(hour_string + ':' + minute_string);
		}
	}

	public void activateAlarmFor(int index, int hour, int minute, int snoozeTime) {
		Toast.makeText(getApplicationContext(), "alarme ativado", 2).show();
		Intent i = new Intent(BrandNewDay.this, AlarmService.class);
		i.putExtra("hour", alarms_list[index].hour);
		i.putExtra("minute", alarms_list[index].minute);
		i.putExtra("snoozeTime", alarms_list[index].snooze);
		String hourr = new Integer(hour).toString();
		Toast.makeText(getApplicationContext(), "tempo" + hourr, 3).show();
		
		//this will cancel the old alarm request and set the new one
		pendingIntent = PendingIntent.getService(BrandNewDay.this, index, i, 0); 
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar alarm_time = Calendar.getInstance();
		Calendar rightNow = Calendar.getInstance();
		
		rightNow.setTimeInMillis(System.currentTimeMillis());
		rightNow.set(Calendar.SECOND, 0);
		
		alarm_time.setTimeInMillis(System.currentTimeMillis());
		alarm_time.set(Calendar.HOUR_OF_DAY, alarms_list[index].hour);
		alarm_time.set(Calendar.MINUTE, alarms_list[index].minute);
		alarm_time.set(Calendar.SECOND,0);
		
		if(rightNow.compareTo(alarm_time) == 0) {
			//equal alarms
			alarm_time.add(Calendar.DAY_OF_YEAR, 1); //set to next day
		}
		else if(rightNow.compareTo(alarm_time) == 1) {
			//rightNow is later 
			alarm_time.add(Calendar.DAY_OF_YEAR, 1); //set to next day
		}
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pendingIntent);
		Toast.makeText(getApplicationContext(), "enviou pedido", 2).show();
	}
	
	public void deactivateAlarmFor(int index){
		Toast.makeText(getApplicationContext(), "alarme desativado", 2).show();
		Intent i = new Intent(BrandNewDay.this, AlarmService.class);
		i.putExtra("hour", alarms_list[index].hour);
		i.putExtra("minute", alarms_list[index].minute);
		i.putExtra("snoozeTime", alarms_list[index].snooze);
		
		//this will cancel the old alarm
		pendingIntent = PendingIntent.getService(BrandNewDay.this, index, i, 0); 
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
    	alarmManager.cancel(pendingIntent);
	}
}
		
        
        