package com.example.brandnewday;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmSettings extends Activity {
	MyApplication myApplication;
	BrandNewDay mainActivity;

	public int[] alarmHours, alarmMinutes, alarmSnoozes, volumes; 
	public boolean[] alarmActivated;
	
	ArrayList<String> myStringPlaylist;

	TimePicker timePicker;
	SeekBar seekBar;
	TextView snoozeTimeTextView;
	Button setAlarmButton;
	Button backButton;
	CheckBox snoozeDisabledCheckBox;
	RadioButton volumeCrescentRadioButton;
	RadioButton volumeLowRadioButton;
	RadioButton volumeMediumRadioButton;
	RadioButton volumeHighRadioButton;
	
	Context context = this;

	protected int index;
	protected int new_hour;
	protected int new_minute;
	protected int new_snooze;	
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	
	int volumeCrescent = 0;
	int volumeLow = 1;
	int volumeMedium = 2;
	int volumeHigh = 3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm_settings);
		myApplication = getMyApplication();
		alarmHours = new int[3]; //myApplication.getAlarmHours();
		alarmMinutes = new int[3]; //myApplication.getAlarmMinutes();
		alarmSnoozes = new int[3]; //myApplication.getAlarmSnoozes();
		alarmActivated = new boolean[4]; //myApplication.getAlarmActivated();
		volumes = new int[4];
		

		
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
		volumes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Volume", volumeCrescent);
		volumes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Volume", volumeCrescent);
		volumes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Volume", volumeCrescent);
		volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeCrescent);
		

		timePicker = (TimePicker) findViewById(R.id.time_picker);
		seekBar = (SeekBar) findViewById(R.id.snooze_bar);
		snoozeTimeTextView = (TextView) findViewById(R.id.snooze_time_textView);
		snoozeDisabledCheckBox = (CheckBox) findViewById(R.id.snooze_disabled_checkBox);
		setAlarmButton = (Button) findViewById(R.id.set_alarm_button);
		
		volumeCrescentRadioButton = (RadioButton) findViewById(R.id.volumeCrescentRadioButton);
		volumeLowRadioButton = (RadioButton) findViewById(R.id.volumeLowRadioButton);
		volumeMediumRadioButton = (RadioButton) findViewById(R.id.volumeMediumRadioButton);
		volumeHighRadioButton = (RadioButton) findViewById(R.id.volumeHighRadioButton);
		backButton = (Button) findViewById(R.id.back_button);
		
		setAlarmButton.setOnClickListener(setAlarmListener);
		backButton.setOnClickListener(backListener);
		snoozeDisabledCheckBox.setOnClickListener(snoozeDisabledCheckBoxListener);
		seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		volumeCrescentRadioButton.setOnClickListener(volumeCrescentRadioButtonListener);
		volumeLowRadioButton.setOnClickListener(volumeLowRadioButtonListener);
		volumeMediumRadioButton.setOnClickListener(volumeMediumRadioButtonListener);
		volumeHighRadioButton.setOnClickListener(volumeHighRadioButtonListener);

		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");

		timePicker.setCurrentHour(alarmHours[index]);
		
		timePicker.setCurrentMinute(alarmMinutes[index]);
		adjustSeekBar(seekBar, index);
		adjustSnoozeDisabledCheckBox(snoozeDisabledCheckBox, index);
		
		if(volumes[index] == volumeCrescent){
			volumeCrescentRadioButton.toggle();
		}
		else if(volumes[index] == volumeLow){
			volumeLowRadioButton.toggle();
		}
		else if(volumes[index] == volumeMedium){
			volumeMediumRadioButton.toggle();
		}
		else if(volumes[index] == volumeHigh){
			volumeHighRadioButton.toggle();
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		timePicker.setCurrentHour(alarmHours[index]);
		timePicker.setCurrentMinute(alarmMinutes[index]);
		timePicker.setCurrentHour(alarmHours[index]);
		timePicker.setCurrentMinute(alarmMinutes[index]);
		adjustSeekBar(seekBar, index);
		adjustSnoozeDisabledCheckBox(snoozeDisabledCheckBox, index);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
		
	View.OnClickListener setAlarmListener = new View.OnClickListener() {
		public void onClick(View v) {
			new_hour = timePicker.getCurrentHour();
			new_minute = timePicker.getCurrentMinute();

			if (snoozeDisabledCheckBox.isChecked()) 
				new_snooze = 0;
			 else 
				new_snooze = seekBar.getProgress() + 3;
			
			alarmHours[index] = new_hour;
			alarmMinutes[index] = new_minute;
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
			editor.putInt("alarm1Volume", volumes[ALARM_1_INDEX]);
			editor.putInt("alarm2Volume", volumes[ALARM_2_INDEX]);
			editor.putInt("alarm3Volume", volumes[ALARM_3_INDEX]);
			editor.putInt("alarmNapVolume", volumes[ALARM_NAP_INDEX]);
			
			editor.commit();
			
			
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			finish();
		}
	};

	View.OnClickListener backListener = new View.OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	View.OnClickListener snoozeDisabledCheckBoxListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (snoozeDisabledCheckBox.isChecked()) {
				alarmSnoozes[index] = 0;
				seekBar.setVisibility(View.GONE);
				snoozeTimeTextView.setVisibility(View.GONE);
			} 
			else {
				seekBar.setVisibility(View.VISIBLE);
				snoozeTimeTextView.setVisibility(View.VISIBLE);
			}
		}
	};
	
	View.OnClickListener volumeCrescentRadioButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(volumeCrescentRadioButton.isChecked()) {
				volumeLowRadioButton.setChecked(false);
				volumeMediumRadioButton.setChecked(false);
				volumeHighRadioButton.setChecked(false);
			}
			volumes[index] = volumeCrescent;
			
		}
	};
	View.OnClickListener volumeLowRadioButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(volumeLowRadioButton.isChecked()) {
				volumeCrescentRadioButton.setChecked(false);
				volumeMediumRadioButton.setChecked(false);
				volumeHighRadioButton.setChecked(false);
			}
			volumes[index] = volumeLow;
		}
	};
	View.OnClickListener volumeMediumRadioButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(volumeMediumRadioButton.isChecked()) {
				volumeLowRadioButton.setChecked(false);
				volumeCrescentRadioButton.setChecked(false);
				volumeHighRadioButton.setChecked(false);
			}
			volumes[index] = volumeMedium;
		}
	};
	View.OnClickListener volumeHighRadioButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(volumeHighRadioButton.isChecked()) {
				volumeLowRadioButton.setChecked(false);
				volumeMediumRadioButton.setChecked(false);
				volumeCrescentRadioButton.setChecked(false);
			}
			volumes[index] = volumeHigh;
			
		}
	};

	SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			int snooze = 3 + progress;
			snoozeTimeTextView.setText(Integer.toString(snooze) + " minutes");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};
	
	
	
	public void adjustSeekBar(SeekBar seekBar, int index) {
			if(alarmSnoozes[index] == 0){
				seekBar.setVisibility(View.GONE);
				snoozeTimeTextView.setVisibility(View.GONE);
			}
			else
				seekBar.setProgress(alarmSnoozes[index] - 3);
	}
	
	public void adjustSnoozeDisabledCheckBox(CheckBox snoozeDisabledCheckBox, int index) {
		if(alarmSnoozes[index] == 0) 
			snoozeDisabledCheckBox.setChecked(true);
		else
			snoozeDisabledCheckBox.setChecked(false);
	}
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	
}

	
	
	
		
