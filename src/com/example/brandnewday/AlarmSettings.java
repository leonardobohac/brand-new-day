package com.example.brandnewday;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmSettings extends Activity {
	MyApplication myApplication;
	BrandNewDay mainActivity;

	public int[] alarmHours, alarmMinutes, alarmSnoozes;
	public boolean[] alarmActivated;
	ArrayList<String> myStringPlaylist;

	TimePicker timePicker;
	SeekBar seekBar;
	TextView snoozeTimeTextView;
	Button setAlarmButton;
	Button backButton;
	CheckBox snoozeDisabledCheckBox;
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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_settings);
		myApplication = getMyApplication();
		
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		alarmActivated = myApplication.getAlarmActivated();

		timePicker = (TimePicker) findViewById(R.id.time_picker);
		seekBar = (SeekBar) findViewById(R.id.snooze_bar);
		snoozeTimeTextView = (TextView) findViewById(R.id.snooze_time_textView);
		setAlarmButton = (Button) findViewById(R.id.set_alarm_button);
		backButton = (Button) findViewById(R.id.back_button);
		snoozeDisabledCheckBox = (CheckBox) findViewById(R.id.snooze_disabled_checkBox);

		setAlarmButton.setOnClickListener(setAlarmListener);
		backButton.setOnClickListener(backListener);
		snoozeDisabledCheckBox.setOnClickListener(snoozeDisabledCheckBoxListener);
		seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);

		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");

		timePicker.setCurrentHour(alarmHours[index]);
		timePicker.setCurrentMinute(alarmMinutes[index]);
		adjustSeekBar(seekBar, index);
		adjustSnoozeDisabledCheckBox(snoozeDisabledCheckBox, index);
		
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
			
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(String.format("alarm%dHour",index+1), alarmHours[index]);
			editor.putInt(String.format("alarm%dMinute",index+1), alarmMinutes[index]);
			editor.putInt(String.format("alarm%dSnooze",index+1), alarmSnoozes[index]);
			editor.putBoolean(String.format("alarm%dActivated",index+1), alarmActivated[index]);
		
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

	
	
	
		
