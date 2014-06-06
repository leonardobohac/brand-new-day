package com.example.brandnewday;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	MyApplication myApplication;
	
	public int[] alarmHours, alarmMinutes, alarmSnoozes;
	public float[] volumes;
	public boolean[] alarmActivated;
	
	ImageView mon;
	ImageView tue;
	ImageView wed;
	ImageView thu;
	ImageView fri;
	ImageView sat;
	ImageView sun;
	ImageView inc_hour;
	ImageView dec_hour;
	ImageView inc_minute;
	ImageView dec_minute;
	
	TextView hour;
	TextView minute;
	TextView inc_snooze;
	TextView dec_snooze;
	TextView snooze;
	
	SeekBar volume_seekBar;
	
	Context context = this;

	protected int index;
	protected int new_hour;
	protected int new_minute;
	protected int new_snooze;	
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_settings);
		
		myApplication = getMyApplication();
		alarmHours = new int[3]; //myApplication.getAlarmHours();
		alarmMinutes = new int[3]; //myApplication.getAlarmMinutes();
		alarmSnoozes = new int[3]; //myApplication.getAlarmSnoozes();
		alarmActivated = new boolean[4]; //myApplication.getAlarmActivated();
		volumes = new float[4];
		
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmHours[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Hour", 9);
		alarmMinutes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Minute", 15);
		alarmMinutes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Minute", 45);
		alarmSnoozes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = defaultPreferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = defaultPreferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = defaultPreferences.getBoolean("alarm3Activated", false);
		
		inc_hour = (ImageView)findViewById(R.id.increase_hour);
		dec_hour = (ImageView)findViewById(R.id.decrease_hour);
		inc_minute = (ImageView)findViewById(R.id.increase_minute);
		dec_minute = (ImageView)findViewById(R.id.decrease_minute);
		
		hour = (TextView)findViewById(R.id.set_hour);
		hour.setText(Integer.toString(alarmHours[index]));
		
		minute = (TextView)findViewById(R.id.set_minute);
		hour.setText(Integer.toString(alarmMinutes[index]));
		
		inc_snooze = (TextView)findViewById(R.id.increase_snooze);
		dec_snooze = (TextView)findViewById(R.id.decrease_snooze);
		
		snooze = (TextView)findViewById(R.id.snooze);
		snooze.setText(Integer.toString(alarmSnoozes[index]));
		
		volume_seekBar = (SeekBar)findViewById(R.id.volume_seekBar);
		
		
		mon = (ImageView)findViewById(R.id.monday);
		mon.setOnClickListener(dayButtonListener);
		tue = (ImageView)findViewById(R.id.tuesday);
		tue.setOnClickListener(dayButtonListener);
		wed = (ImageView)findViewById(R.id.wednesday);
		wed.setOnClickListener(dayButtonListener);
		thu = (ImageView)findViewById(R.id.thursday);
		thu.setOnClickListener(dayButtonListener);
		fri = (ImageView)findViewById(R.id.friday);
		fri.setOnClickListener(dayButtonListener);
		sat = (ImageView)findViewById(R.id.saturday);
		sat.setOnClickListener(dayButtonListener);
		sun = (ImageView)findViewById(R.id.sunday);
		sun.setOnClickListener(dayButtonListener);
		
		
		
		
			
	}

	View.OnClickListener dayButtonListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	ImageView day =(ImageView)v;
	    	if(day.isSelected()){
	    		day.setImageResource(R.drawable.btn_inactive1);
	    		day.setSelected(false);
	    	}
	    	else{
	    		day.setImageResource(R.drawable.btn_active1);
	    		day.setSelected(true);
	    	}
	    }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
}
