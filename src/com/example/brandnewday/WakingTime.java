package com.example.brandnewday;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WakingTime extends Activity {

	private PendingIntent pendingIntent;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waking_time);
		Button wakeButton = (Button)findViewById(R.id.wake_button);
		Button snoozeButton = (Button)findViewById(R.id.snooze_button);
		
		wakeButton.setOnClickListener(wakeButtonOnClickListener);
		snoozeButton.setOnClickListener(snoozeButtonOnClickListener);
		
		intent = getIntent();
		int snoozeTime = intent.getExtras().getInt("snoozeTime");

		
		if(snoozeTime == 0) {
			snoozeButton.setVisibility(View.GONE);
			snoozeButton.setClickable(false);
			
		}
	}
		
	
	
	View.OnClickListener wakeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = getIntent();
			int hour = intent.getExtras().getInt("hour");
			int minute = intent.getExtras().getInt("minute");
			
			
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			pendingIntent = PendingIntent.getService(WakingTime.this, 0, i, 0);
			

			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND,0);

	        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	        finish();
        }
			
		
	};
	
	View.OnClickListener snoozeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = getIntent();
			int snoozeTime = intent.getExtras().getInt("snoozeTime");
			
			String snooze = new Integer(snoozeTime).toString();
			Toast.makeText(getApplicationContext(), snooze, 3).show();
			
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			pendingIntent = PendingIntent.getService(WakingTime.this, 0, i, 0);
			

			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.MINUTE, snoozeTime);
			

	        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	        finish();
		}
	};
	
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waking_time, menu);
		return true;
	}
}
	

	

 

