package com.example.brandnewday;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class AlarmReceiver extends BroadcastReceiver
{
      
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