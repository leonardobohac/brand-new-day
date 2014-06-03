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
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



public class AlarmService extends Service implements MediaPlayer.OnCompletionListener {
	MyApplication myApplication;
	MediaPlayer mediaPlayer = null;
	int[] volumes = new int[4];
	Set<String> emptySet = new HashSet<String>();
	ArrayList<Uri> randomAudioUris;    //keeps only the not yet-played tracks
	ArrayList<Uri> audioUris;
	ArrayList<String> currentStrings;
	int firstTrack = 0;
	String audioArrayInString;
	int index;
	int snooze; 
	WakeLock wakeLock;
	float mediaPlayerDefaultVolume = 1.0f;
	int volumeCounter = 1;
	int volumeCode;
	Timer volumeRaiserTimer = new Timer();
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final int ALARM_NAP_INDEX = 3;
	int volumeCrescent = 0;
	int volumeLow = 1;
	int volumeMedium = 2;
	int volumeHigh = 3;
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	TimerTask raiseVolumeTask = new TimerTask() {
        public void run() {
        	raiseVolume();
            }
        };
        
    public void raiseVolume() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	}

	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		myApplication = getMyApplication();
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = defaultPreferences.edit();
		volumes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Volume", volumeMedium);
		volumes[ALARM_NAP_INDEX] = defaultPreferences.getInt("alarmNapVolume", volumeMedium);
		
		PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		
		wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.setReferenceCounted(false); //any release() can set wakeLock off
		wakeLock.acquire();
		
		index = intent.getExtras().getInt("index");
		snooze = intent.getExtras().getInt("snooze");
		
		String audioUrisFromPreferences = defaultPreferences.getString("audioUrisFromPreferences", "");
		try {
			audioUris = myApplication.deserialize(audioUrisFromPreferences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String randomAudioUrisFromPreferences = defaultPreferences.getString("randomAudioUrisFromPreferences", "");
		try {
			randomAudioUris = myApplication.deserialize(randomAudioUrisFromPreferences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(audioUris.size()!=0){
			if(randomAudioUris.size() == 0){
				//No more unplayed tracks to play
				randomAudioUris = new ArrayList<Uri>(audioUris.size());
				randomAudioUris = randomizeUriArrayList(audioUris);
				mediaPlayer = MediaPlayer.create(getApplicationContext(), randomAudioUris.get(firstTrack));
				randomAudioUris.remove(firstTrack); 
				try {
					editor.putString("randomAudioUrisFromPreferences", myApplication.serialize(randomAudioUris));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else{
				mediaPlayer = MediaPlayer.create(getApplicationContext(), randomAudioUris.get(firstTrack));

				randomAudioUris.remove(firstTrack);  //remove track so it's not going to play again until the playlist ends
				try {
					editor.putString("randomAudioUrisFromPreferences", myApplication.serialize(randomAudioUris));
				} catch (Exception e) {
					e.printStackTrace();
				}
				editor.commit();
			}
			mediaPlayer.setOnCompletionListener(this);

			
		    if(volumes[index] == volumeCrescent){
			    mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
				volumeRaiserTimer.schedule(raiseVolumeTask, 4500, 4500);
			}
			else if(volumes[index] == volumeLow){
			    mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
			}
			else if(volumes[index] == volumeMedium){
			    mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
			}
			else if(volumes[index] == volumeHigh){
			    mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,  audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			}
		    
			mediaPlayer.start();
		    
		}
		
		Intent i = new Intent(getApplicationContext(), WakingTime.class);
		i.putExtra("index", index);
		i.putExtra("snooze", snooze);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY;
	}
	
	public void onCompletion(MediaPlayer mediaPlayer) {
	    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mediaPlayer.release();
		if(randomAudioUris == null){
			randomAudioUris = new ArrayList<Uri>(audioUris.size());
			randomAudioUris = randomizeUriArrayList(audioUris);
			mediaPlayer = MediaPlayer.create(getApplicationContext(), randomAudioUris.get(firstTrack));
			randomAudioUris.remove(firstTrack);
		}
		else {
		    mediaPlayer = MediaPlayer.create(getApplicationContext(), randomAudioUris.get(firstTrack));
		    randomAudioUris.remove(firstTrack);
		    //mediaPlayer.setOnCompletionListener(this);
		    if(volumes[index] == volumeCrescent){
		        mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
			    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
			    volumeRaiserTimer.schedule(raiseVolumeTask, 3000, 3000);
		    }
		    else if(volumes[index] == volumeLow){
		    	mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
		    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
		    }
		    else if(volumes[index] == volumeMedium){
		    	mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
		    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
		    }
		    else if(volumes[index] == volumeHigh){
		    	mediaPlayer.setVolume(mediaPlayerDefaultVolume, mediaPlayerDefaultVolume);
		    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,  audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		    }
		    mediaPlayer.start();
		}
      }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mediaPlayer != null)
			mediaPlayer.release();
		wakeLock.release();
		volumeRaiserTimer.cancel();
	}
	
	public ArrayList<Uri> randomizeUriArrayList(ArrayList<Uri> uriArrayList) {
		// Shuffles an array of URIs 
		ArrayList<Uri> randomizedArray = new ArrayList<Uri>(uriArrayList.size());
		ArrayList<Integer> randomIndexArray = generateRandomIndexList(uriArrayList.size());
		
		for(int i=0; i < randomIndexArray.size(); i += 1)
			randomizedArray.add(uriArrayList.get(randomIndexArray.get(i)));
		
		return randomizedArray;
	}
	
	public ArrayList<Integer> generateRandomIndexList(int size) {
		// List of distinct random integers between 1 and size. Ex: [2,5,1,4,3], with size = 5 ///
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