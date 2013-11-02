package com.example.brandnewday;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class WakingTime extends Activity{
	MyApplication myApplication;
	
	SharedPreferences preferences;
	int index;
	int snooze;
	int[] alarmSnoozes;
	int [] alarmHours;
	int [] alarmMinutes;
	int currentTrack = 0;
	
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	private MediaPlayer mediaPlayer;
	PendingIntent pendingIntent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waking_time);
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		snooze = intent.getExtras().getInt("snooze");

		
		myApplication = getMyApplication();
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		
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
	
		Button wakeButton = (Button)findViewById(R.id.wake_button);
		Button snoozeButton = (Button)findViewById(R.id.snooze_button);
		
		wakeButton.setOnClickListener(wakeButtonOnClickListener);
		snoozeButton.setOnClickListener(snoozeButtonOnClickListener);
		
		if(snooze == 0)
			snoozeButton.setVisibility(View.GONE);
		else
			snoozeButton.setVisibility(View.VISIBLE);		
	}

	@Override
	protected void onStop() {
		super.onStop();	
		if(mediaPlayer != null)
			mediaPlayer.release();
		PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.setReferenceCounted(false); //any release() can set wakeLock off
		wakeLock.release();
		Log.d("wakelock", "wake lock released");
	}

	View.OnClickListener wakeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			
			Intent intent = new Intent(getApplicationContext(), BrandNewDay.class);
			startActivity(intent);
	        finish();
        }			
	};
	
	View.OnClickListener snoozeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateSnooze(index, snooze);
			Intent intent = new Intent(getApplicationContext(), BrandNewDay.class);
			startActivity(intent);
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waking_time, menu);
		return true;
	}
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
}






