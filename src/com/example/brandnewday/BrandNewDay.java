package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BrandNewDay extends Activity {
	public MyApplication myApplication;	
	public int[] alarmHours;
	public int[] alarmMinutes;
	public int[] alarmSnoozes;
	public int[] volumes;
	public boolean[] alarmActivated;
	PendingIntent pendingIntent;
	ArrayList<Uri> audioUris;
	
	static final int SET_ALARM_REQUEST = 1;
	static NumberFormat formatter = new DecimalFormat("00");
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	static final boolean ALARM_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	int volumeCrescent = 0;
	int volumeLow = 1;
	int volumeMedium = 2;
	int volumeHigh = 3;
	int nap;

	ToggleButton alarm1ToggleButton;
	ToggleButton alarm2ToggleButton;
	ToggleButton alarm3ToggleButton;
	ToggleButton alarmNapToggleButton;
	Button alarm1SettingsButton;
	Button alarm2SettingsButton;
	Button alarmNapSettingsButton;
	Button playlistButton;
	SeekBar napSeekBar;
	Boolean isActive;
	String alarm1ToggleButtonText;
	String alarm2ToggleButtonText;
	String alarm3ToggleButtonText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.brand_new_day);
		getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.blue));
		

		myApplication = getMyApplication();
		alarmHours = new int[3];
		alarmMinutes = new int[3];
		alarmSnoozes = new int[3];
		alarmActivated = new boolean[4];
		volumes = new int[4];
		
		

		//PREFERENCES
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmHours[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Hour", 8);
		alarmMinutes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Minute", 15);
		alarmSnoozes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = defaultPreferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = defaultPreferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_NAP_INDEX] = defaultPreferences.getBoolean("alarmNapActivated", false);
		volumes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Volume", volumeMedium);
		volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeMedium);
		nap = defaultPreferences.getInt("nap", 20);
		//////

		alarm1ToggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		alarm2ToggleButton = (ToggleButton)findViewById(R.id.toggleButton2);
	    alarmNapToggleButton = (ToggleButton)findViewById(R.id.nap_toggleButton);
	    alarm1SettingsButton = (Button)findViewById(R.id.alarm1_settings_button);
	    alarm2SettingsButton = (Button)findViewById(R.id.alarm2_settings_button);
	    alarmNapSettingsButton = (Button)findViewById(R.id.nap_settings_button);
	    napSeekBar = (SeekBar) findViewById(R.id.nap_seekBar);
	    playlistButton = (Button)findViewById(R.id.playlist_button);
	    
	    
	        
	    alarm1ToggleButton.setChecked(alarmActivated[ALARM_1_INDEX]);
		setButtonText(ALARM_1_INDEX);
		alarm2ToggleButton.setChecked(alarmActivated[ALARM_2_INDEX]);
		setButtonText(ALARM_2_INDEX);
		alarmNapToggleButton.setChecked(alarmActivated[ALARM_NAP_INDEX]);
		setButtonText(ALARM_NAP_INDEX);
	
	    alarm1SettingsButton.setOnClickListener(settings1Listener);
	    alarm2SettingsButton.setOnClickListener(settings2Listener);
	    alarmNapSettingsButton.setOnClickListener(settingsNapListener);
	    
	    playlistButton.setOnClickListener(playlistButtonListener);
	    alarm1ToggleButton.setOnClickListener(alarm1ToggleButtonListener);
	    alarm2ToggleButton.setOnClickListener(alarm2ToggleButtonListener);
	    alarmNapToggleButton.setOnClickListener(alarmNapToggleButtonListener);
	    napSeekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
	    napSeekBar.setVisibility(View.GONE);
	    //setNapButton.setOnClickListener(setNapButtonListener);
	}
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmHours[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Hour", 8);
		alarmMinutes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Minute", 15);
		alarmSnoozes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = defaultPreferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = defaultPreferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_NAP_INDEX] = defaultPreferences.getBoolean("alarmNapActivated", false);
		volumes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Volume", volumeMedium);
		volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeMedium);
		nap = defaultPreferences.getInt("nap", 20);

		alarm1ToggleButton.setChecked(alarmActivated[ALARM_1_INDEX]);
		setButtonText(ALARM_1_INDEX);
		alarm2ToggleButton.setChecked(alarmActivated[ALARM_2_INDEX]);
		setButtonText(ALARM_2_INDEX);
		alarmNapToggleButton.setChecked(alarmActivated[ALARM_NAP_INDEX]);
		alarmNapToggleButton.setText(Integer.toString(nap) + " minutes");
		
		if(nap == 10)
			napSeekBar.setProgress(4);
		else if(nap == 15)
			napSeekBar.setProgress(12);
		else if(nap == 20)
			napSeekBar.setProgress(20);
		else if(nap == 25)
			napSeekBar.setProgress(28);
		else if(nap == 30)
			napSeekBar.setProgress(36);
		else if(nap == 35)
			napSeekBar.setProgress(44);
		else if(nap == 40)
			napSeekBar.setProgress(52);
		else if(nap == 45)
			napSeekBar.setProgress(60);
		else if(nap == 50)
			napSeekBar.setProgress(68);
		else if(nap == 55)
			napSeekBar.setProgress(76);
		else if(nap == 60)
			napSeekBar.setProgress(84);
		else if(nap == 75)
			napSeekBar.setProgress(92);
		else if(nap == 90)
			napSeekBar.setProgress(100);
		
		napSeekBar.setVisibility(View.GONE);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}
		
	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = defaultPreferences.edit();

		editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
		editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
		editor.putInt("alarm3Hour", alarmHours[ALARM_3_INDEX]);
		editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
		editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
		editor.putInt("alarm3Minute", alarmMinutes[ALARM_3_INDEX]);
		editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
		editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);
		editor.putInt("alarm1Volume", volumes[ALARM_1_INDEX]);
		editor.putInt("alarm2Volume", volumes[ALARM_2_INDEX]);
		editor.putInt("alarmNapVolume", volumes[ALARM_NAP_INDEX]);
		editor.putInt("nap", nap);
		
		editor.commit();	
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = defaultPreferences.edit();

		editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
		editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
		editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
		editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
		editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
		editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);
		editor.putInt("alarm1Volume", volumes[ALARM_1_INDEX]);
		editor.putInt("alarm2Volume", volumes[ALARM_2_INDEX]);
		editor.putInt("alarmNapVolume", volumes[ALARM_NAP_INDEX]);
		editor.putInt("nap", nap);
		editor.commit();
	}
	
	SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if(progress <  8)
				nap = 10;
			else if(progress < 16)
				nap = 15;
			else if(progress < 24)
				nap = 20;
			else if(progress < 32)
				nap = 25;
			else if(progress < 40)
				nap = 30;
			else if(progress < 48)
				nap = 35;
			else if(progress < 56)
				nap = 40;
			else if(progress < 64)
				nap = 45;
			else if(progress < 72)
				nap = 50;
			else if(progress < 80)
				nap = 55;
			else if(progress < 88)
				nap = 60;
			else if(progress < 94)
				nap = 75;
			else if(progress < 100)
				nap = 90;
			
			alarmNapToggleButton.setText(Integer.toString(nap) + " minutes");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			saveNapPreferences();
		}
		public void saveNapPreferences(){
			SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = defaultPreferences.edit();
			
			editor.putInt("nap", nap);
			editor.commit();
		};
	};

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
	    	Log.d("nap", Integer.toString(nap));
	    	if(napSeekBar.isActivated() == false){
	    		napSeekBar.setVisibility(View.VISIBLE);
	    		napSeekBar.setActivated(true);
	    	}
	    	else {
	    		napSeekBar.setVisibility(View.GONE);
	    		napSeekBar.setActivated(false);
	    	}
	    		
	    }
	};

	View.OnClickListener alarm1ToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		Log.d("TAG", "OI");
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
	  	

	  	View.OnClickListener alarmNapToggleButtonListener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		alarmNapToggleButton.setText(Integer.toString(nap) + " minutes");
	    		napSeekBar.setVisibility(View.GONE);
	    		napSeekBar.setActivated(false);
	    		if(alarmNapToggleButton.isChecked()){
	            	//activated toggle button
	            	alarmActivated[ALARM_NAP_INDEX] = true;
	            	myApplication.activateNap(nap);
	            }
	            else {
	            	//toggle button is inactive
	            	alarmActivated[ALARM_NAP_INDEX] = false;
	            	myApplication.deactivateNap();
	            }	
	    	
	    	}
	  	};
	  	
	  	View.OnClickListener playlistButtonListener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	Intent i = new Intent(getApplicationContext(), SongList.class);
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
			if(index == ALARM_1_INDEX) {
				alarm1ToggleButton.setText(hour_string + ':' + minute_string);
				alarm1ToggleButton.setTextOff(hour_string + ':' + minute_string);
			    alarm1ToggleButton.setTextOn(hour_string + ':' + minute_string);
			}
			else if(index == ALARM_2_INDEX) {
				alarm2ToggleButton.setText(hour_string + ':' + minute_string);
				alarm2ToggleButton.setTextOff(hour_string + ':' + minute_string);
			    alarm2ToggleButton.setTextOn(hour_string + ':' + minute_string);
			}
			else if(index == ALARM_NAP_INDEX) {
				alarmNapToggleButton.setText(Integer.toString(nap));
				alarmNapToggleButton.setTextOff(Integer.toString(nap));
				alarmNapToggleButton.setTextOn(Integer.toString(nap));
			}
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


        
        