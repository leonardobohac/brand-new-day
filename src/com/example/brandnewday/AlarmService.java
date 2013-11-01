package com.example.brandnewday;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.pig.impl.util.ObjectSerializer;

import android.app.Activity;
import android.app.Service;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



public class AlarmService extends Service implements MediaPlayer.OnCompletionListener {
	MyApplication myApplication;
	MediaPlayer mediaPlayer = null;
	Set<String> emptySet = new HashSet<String>();
	ArrayList<Uri> randomizedAudioUris;
	ArrayList<Uri> audioUris;
	ArrayList<String> currentStrings;
	int currentTrack = 0;
	String audioArrayInString;
	
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
		if(mediaPlayer != null)
			mediaPlayer.release();
	}

	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
		
		myApplication = getMyApplication();
		this.audioUris = myApplication.getAudioUris();
		if(this.audioUris.size() == 0) {
			SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String audioStringFromPreferences = defaultPreferences.getString("audioStringFromPreferences", "");
			try {
				this.audioUris = myApplication.deserialize(audioStringFromPreferences);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
		randomizedAudioUris = new ArrayList<Uri>(this.audioUris.size());
		randomizedAudioUris = randomizeUriArrayList(this.audioUris);
		if(this.randomizedAudioUris.size() != 0){
			 mediaPlayer = MediaPlayer.create(getApplicationContext(), randomizedAudioUris.get(currentTrack));
		     mediaPlayer.setOnCompletionListener(this);
		     mediaPlayer.start();
		}
		
		
		int index = intent.getExtras().getInt("index");
		int snooze = intent.getExtras().getInt("snooze");
		Intent i = new Intent(getApplicationContext(), WakingTime.class);
		i.putExtra("index", index);
		i.putExtra("snooze", snooze);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY;
	}

	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();	
		return super.onUnbind(intent);
	
	}
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	public void onCompletion(MediaPlayer arg0) {
	      arg0.release();
	      if(this.randomizedAudioUris == null)
	    	  System.out.println("NULL playlist");
	      else {
		      if (currentTrack < this.randomizedAudioUris.size()) {
		        currentTrack++;
		        arg0 = MediaPlayer.create(getApplicationContext(), randomizedAudioUris.get(currentTrack));
		        arg0.setOnCompletionListener(this);
		        arg0.start();
		      }
	      }
	}
	public ArrayList<Uri> randomizeUriArrayList(ArrayList<Uri> uriArrayList) {
		// Shuffles and array of URIs 
		ArrayList<Uri> randomizedArray = new ArrayList<Uri>(uriArrayList.size());
		ArrayList<Integer> randomIndexArray = generateRandomIndexList(uriArrayList.size());
		
		for(int i=0; i < randomIndexArray.size(); i += 1)
			randomizedArray.add(uriArrayList.get(randomIndexArray.get(i)));
		
		return randomizedArray;
	}
	
	public ArrayList<Integer> generateRandomIndexList(int size) {
		// List of distinct random integers between 0 and size. Ex: [2,5,1,4,3], with size = 5 ///
		Random generator = new Random();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<Integer> randomIndexList = new ArrayList<Integer>();
		
		for(int i=0; i < size; i += 1) {
			indexList.add(i);
		}
		for(int j=0; j < size; j += 1) {
			int randomIndex = generator.nextInt(indexList.size());
			randomIndexList.add(indexList.get(randomIndex));
			indexList.remove(indexList.get(randomIndex));
		}
		
		return randomIndexList;
	}
	
	/*public void getAudioUrisInStringPreferences() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.audioUrisInStringSet = preferences.getStringSet("AudioUrisInStringSet", emptySet);
	}*/
}


