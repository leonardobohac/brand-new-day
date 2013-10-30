package com.example.brandnewday;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
 
public class AlarmReceiver extends BroadcastReceiver
{
	private static PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent)
    {
    	
    	int index = intent.getExtras().getInt("index");
    	int snooze = intent.getExtras().getInt("snooze");
    	Intent service = new Intent(context, AlarmService.class);
    	service.putExtra("index", index);
    	service.putExtra("snooze", snooze);
    	
        context.startService(service);
        
    }
}
    
   

    

 