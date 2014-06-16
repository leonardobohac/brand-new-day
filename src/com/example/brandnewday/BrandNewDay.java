package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
	int nap_time;

	RelativeLayout playlist;
	RelativeLayout alarm_1;
	RelativeLayout alarm_2;
	RelativeLayout alarm_3;
	RelativeLayout nap;
	
	ImageView check_alarm_1;
	ImageView check_alarm_2;
	ImageView check_alarm_3;
	
	TextView alarm_1_textView;
	TextView alarm_2_textView;
	TextView alarm_3_textView;
	TextView nap_textView;
	
	SeekBar nap_seekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.brand_new_day);
		//getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.blue));
		
		myApplication = getMyApplication();
		alarmHours = new int[4];
		alarmMinutes = new int[4];
		alarmSnoozes = new int[3];
		alarmActivated = new boolean[4];
		volumes = new int[4];
		
		//PREFERENCES
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmHours[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Hour", 8);
		alarmHours[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Hour", 10);
		alarmHours[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Hour", 9);
		alarmMinutes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Minute", 10);
		alarmMinutes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Minute", 30);
		alarmSnoozes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = defaultPreferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = defaultPreferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = defaultPreferences.getBoolean("alarm3Activated", false);
		alarmActivated[ALARM_NAP_INDEX] = defaultPreferences.getBoolean("alarmNapActivated", false);
		volumes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Volume", volumeMedium);
		volumes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Volume", volumeMedium);
		//volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeMedium);
		nap_time = defaultPreferences.getInt("nap_time", 30);
		//////
		
		playlist = (RelativeLayout)findViewById(R.id.playlist);
		alarm_1 = (RelativeLayout)findViewById(R.id.alarm_1);
		alarm_2 = (RelativeLayout)findViewById(R.id.alarm_2);
		alarm_3 = (RelativeLayout)findViewById(R.id.alarm_3);
		nap = (RelativeLayout)findViewById(R.id.nap);
		
		check_alarm_1 = (ImageView)findViewById(R.id.check_alarm_1);
		check_alarm_2 = (ImageView)findViewById(R.id.check_alarm_2);
		check_alarm_3 = (ImageView)findViewById(R.id.check_alarm_3);
	    
		alarm_1_textView = (TextView)findViewById(R.id.alarm_1_textView);
		alarm_2_textView = (TextView)findViewById(R.id.alarm_2_textView);
		alarm_3_textView = (TextView)findViewById(R.id.alarm_3_textView);
		nap_textView = (TextView)findViewById(R.id.nap_textView);
		
		nap_seekBar = (SeekBar)findViewById(R.id.nap_seekBar);
	    
		set_alarm_text(ALARM_1_INDEX);
		set_alarm_text(ALARM_2_INDEX);
		set_alarm_text(ALARM_3_INDEX);
		set_alarm_text(ALARM_NAP_INDEX);
	
	    alarm_1.setOnClickListener(alarm_1_listener);
	    alarm_2.setOnClickListener(alarm_2_listener);
	    alarm_3.setOnClickListener(alarm_3_listener);
	    
	    check_alarm_1.setOnClickListener(check_alarm_1_listener);
	    check_alarm_2.setOnClickListener(check_alarm_2_listener);
	    check_alarm_3.setOnClickListener(check_alarm_3_listener);
	    nap.setOnClickListener(nap_listener);
	    
	    playlist.setOnClickListener(playlist_listener);
	    nap_seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);    
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
		//volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeMedium);
		nap_time = defaultPreferences.getInt("nap_time", 20);

		set_alarm_text(ALARM_1_INDEX);
		set_alarm_text(ALARM_2_INDEX);
		set_alarm_text(ALARM_3_INDEX);
		set_alarm_text(ALARM_NAP_INDEX);

		
		if(nap_time == 10)
			nap_seekBar.setProgress(4);
		else if(nap_time == 15)
			nap_seekBar.setProgress(12);
		else if(nap_time == 20)
			nap_seekBar.setProgress(20);
		else if(nap_time == 25)
			nap_seekBar.setProgress(28);
		else if(nap_time == 30)
			nap_seekBar.setProgress(36);
		else if(nap_time == 35)
			nap_seekBar.setProgress(44);
		else if(nap_time == 40)
			nap_seekBar.setProgress(52);
		else if(nap_time == 45)
			nap_seekBar.setProgress(60);
		else if(nap_time == 50)
			nap_seekBar.setProgress(68);
		else if(nap_time == 55)
			nap_seekBar.setProgress(76);
		else if(nap_time == 60)
			nap_seekBar.setProgress(84);
		else if(nap_time == 75)
			nap_seekBar.setProgress(92);
		else if(nap_time == 90)
			nap_seekBar.setProgress(100);
		
		
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
		//editor.putInt("alarmNapVolume", volumes[ALARM_NAP_INDEX]);
		editor.putInt("nap_time", nap_time);
		
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
		//editor.putInt("alarmNapVolume", volumes[ALARM_NAP_INDEX]);
		editor.putInt("nap_time", nap_time);
		editor.commit();
	}
	
	SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if(progress < 2)
				nap_time = 1;
			else if(progress < 4)
				nap_time = 2;
			else if(progress < 6)
				nap_time = 3;
			else if(progress < 8)
				nap_time = 4;
			else if(progress < 10)
				nap_time = 5;
			else if(progress < 12)
				nap_time = 6;
			else if(progress < 14)
				nap_time = 7;
			else if(progress < 16)
				nap_time = 8;
			else if(progress < 18)
				nap_time = 9;
			else if(progress < 20)
				nap_time = 10;
			else if(progress < 24)
				nap_time = 15;
			else if(progress < 30)
				nap_time = 20;
			else if(progress < 36)
				nap_time = 25;
			else if(progress < 42)
				nap_time = 30;
			else if(progress < 48)
				nap_time = 35;
			else if(progress < 54)
				nap_time = 40;
			else if(progress < 60)
				nap_time = 45;
			else if(progress < 66)
				nap_time = 50;
			else if(progress < 72)
				nap_time = 55;
			else if(progress < 78)
				nap_time = 60;
			else if(progress < 86)
				nap_time = 75;
			else if(progress < 94)
				nap_time = 90;
			else if(progress <= 100)
				nap_time = 120;
			nap_textView.setText(Integer.toString(nap_time));
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
			
			editor.putInt("nap", nap_time);
			editor.commit();
		};
	};

	
	View.OnClickListener alarm_1_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_1_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener alarm_2_listener = new View.OnClickListener() {
	    public void onClick(View v) {	    	
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_2_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener alarm_3_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_3_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener check_alarm_1_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(alarmActivated[ALARM_1_INDEX] == false){
	            	alarmActivated[ALARM_1_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_1_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_1_textView.setTextColor(getResources().getColor(R.color.blue));
	            }
	            else {
	            	alarmActivated[ALARM_1_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_1_INDEX);
	            	alarm_1_textView.setTextColor(Color.WHITE);
	            }	
	    	}
	  	};
	
	  	View.OnClickListener check_alarm_2_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(alarmActivated[ALARM_2_INDEX] == false){
	            	alarmActivated[ALARM_2_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_2_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_1_textView.setTextColor(getResources().getColor(R.color.blue));
	            }
	            else {
	            	alarmActivated[ALARM_2_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_2_INDEX);
	            	alarm_2_textView.setTextColor(Color.WHITE);
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener check_alarm_3_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(alarmActivated[ALARM_3_INDEX] == false){
	            	alarmActivated[ALARM_3_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_3_INDEX, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_1_textView.setTextColor(getResources().getColor(R.color.blue));
	            }
	            else {
	            	alarmActivated[ALARM_3_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_1_INDEX);
	            	alarm_3_textView.setTextColor(Color.WHITE);
	            }	
	    	}
	  	};
	
	  	View.OnClickListener nap_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(alarmActivated[ALARM_NAP_INDEX] = false){
	            	alarmActivated[ALARM_NAP_INDEX] = true;
	            	nap_textView.setTextColor((getResources().getColor(R.color.blue)));
	            	myApplication.activateNap(nap_time);
	            	
	            }
	            else {
	            	alarmActivated[ALARM_NAP_INDEX] = false;
	            	nap_textView.setTextColor(Color.WHITE);
	            	myApplication.deactivateNap();
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener playlist_listener = new View.OnClickListener() {
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

        
	public void set_alarm_text(int index) {
		String hour_string = formatter.format(alarmHours[index]);
		String minute_string = formatter.format(alarmMinutes[index]);
			if(index == ALARM_1_INDEX)
				alarm_1_textView.setText(hour_string + ':' + minute_string);
			else if(index == ALARM_2_INDEX)
				alarm_2_textView.setText(hour_string + ':' + minute_string);
			else if(index == ALARM_3_INDEX)
				alarm_3_textView.setText(hour_string + ':' + minute_string);
			else if(index == ALARM_NAP_INDEX)
				nap_textView.setText(Integer.toString(nap_time));			
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
	
	



        
        