package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class BrandNewDay extends Activity {
	MyApplication myApplication;
	
	int[] alarmHours, alarmMinutes, alarmSnoozes;
	boolean[] alarmActivated;
	ArrayList<String> myStringPlaylist;
	
	/*private int Alarm1DefaultHour = 7;
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
	private boolean Alarm3DefaultActive = false;*/
	
	static final int SET_ALARM_REQUEST = 1;
	static NumberFormat formatter = new DecimalFormat("00");
	
	
	
	
	/*static final String STATE_ALARM1_HOUR = "alarm1_hour";
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
	static final int ALARM_QUICKNAP_INDEX = 3;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;*/
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	static final boolean ALARM_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	String hoursPreferences = "hoursPreferences";
	String minutesPreferences = "minutesPreferences";
	String snoozesPreferences = "snoozesPreferences";
	String activatedPreferences = "activatedPreferences";
	
	ToggleButton alarm1ToggleButton;
	ToggleButton alarm2ToggleButton;
	ToggleButton alarm3ToggleButton;
	ToggleButton alarmNapToggleButton;
	Button alarm1SettingsButton;
	Button alarm2SettingsButton;
	Button alarm3SettingsButton;
	Button playlistButton;
	Boolean isActive;
	String alarm1ToggleButtonText;
	String alarm2ToggleButtonText;
	String alarm3ToggleButtonText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Main Activity Creating");
		setContentView(R.layout.brand_new_day);
		myApplication = getMyApplication();
		
		//myStringPlaylist = myApplication.getMyStringPlaylist();
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		alarmActivated = myApplication.getAlarmActivated();
		
		System.out.println("Here: " + (Integer.toString(alarmHours[0])));
		
		alarm1ToggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		alarm2ToggleButton = (ToggleButton)findViewById(R.id.toggleButton2);
		alarm3ToggleButton = (ToggleButton)findViewById(R.id.toggleButton3);
	    alarmNapToggleButton = (ToggleButton)findViewById(R.id.quickNapToggleButton);
	    alarm1SettingsButton = (Button)findViewById(R.id.alarm1_settings_button);
	    alarm2SettingsButton = (Button)findViewById(R.id.alarm2_settings_button);
	    alarm3SettingsButton = (Button)findViewById(R.id.alarm3_settings_button);
	    playlistButton = (Button)findViewById(R.id.playlist_button);
			
		alarm1ToggleButton.setChecked(alarmActivated[ALARM_1_INDEX]);
		setButtonText(ALARM_1_INDEX);
		alarm2ToggleButton.setChecked(alarmActivated[ALARM_2_INDEX]);
		setButtonText(ALARM_2_INDEX);
		alarm3ToggleButton.setChecked(alarmActivated[ALARM_3_INDEX]);
		setButtonText(ALARM_3_INDEX);
		alarmNapToggleButton.setChecked(alarmActivated[ALARM_NAP_INDEX]);
		setButtonText(ALARM_NAP_INDEX);
	
	    alarm1SettingsButton.setOnClickListener(settings1Listener);
	    alarm2SettingsButton.setOnClickListener(settings2Listener);
	    alarm3SettingsButton.setOnClickListener(settings3Listener);
	    playlistButton.setOnClickListener(playlistButtonListener);
	    alarm1ToggleButton.setOnClickListener(alarm1ToggleButtonListener);
	    alarm2ToggleButton.setOnClickListener(alarm2ToggleButtonListener);
	    alarm3ToggleButton.setOnClickListener(alarm3ToggleButtonListener);
	    //alarmNapToggleButton.setOnClickListener(alarmNapToggleButtonListener);
	    
	}
	    
	  
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println(alarmHours.toString());
		System.out.println("Main Activity Resuming");
		alarm1ToggleButton.setChecked(alarmActivated[ALARM_1_INDEX]);
		setButtonText(ALARM_1_INDEX);
		alarm2ToggleButton.setChecked(alarmActivated[ALARM_2_INDEX]);
		setButtonText(ALARM_2_INDEX);
		alarm3ToggleButton.setChecked(alarmActivated[ALARM_3_INDEX]);
		setButtonText(ALARM_3_INDEX);
		alarmNapToggleButton.setChecked(alarmActivated[ALARM_NAP_INDEX]);
		setButtonText(ALARM_NAP_INDEX);
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("Main Activity Pausing");
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);

		editor.commit();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("Main Activity Stopping");
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);

		editor.commit();
	}
		
	
	/*@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("Main Activity Destroying");
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);

		editor.commit();
	}*/
	
	/*View.OnClickListener playMusicListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Intent i = new Intent(getApplicationContext(), WakingTime.class);
            startActivity(i);
	    }
	};*/
	
	
	View.OnClickListener settings1Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	/*if(alarm1ToggleButton.isChecked())
	    	{
	    		alarmActivated[ALARM_1_INDEX] = ALARM_ACTIVE;
	    	}
	    	else {
	    		alarmActivated[ALARM_1_INDEX] = ALARM_NOT_ACTIVE;
	    	}*/
	    	
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_1_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings2Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	/*if(alarm2ToggleButton.isChecked())
	    	{
	    		alarmActivated[ALARM_2_INDEX] = ALARM_ACTIVE;
	    	}
	    	else {
	    		alarmActivated[ALARM_2_INDEX] = ALARM_NOT_ACTIVE;
	    	}*/
	    	
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_2_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings3Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	/*if(alarm3ToggleButton.isChecked())
	    	{
	    		alarmActivated[ALARM_3_INDEX] = ALARM_ACTIVE;
	    	}
	    	else {
	    		alarmActivated[ALARM_3_INDEX] = ALARM_NOT_ACTIVE;
	    	}*/
	    	
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_3_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settingsNapListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	/*if(alarmNapToggleButton.isChecked())
	    	{
	    		alarmActivated[ALARM_NAP_INDEX] = ALARM_ACTIVE;
	    	}
	    	else {
	    		alarmActivated[ALARM_NAP_INDEX] = ALARM_NOT_ACTIVE;
	    	}*/
	    	
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_NAP_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};

	View.OnClickListener alarm1ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {	            
	    		if(alarm1ToggleButton.isChecked()){
	            	alarmActivated[ALARM_1_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_1_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            }
	            else {
	            	alarmActivated[ALARM_1_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_1_INDEX);
	            }	
	    	}
	  	};
	
	  	View.OnClickListener alarm2ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {     
	    		if(alarm2ToggleButton.isChecked()){
	            	alarmActivated[ALARM_2_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_2_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            }
	            else {
	            	alarmActivated[ALARM_2_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_2_INDEX);
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener alarm3ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(alarm3ToggleButton.isChecked()){
	            	alarmActivated[ALARM_3_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_3_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            }
	            else {
	            	alarmActivated[ALARM_3_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_3_INDEX);
	            }	
	    	}
	  	};
	
	  	/*View.OnClickListener alarmNapToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		String hour = formatter.format(alarmHours[ALARM_NAP_INDEX]);
	    		String minute = formatter.format(alarmMinutes[ALARM_NAP_INDEX]);
	    		alarmNapToggleButton.setText(hour + ":" + minute);
	            
	    		if(alarmNapToggleButton.isChecked()){
	            	//activated toggle button
	            	alarmActivated[ALARM_NAP_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_NAP_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            }
	            else {
	            	//toggle button is inactive
	            	alarmActivated[ALARM_NAP_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_NAP_INDEX);
	            }	
	    	}
	  	};*/
	  	
	  	View.OnClickListener playlistButtonListener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	Intent i = new Intent(getApplicationContext(), MyPlaylist.class);
                startActivity(i);
            }
	  	};
        
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			if(requestCode == SET_ALARM_REQUEST) {
				if(data != null ){
					int index = data.getExtras().getInt("index");
		            setButtonText(index);
				}
			}
		}
	}*/
					
	
	/*@Override
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
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brand_new_day, menu);
		return true;
	}

        
	void setButtonText(int index) {
		if(index < 3){
		String hour_string = formatter.format(alarmHours[index]);
		String minute_string = formatter.format(alarmMinutes[index]);
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
		else {
			alarmNapToggleButton.setText("Nap");
			alarmNapToggleButton.setTextOff("Nap");
			alarmNapToggleButton.setTextOn("Nap");
		}
	}
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	public void getHoursPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmHours[ALARM_1_INDEX] = preferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = preferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = preferences.getInt("alarm3Hour", 9);
	}
	
	public void getMinutesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmMinutes[ALARM_1_INDEX] = preferences.getInt("alarm1Minutes", 30);
		alarmMinutes[ALARM_2_INDEX] = preferences.getInt("alarm2Minutes", 30);
		alarmMinutes[ALARM_3_INDEX] = preferences.getInt("alarm3Minutes", 30);
	}
	
	public void setHourPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
		editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
		editor.putInt("alarm3Hour", alarmHours[ALARM_3_INDEX]);
	
		editor.commit();
	}
	
	public void setMinutesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt("alarm1Minutes", alarmMinutes[ALARM_1_INDEX]);
		editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
		editor.putInt("alarm3Minute", alarmMinutes[ALARM_3_INDEX]);
	
		editor.commit();
	}
	
	public void setSnoozesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
		editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
		editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
	
		editor.commit();
	}
	
	public void setActivatedPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
	
		editor.commit();
	}
}


        
        