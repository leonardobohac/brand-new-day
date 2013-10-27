package com.example.brandnewday;

import android.app.Service;
import android.os.IBinder;
import android.widget.Toast;
import android.content.Intent;



public class AlarmService extends Service {
	
	@Override
	public void onCreate() {
	    super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
	//TODO for communication return IBinder implementation
		return null;
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		/*int index = intent.getExtras().getInt("index");
		int hour = intent.getExtras().getInt("hour");
		int minute = intent.getExtras().getInt("minute");
		int snoozeTime = intent.getExtras().getInt("snooze");*/

		
		/*i.putExtra("index", index);
		i.putExtra("hour", hour);
		i.putExtra("minute", minute);
		i.putExtra("snoozeTime", snoozeTime);*/
		int index = intent.getExtras().getInt("index");
		int snooze = intent.getExtras().getInt("snooze");
		Intent i = new Intent(getApplicationContext(), WakingTime.class);
		i.putExtra("index", index);
		i.putExtra("snooze", snooze);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		startActivity(i);
		return START_NOT_STICKY;
	}
		
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();	
		return super.onUnbind(intent);
	
	}
}


