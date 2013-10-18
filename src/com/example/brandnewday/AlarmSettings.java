package com.example.brandnewday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSettings extends Activity{
	
	TimePicker timePicker;
	SeekBar seekBar;
	TextView snoozeTimeTextView;
	Button setAlarmButton;
	Button backButton;
	CheckBox snoozeDisabledCheckBox;
	Context context = this;
	
	Intent intent;
	
	int index;
	int isActive;
	int hour;
	int minute;
	int snoozeTime;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_settings);
		// Capture our buttons from layout
	    timePicker = (TimePicker)findViewById(R.id.time_picker);
	    seekBar = (SeekBar)findViewById(R.id.snooze_bar);
	    snoozeTimeTextView = (TextView)findViewById(R.id.snooze_time_textView);
	    setAlarmButton = (Button)findViewById(R.id.set_alarm_button);
	    backButton = (Button)findViewById(R.id.back_button);
	    snoozeDisabledCheckBox = (CheckBox)findViewById(R.id.snooze_disabled_checkBox);
		
		setAlarmButton.setOnClickListener(setAlarmListener);
		backButton.setOnClickListener(backListener);
		snoozeDisabledCheckBox.setOnClickListener(snoozeDisabledCheckBoxListener);
		seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		
		intent = getIntent();
		
		index = intent.getExtras().getInt("index");
		isActive = intent.getExtras().getInt("isActive");
		hour = intent.getExtras().getInt("hour");
		minute = intent.getExtras().getInt("minute");
		snoozeTime = intent.getExtras().getInt("snoozeTime");
		
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		

		if(snoozeTime == 0) {
			snoozeDisabledCheckBox.setChecked(true);
			seekBar.setProgress(snoozeTime-3);
			seekBar.setVisibility(View.GONE);
			snoozeTimeTextView.setVisibility(View.GONE);
		}
		else seekBar.setProgress(snoozeTime-3);
	}
		
	
	
	
		
	View.OnClickListener setAlarmListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			if(snoozeDisabledCheckBox.isChecked()) {
				snoozeTime = 0;
			}
			else {
				snoozeTime = seekBar.getProgress() + 3;
			}
			
			hour = timePicker.getCurrentHour();
			minute = timePicker.getCurrentMinute();	

			intent.putExtra("hour", hour);
			intent.putExtra("minute", minute);
			intent.putExtra("snoozeTime", snoozeTime);
			String snooze = new Integer(snoozeTime).toString();
			Toast.makeText(getApplicationContext(), snooze, 3).show();
			setResult(RESULT_OK, intent);
			
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
			if(snoozeDisabledCheckBox.isChecked()) {
				seekBar.setVisibility(View.GONE);
				snoozeTimeTextView.setVisibility(View.GONE);
			}
			else {
				seekBar.setVisibility(View.VISIBLE);
				seekBar.setEnabled(true);
				snoozeTimeTextView.setVisibility(View.VISIBLE);
			}
		}
	};
	
	SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){ 

	   @Override 
	   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		   snoozeTime = 3 + progress;
		   snoozeTimeTextView.setText(String.valueOf(snoozeTime) + " minutes");
		   
	   } 

	   @Override 
	   public void onStartTrackingTouch(SeekBar seekBar) {
		   
	   } 

	   @Override 
	   public void onStopTrackingTouch(SeekBar seekBar) { 

	   }
	   
	};
	
}
		
		



		   

