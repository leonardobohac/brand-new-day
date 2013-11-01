package com.example.brandnewday;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pig.impl.util.ObjectSerializer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class BrandNewDay extends Activity {
	public MyApplication myApplication;
	
	public int[] alarmHours;
	public int[] alarmMinutes;
	public int[] alarmSnoozes;
	public boolean[] alarmActivated;
	//private ArrayList<Uri> audioUris;
	//private Set<String> audioUrisInStringSet = new HashSet<String>();
	//private ArrayList<String> audioPaths;
	PendingIntent pendingIntent;
	/*public Set<String> audioUrisInStringSet; 
	private Set<String> emptySet = new HashSet<String>();*/
	ArrayList<Uri> audioUris;
	
	
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

	String string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.brand_new_day);
		myApplication = getMyApplication();
		
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		alarmActivated = myApplication.getAlarmActivated();

	

		
		//PREFERENCES
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmHours[ALARM_1_INDEX] = preferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = preferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = preferences.getInt("alarm3Hour", 9);
		alarmMinutes[ALARM_1_INDEX] = preferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = preferences.getInt("alarm2Minute", 15);
		alarmMinutes[ALARM_3_INDEX] = preferences.getInt("alarm3Minute", 45);
		alarmSnoozes[ALARM_1_INDEX] = preferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = preferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = preferences.getInt("alarm3Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = preferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = preferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = preferences.getBoolean("alarm3Activated", false);

				
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
	}
		
	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
		editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
		editor.putInt("alarm3Hour", alarmHours[ALARM_3_INDEX]);
		editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
		editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
		editor.putInt("alarm3Minute", alarmMinutes[ALARM_3_INDEX]);
		editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
		editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
		editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		//editor.putString("STRINGS", audioArrayInString);

		editor.commit();
		
		audioUris = myApplication.getAudioUris();
		if(audioUris.size() != 0) {
			try {
				string = myApplication.serialize(audioUris);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor new_editor = defaultPreferences.edit();
			System.out.println("Storing string for " + audioUris.toString());
			new_editor.putString("audioStringFromPreferences", string);
			new_editor.commit();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
		editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
		editor.putInt("alarm3Hour", alarmHours[ALARM_3_INDEX]);
		editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
		editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
		editor.putInt("alarm3Minute", alarmMinutes[ALARM_3_INDEX]);
		editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
		editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
		editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		//editor.putString("STRINGS", audioArrayInString);
		editor.commit();
		
		audioUris = myApplication.getAudioUris();
		if(audioUris.size() != 0) {
			try {
				string = myApplication.serialize(audioUris);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor new_editor = defaultPreferences.edit();
			new_editor.putString("audioStringFromPreferences", string);
			new_editor.commit();
		}
		
	}
	
	
	
	

	View.OnClickListener settings1Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_1_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings2Listener = new View.OnClickListener() {
	    public void onClick(View v) {	    	
	    	Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
	    	intent.putExtra("index", ALARM_2_INDEX);
            //startActivityForResult(intent, SET_ALARM_REQUEST);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings3Listener = new View.OnClickListener() {
	    public void onClick(View v) {
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
        
					
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brand_new_day, menu);
		return true;
	}

        
	public void setButtonText(int index) {
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
	
	public MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	
	
//////////// Preferences Getters /////////////////	
	public void getHoursPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmHours[ALARM_1_INDEX] = preferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = preferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = preferences.getInt("alarm3Hour", 9);
	}
	public void getMinutesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmMinutes[ALARM_1_INDEX] = preferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = preferences.getInt("alarm2Minute", 15);
		alarmMinutes[ALARM_3_INDEX] = preferences.getInt("alarm3Minute", 45);
	}
	public void getSnoozesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmSnoozes[ALARM_1_INDEX] = preferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = preferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = preferences.getInt("alarm3Snooze", 0);
	}
	public void getActivatedPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmActivated[ALARM_1_INDEX] = preferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = preferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = preferences.getBoolean("alarm3Activated", false);
	}
	
	/*public void getAudioUrisInStringPreferences() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		audioUrisInStringSet = preferences.getStringSet("AudioUrisInStringSet", emptySet);
	}*/
	
//////////// Preferences Setters /////////////////		
	public void setHoursPreferences() {
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
		editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
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
	
	/*public void setAudioUrisInStringPreferences() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet("AudioUrisInStringSet", audioUrisInStringSet);
		editor.commit();
	}*/
	
	
	
	
}


        
        