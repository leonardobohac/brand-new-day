package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BrandNewDay extends Activity {
	public MyApplication myApplication;	
	public int[] alarmHours;
	public int[] alarmMinutes;
	public int[] alarmSnoozes;
	public float[] volumes;
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
	float volumeMedium = 0.5f;
	int nap_time;
	
	public ArrayList<String> alarm1_days;
	public ArrayList<String> alarm2_days;
	public ArrayList<String> alarm3_days;
	public ArrayList<ArrayList<String>> alarm_days;
	public String alarm1_days_preferences;
	public String alarm2_days_preferences;
	public String alarm3_days_preferences;

	RelativeLayout playlist;
	RelativeLayout alarm_1;
	RelativeLayout alarm_2;
	RelativeLayout alarm_3;
	RelativeLayout nap;
	
	ImageView settings_alarm_1;
	ImageView settings_alarm_2;
	ImageView settings_alarm_3;
	
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
		alarmHours = new int[3];
		alarmMinutes = new int[3];
		alarmSnoozes = new int[3];
		alarmActivated = new boolean[4];
		volumes = new float[3];
	
		
		alarm_days = new ArrayList<ArrayList<String>>(3);
		alarm1_days = new ArrayList<String>();
		alarm2_days = new ArrayList<String>();
		alarm3_days = new ArrayList<String>();
		
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
		volumes[ALARM_1_INDEX] = defaultPreferences.getFloat("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getFloat("alarm2Volume", volumeMedium);
		volumes[ALARM_3_INDEX] = defaultPreferences.getFloat("alarm3Volume", volumeMedium);
		nap_time = defaultPreferences.getInt("nap_time", 30);
		alarm1_days_preferences = defaultPreferences.getString("alarm1_days_preferences", "");
		alarm2_days_preferences = defaultPreferences.getString("alarm2_days_preferences", "");
		alarm3_days_preferences = defaultPreferences.getString("alarm3_days_preferences", "");
		///////
		
		playlist = (RelativeLayout)findViewById(R.id.playlist);
		alarm_1 = (RelativeLayout)findViewById(R.id.alarm_1);
		alarm_2 = (RelativeLayout)findViewById(R.id.alarm_2);
		alarm_3 = (RelativeLayout)findViewById(R.id.alarm_3);
		nap = (RelativeLayout)findViewById(R.id.nap);
		
		settings_alarm_1 = (ImageView)findViewById(R.id.settings_alarm_1);
		settings_alarm_2 = (ImageView)findViewById(R.id.settings_alarm_2);
		settings_alarm_3 = (ImageView)findViewById(R.id.settings_alarm_3);
	    
		alarm_1_textView = (TextView)findViewById(R.id.alarm_1_textView);
		alarm_2_textView = (TextView)findViewById(R.id.alarm_2_textView);
		alarm_3_textView = (TextView)findViewById(R.id.alarm_3_textView);
		nap_textView = (TextView)findViewById(R.id.nap_textView);
		
		nap_seekBar = (SeekBar)findViewById(R.id.nap_seekBar);
	    
		set_alarm_text(ALARM_1_INDEX);
		set_alarm_text(ALARM_2_INDEX);
		set_alarm_text(ALARM_3_INDEX);
		Toast.makeText(getApplicationContext(), Boolean.toString(alarmActivated[ALARM_NAP_INDEX]), Toast.LENGTH_SHORT).show();
		set_alarm_text(ALARM_NAP_INDEX);
	
	    alarm_1.setOnClickListener(alarm_1_listener);
	    alarm_2.setOnClickListener(alarm_2_listener);
	    alarm_3.setOnClickListener(alarm_3_listener);
	    nap.setOnClickListener(nap_listener);
	    
	    settings_alarm_1.setOnClickListener(settings_alarm_1_listener);
	    settings_alarm_2.setOnClickListener(settings_alarm_2_listener);
	    settings_alarm_3.setOnClickListener(settings_alarm_3_listener);
	    
	    
	    playlist.setOnClickListener(playlist_listener);
	    nap_seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
	    
	    try {
			alarm1_days = myApplication.deserializeStrings(alarm1_days_preferences);
			alarm2_days = myApplication.deserializeStrings(alarm2_days_preferences);
			alarm3_days = myApplication.deserializeStrings(alarm3_days_preferences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	alarm_days.add(ALARM_1_INDEX, alarm1_days);
	alarm_days.add(ALARM_2_INDEX, alarm2_days);
	alarm_days.add(ALARM_3_INDEX, alarm3_days);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
		volumes[ALARM_1_INDEX] = defaultPreferences.getFloat("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getFloat("alarm2Volume", volumeMedium);
		volumes[ALARM_3_INDEX] = defaultPreferences.getFloat("alarm3Volume", volumeMedium);
		nap_time = defaultPreferences.getInt("nap_time", 30);

		
		set_alarm_text(ALARM_1_INDEX);
		set_alarm_text(ALARM_2_INDEX);
		set_alarm_text(ALARM_3_INDEX);
		set_alarm_text(ALARM_NAP_INDEX);

		
		if(nap_time == 1)
			nap_seekBar.setProgress(1);
		else if(nap_time == 2)
			nap_seekBar.setProgress(3);
		else if(nap_time == 3)
			nap_seekBar.setProgress(5);
		else if(nap_time == 4)
			nap_seekBar.setProgress(7);
		else if(nap_time == 5)
			nap_seekBar.setProgress(9);
		else if(nap_time == 6)
			nap_seekBar.setProgress(11);
		else if(nap_time == 7)
			nap_seekBar.setProgress(13);
		else if(nap_time == 8)
			nap_seekBar.setProgress(15);
		else if(nap_time == 9)
			nap_seekBar.setProgress(17);
		else if(nap_time == 10)
			nap_seekBar.setProgress(19);
		else if(nap_time == 15)
			nap_seekBar.setProgress(23);
		else if(nap_time == 20)
			nap_seekBar.setProgress(29);
		else if(nap_time == 25)
			nap_seekBar.setProgress(35);
		else if(nap_time == 30)
			nap_seekBar.setProgress(41);
		else if(nap_time == 35)
			nap_seekBar.setProgress(47);
		else if(nap_time == 40)
			nap_seekBar.setProgress(53);
		else if(nap_time == 45)
			nap_seekBar.setProgress(59);
		else if(nap_time == 50)
			nap_seekBar.setProgress(65);
		else if(nap_time == 55)
			nap_seekBar.setProgress(71);
		else if(nap_time == 60)
			nap_seekBar.setProgress(77);
		else if(nap_time == 75)
			nap_seekBar.setProgress(85);
		else if(nap_time == 90)
			nap_seekBar.setProgress(93);
		else if(nap_time == 120)
			nap_seekBar.setProgress(99);
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);
		editor.putFloat("alarm1Volume", volumes[ALARM_1_INDEX]);
		editor.putFloat("alarm2Volume", volumes[ALARM_2_INDEX]);
		editor.putFloat("alarm3Volume", volumes[ALARM_3_INDEX]);
		editor.putInt("nap_time", nap_time);
		
		editor.commit();
		
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
		editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
		editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
		editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
		editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);
		editor.putFloat("alarm1Volume", volumes[ALARM_1_INDEX]);
		editor.putFloat("alarm2Volume", volumes[ALARM_2_INDEX]);
		editor.putFloat("alarm3Volume", volumes[ALARM_3_INDEX]);
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
		editor.putBoolean("alarmNapActivated", alarmActivated[ALARM_NAP_INDEX]);
		editor.putFloat("alarm1Volume", volumes[ALARM_1_INDEX]);
		editor.putFloat("alarm2Volume", volumes[ALARM_2_INDEX]);
		editor.putFloat("alarm3Volume", volumes[ALARM_3_INDEX]);
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

	
	View.OnClickListener settings_alarm_1_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(playlist_is_empty()){
	    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
	    		return;
	    	}
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_1_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings_alarm_2_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(playlist_is_empty()){
	    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
	    		return;
	    	}
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_2_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener settings_alarm_3_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(playlist_is_empty()){
	    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
	    		return;
	    	}
	    	Intent intent = new Intent(getApplicationContext(), Settings.class);
	    	intent.putExtra("index", ALARM_3_INDEX);
	    	startActivity(intent);
	    }
	};
	
	View.OnClickListener alarm_1_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(playlist_is_empty()){
		    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
		    		return;
		    	}
	    		if(alarmActivated[ALARM_1_INDEX] == false){
	            	alarmActivated[ALARM_1_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_1_INDEX, alarm_days, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_1_textView.setTextColor(getResources().getColor(R.color.blueback));
	            	setBackground(alarm_1, getResources().getDrawable(R.drawable.btn_alarm_checked));
	            	
	            	String hour_string = formatter.format(alarmHours[ALARM_1_INDEX]);
					String minute_string = formatter.format(alarmMinutes[ALARM_1_INDEX]);
					String text = (hour_string + ":" + minute_string);
	            	Toast.makeText(getApplicationContext(), "Alarme ativado para " + text, Toast.LENGTH_SHORT).show();
	            }
	            else {
	            	alarmActivated[ALARM_1_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_1_INDEX);
	            	alarm_1_textView.setTextColor(Color.WHITE);
	            	setBackground(alarm_1, getResources().getDrawable(R.drawable.btn_alarm));
	            	Toast.makeText(getApplicationContext(), "Alarme Desativado", Toast.LENGTH_SHORT).show();
	            	
	            }	
	    	}
	  	};
	
	  	View.OnClickListener alarm_2_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(playlist_is_empty()){
		    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
		    		return;
		    	}
	    		if(alarmActivated[ALARM_2_INDEX] == false){
	            	alarmActivated[ALARM_2_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_2_INDEX, alarm_days, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_2_textView.setTextColor(getResources().getColor(R.color.blueback));
	            	setBackground(alarm_2, getResources().getDrawable(R.drawable.btn_alarm_checked));
	            	
	            	String hour_string = formatter.format(alarmHours[ALARM_2_INDEX]);
					String minute_string = formatter.format(alarmMinutes[ALARM_2_INDEX]);
					String text = (hour_string + ":" + minute_string);
					Toast.makeText(getApplicationContext(), "Alarme ativado para " + text, Toast.LENGTH_SHORT).show();
	            }
	            else {
	            	alarmActivated[ALARM_2_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_2_INDEX);
	            	alarm_2_textView.setTextColor(Color.WHITE);
	            	setBackground(alarm_2, getResources().getDrawable(R.drawable.btn_alarm));
	            	Toast.makeText(getApplicationContext(), "Alarme Desativado", Toast.LENGTH_SHORT).show();
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener alarm_3_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(playlist_is_empty()){
		    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
		    		return;
		    	}
	    		if(alarmActivated[ALARM_3_INDEX] == false){
	            	alarmActivated[ALARM_3_INDEX] = true;
	            	myApplication.activateAlarm(ALARM_3_INDEX, alarm_days, alarmHours, alarmMinutes, alarmSnoozes);
	            	alarm_3_textView.setTextColor(getResources().getColor(R.color.blueback));
	            	setBackground(alarm_3, getResources().getDrawable(R.drawable.btn_alarm_checked));
	            	
	            	String hour_string = formatter.format(alarmHours[ALARM_3_INDEX]);
					String minute_string = formatter.format(alarmMinutes[ALARM_3_INDEX]);
					String text = (hour_string + ":" + minute_string);
					Toast.makeText(getApplicationContext(), "Alarme ativado para " + text, Toast.LENGTH_SHORT).show();
	            }
	            else {
	            	alarmActivated[ALARM_3_INDEX] = false;
	            	myApplication.deactivateAlarm(ALARM_3_INDEX);
	            	alarm_3_textView.setTextColor(Color.WHITE);
	            	setBackground(alarm_3, getResources().getDrawable(R.drawable.btn_alarm));
	            	Toast.makeText(getApplicationContext(), "Alarme Desativado", Toast.LENGTH_SHORT).show();
	            }	
	    	}
	  	};
	
	  	View.OnClickListener nap_listener = new View.OnClickListener() {
	    	@Override
	        public void onClick(View arg0) {
	    		if(playlist_is_empty()){
		    		Toast.makeText(getApplicationContext(), "Adicione músicas na Playlist primeiro", Toast.LENGTH_LONG).show();
		    		return;
		    	}
	    		if(alarmActivated[ALARM_NAP_INDEX] == false){
	            	alarmActivated[ALARM_NAP_INDEX] = true;
	            	nap_textView.setTextColor((getResources().getColor(R.color.blueback)));
	            	myApplication.activateNap(nap_time);
	            	Toast.makeText(getApplicationContext(), "Bom Cochilo!", Toast.LENGTH_SHORT).show();
	            	
	            }
	            else {
	            	alarmActivated[ALARM_NAP_INDEX] = false;
	            	nap_textView.setTextColor(Color.WHITE);
	            	myApplication.deactivateNap();
	            	Toast.makeText(getApplicationContext(), "Cochilo Desativado", Toast.LENGTH_SHORT).show();
	            }	
	    	}
	  	};
	  	
	  	View.OnClickListener playlist_listener = new View.OnClickListener() {
		    public void onClick(View v) {
		    	Intent i = new Intent(getApplicationContext(), SongList.class); //para adicionar rintones mudar para SongTab
                startActivity(i);
            }
	  	};
	  	
	  	@SuppressWarnings("deprecation")
		public void setBackground(RelativeLayout layout, Drawable bg){
	  		if (Build.VERSION.SDK_INT >= 16) {

	  		    layout.setBackground(bg);

	  		} else {

	  		    layout.setBackgroundDrawable(bg);
	  		}
	  	}
	  	
        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brand_new_day, menu);
		return true;
	}

        
	public void set_alarm_text(int index) {
			if(index == ALARM_1_INDEX){
				String hour_string = formatter.format(alarmHours[index]);
				String minute_string = formatter.format(alarmMinutes[index]);
				alarm_1_textView.setText(hour_string + ':' + minute_string);
				if(alarmActivated[index] == true){
					alarm_1_textView.setTextColor(getResources().getColor(R.color.blueback));
					setBackground(alarm_1, getResources().getDrawable(R.drawable.btn_alarm_checked));
				}
				else{
					alarm_1_textView.setTextColor(getResources().getColor(R.color.white));
	            	setBackground(alarm_1, getResources().getDrawable(R.drawable.btn_alarm));
				}
			}
			else if(index == ALARM_2_INDEX){
				String hour_string = formatter.format(alarmHours[index]);
				String minute_string = formatter.format(alarmMinutes[index]);
				alarm_2_textView.setText(hour_string + ':' + minute_string);
				if(alarmActivated[index] == true){
					alarm_2_textView.setTextColor(getResources().getColor(R.color.blueback));
					setBackground(alarm_2, getResources().getDrawable(R.drawable.btn_alarm_checked));
				}
				else{
					alarm_2_textView.setTextColor(getResources().getColor(R.color.white));
	            	setBackground(alarm_2, getResources().getDrawable(R.drawable.btn_alarm));
				}
			}
			else if(index == ALARM_3_INDEX){ 
				String hour_string = formatter.format(alarmHours[index]);
				String minute_string = formatter.format(alarmMinutes[index]);
				alarm_3_textView.setText(hour_string + ':' + minute_string);
				if(alarmActivated[index] == true){
					alarm_3_textView.setTextColor(getResources().getColor(R.color.blueback));
					setBackground(alarm_3, getResources().getDrawable(R.drawable.btn_alarm_checked));
				}
				else{
					alarm_3_textView.setTextColor(getResources().getColor(R.color.white));
	            	setBackground(alarm_3, getResources().getDrawable(R.drawable.btn_alarm));
				}
			}
			else if(index == ALARM_NAP_INDEX){
				nap_textView.setText(Integer.toString(nap_time));
				
				if(alarmActivated[ALARM_NAP_INDEX] == false)
					nap_textView.setTextColor(Color.WHITE);
					
				else
					nap_textView.setTextColor((getResources().getColor(R.color.blueback)));
			}
					
	}
	
	public MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	public boolean playlist_is_empty(){
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ArrayList<Uri> audioUris = new ArrayList<Uri>();
		String audioUrisFromPreferences = defaultPreferences.getString("audioUrisFromPreferences", "");
		try {
			audioUris = myApplication.deserialize(audioUrisFromPreferences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(audioUris.size() == 0){
			return true;
		}
		return false;
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
	
	



        
        