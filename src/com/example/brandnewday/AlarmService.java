package com.example.brandnewday;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
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
	int index;
	int snooze; 
	WakeLock wakeLock;
	float currentVolume = 0.01f;
	public int volumeCounter = 1;
	Timer volumeRaiserTimer = new Timer();
	

	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	TimerTask raiseVolumeTask = new TimerTask() {
        public void run() {
        	raiseVolume();
            }
        };

	
		
      

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(mediaPlayer != null)
			mediaPlayer.release();
		wakeLock.release();
		
		
		volumeRaiserTimer.cancel();
		
	}

	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		myApplication = getMyApplication();
		
		
		//audioUris = myApplication.getAudioUris();
		
		
		PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		volumeRaiserTimer.schedule(raiseVolumeTask, 70000, 70000);
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		index = intent.getExtras().getInt("index");
		snooze = intent.getExtras().getInt("snooze");
		

		wakeLock.setReferenceCounted(false); //any release() can set wakeLock off
		wakeLock.acquire();
		
		
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String audioStringFromPreferences = defaultPreferences.getString("audioStringFromPreferences", "");
		try {
			audioUris = myApplication.deserialize(audioStringFromPreferences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 
		randomizedAudioUris = new ArrayList<Uri>(audioUris.size());
		randomizedAudioUris = randomizeUriArrayList(audioUris);
		if(randomizedAudioUris.size() != 0){
			 mediaPlayer = MediaPlayer.create(getApplicationContext(), randomizedAudioUris.get(currentTrack));
		     mediaPlayer.setOnCompletionListener(this);
		     mediaPlayer.start();
		}
		else
			Log.d("No songs", "No songs");

		
		Intent i = new Intent(getApplicationContext(), WakingTime.class);
		i.putExtra("index", index);
		i.putExtra("snooze", snooze);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY;
		
		
	}
	
	public void onCompletion(MediaPlayer arg0) {
	      arg0.release();
	      /*volumeCounter = 1;
	      currentVolume = 0.04f;*/
	      if(randomizedAudioUris == null)
	    	  System.out.println("NULL playlist");
	      else {
		      if (currentTrack < randomizedAudioUris.size()-1) {
		        currentTrack++;
			        arg0 = MediaPlayer.create(getApplicationContext(), randomizedAudioUris.get(currentTrack));
			        arg0.setOnCompletionListener(this);
			        //arg0.setVolume(currentVolume, currentVolume);
			        arg0.start();

		      }
	      }
	}
	
	
	
	public void raiseVolume() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
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
	
	@Override
	public void onCreate() {
		super.onCreate();	
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();	
		return super.onUnbind(intent);
	
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}