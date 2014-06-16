package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	MyApplication myApplication;
	static NumberFormat formatter = new DecimalFormat("00");
	
	RelativeLayout mon;
	RelativeLayout tue;
	RelativeLayout wed;
	RelativeLayout thu;
	RelativeLayout fri;
	RelativeLayout sat;
	RelativeLayout sun;
	ImageView check;
	ImageView inc_hour;
	ImageView dec_hour;
	ImageView inc_d_minute;
	ImageView inc_u_minute;
	ImageView dec_d_minute;
	ImageView dec_u_minute;
	TextView hour;
	TextView d_minute;
	TextView u_minute;
	TextView inc_snooze;
	TextView dec_snooze;
	TextView snooze;
	SeekBar volume_seekBar;
	
	Context context = this;

	protected int index;
	protected int new_hour;
	protected int new_d_minute;
	protected int new_u_minute;
	protected int new_snooze;	
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	public int[] alarmHours, alarmMinutes, alarmSnoozes;
	public float[] volumes;
	public boolean[] alarmActivated;
	
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
		
		mon = (RelativeLayout)findViewById(R.id.mon);
		tue = (RelativeLayout)findViewById(R.id.tue);
		wed = (RelativeLayout)findViewById(R.id.wed);
		thu = (RelativeLayout)findViewById(R.id.thu);
		fri = (RelativeLayout)findViewById(R.id.fri);
		sat = (RelativeLayout)findViewById(R.id.sat);
		sun = (RelativeLayout)findViewById(R.id.sun);
		
		check = (ImageView)findViewById(R.id.check);
		
		inc_hour = (ImageView)findViewById(R.id.increase_hour);
		dec_hour = (ImageView)findViewById(R.id.decrease_hour);
		inc_d_minute = (ImageView)findViewById(R.id.increase_d_minute);
		dec_d_minute = (ImageView)findViewById(R.id.decrease_d_minute);
		inc_u_minute = (ImageView)findViewById(R.id.increase_u_minute);
		dec_u_minute = (ImageView)findViewById(R.id.decrease_u_minute);
		
		hour = (TextView)findViewById(R.id.settings_hour);
		d_minute = (TextView)findViewById(R.id.settings_d_minute);
		u_minute = (TextView)findViewById(R.id.settings_u_minute);
		
		volume_seekBar = (SeekBar)findViewById(R.id.volume_seekBar);
		
		snooze = (TextView)findViewById(R.id.snooze);
		inc_snooze = (TextView)findViewById(R.id.increase_snooze);
		dec_snooze = (TextView)findViewById(R.id.decrease_snooze);
		
		mon.setOnClickListener(day_button_Listener);
		tue.setOnClickListener(day_button_Listener);
		wed.setOnClickListener(day_button_Listener);
		thu.setOnClickListener(day_button_Listener);
		fri.setOnClickListener(day_button_Listener);
		sat.setOnClickListener(day_button_Listener);
		sun.setOnClickListener(day_button_Listener);
		
		inc_hour.setOnClickListener(inc_hour_Listener);
		dec_hour.setOnClickListener(dec_hour_Listener);
		inc_d_minute.setOnClickListener(inc_d_minute_Listener);
		dec_d_minute.setOnClickListener(dec_d_minute_Listener);
		inc_u_minute.setOnClickListener(inc_u_minute_Listener);
		dec_u_minute.setOnClickListener(dec_u_minute_Listener);
		inc_snooze.setOnClickListener(inc_snooze_Listener);
		dec_snooze.setOnClickListener(dec_snooze_Listener);
		check.setOnClickListener(check_Listener);
		
		new_hour = alarmHours[index];
		new_d_minute = alarmMinutes[index]/10;
		new_u_minute = alarmMinutes[index]%10;
		new_snooze = alarmSnoozes[index];
		
		/* UPDATE TEXT VALUES */
		hour.setText(formatter.format(new_hour));
		d_minute.setText(Integer.toString(new_d_minute));
		u_minute.setText(Integer.toString(new_u_minute));
		snooze.setText(Integer.toString(new_snooze));
			
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
	View.OnClickListener inc_hour_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_hour == 23)
	    		new_hour = 0;
	    	else
	    		new_hour += 1;
	    	hour.setText(formatter.format(new_hour));
	    }
	};
	View.OnClickListener dec_hour_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_hour == 0)
	    		new_hour = 23;
	    	else
	    		new_hour -= 1;
	    	hour.setText(formatter.format(new_hour));
	    }
	};
	View.OnClickListener inc_d_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_d_minute == 5)
	    		new_d_minute = 0;
	    	else
	    		new_d_minute += 1;
	    	d_minute.setText(Integer.toString(new_d_minute));
	    	
	    }
	};
	View.OnClickListener dec_d_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_d_minute == 0)
	    		new_d_minute = 5;
	    	else
	    		new_d_minute -= 1;
	    	d_minute.setText(Integer.toString(new_d_minute));
	    }
	};
	View.OnClickListener inc_u_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_u_minute == 9)
	    		new_u_minute = 0;
	    	else
	    		new_u_minute += 1;
	    	u_minute.setText(Integer.toString(new_u_minute));
	    }
	};
	View.OnClickListener dec_u_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_u_minute == 0)
	    		new_u_minute = 9;
	    	else
	    		new_u_minute -= 1;
	    	u_minute.setText(Integer.toString(new_u_minute));
	    }
	};
	View.OnClickListener dec_snooze_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_snooze > 1)
	    		new_snooze -= 2;
	    	snooze.setText(Integer.toString(new_snooze));	    	
	    }
	};
	View.OnClickListener inc_snooze_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_snooze < 29)
	    		new_snooze += 2;
	    	snooze.setText(Integer.toString(new_snooze));	    	
	    }
	};
	View.OnClickListener day_button_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	v = (RelativeLayout)v;
	    	if(v.isSelected() == true){
	    		v.setBackground(getResources().getDrawable(R.drawable.btn_inactive_small));
	    		v.setSelected(false);
	    	}
	    	else{
	    		v.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
	    		v.setSelected(true);
	    	}		
	    }
	};
	View.OnClickListener check_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	alarmHours[index] = new_hour;
			alarmMinutes[index] = new_d_minute*10 + new_u_minute;
			alarmSnoozes[index] = new_snooze;
			alarmActivated[index] = true;
			
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
			
			editor.commit();
			
			
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			finish();
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
