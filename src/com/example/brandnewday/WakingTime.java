package com.example.brandnewday;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class WakingTime extends Activity implements MediaPlayer.OnCompletionListener{
	MyApplication myApplication;
	SharedPreferences preferences;
	private Set<String> audioUrisInStringSet;
	
	private ArrayList<Uri> randomizedAudioUris;
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
	private ArrayList<Uri> audioUris;
	private MediaPlayer mediaPlayer = null;
	PendingIntent pendingIntent;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
		setContentView(R.layout.waking_time);
		preferences = getPreferences(MODE_PRIVATE);
	
		myApplication = getMyApplication();
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		audioUrisInStringSet = myApplication.getAudioUrisInStringSet();
		
		if(preferences.getStringSet("AudioUrisInStringSet", null) != null) {
			System.out.println("not null set");
			getAudioUrisInStringPreferences();
			
			for (Iterator<String> iterator = audioUrisInStringSet.iterator(); iterator.hasNext(); ) {
		        String uriString = iterator.next();
		        System.out.println(uriString);
		        audioUris.add(Uri.parse(uriString));
		    }
			
			
			  
			
			randomizedAudioUris = new ArrayList<Uri>(audioUris.size());
			randomizedAudioUris = randomizeUriArrayList(audioUris);
			System.out.println(randomizedAudioUris.toString());
			if(this.randomizedAudioUris.size() != 0){
				 mediaPlayer = MediaPlayer.create(getApplicationContext(), this.randomizedAudioUris.get(currentTrack));
			     mediaPlayer.setOnCompletionListener(this);
			     mediaPlayer.start();
			}
		}
	
		Button wakeButton = (Button)findViewById(R.id.wake_button);
		Button snoozeButton = (Button)findViewById(R.id.snooze_button);
		
		wakeButton.setOnClickListener(wakeButtonOnClickListener);
		snoozeButton.setOnClickListener(snoozeButtonOnClickListener);
		
		snooze = alarmSnoozes[index];
		if(snooze == 0)
			snoozeButton.setVisibility(View.GONE);
		else
			snoozeButton.setVisibility(View.VISIBLE);
	
		
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
	
	
	public void getAudioUrisInStringPreferences() {
		SharedPreferences preferences = getSharedPreferences("AudioUrisInStringPreferences",MODE_PRIVATE);
		audioUrisInStringSet = preferences.getStringSet("AudioUrisInStringSet", null);
	}
		
	/*@Override
	protected void onPause() {
		super.onPause();
		mediaPlayer.release();
		finish();
	}*/
	
	@Override
	protected void onStop() {
		super.onStop();
		//releaseWakeLock();
		
		//mediaPlayer.reset();
		if(mediaPlayer != null)
			mediaPlayer.release();
	}
	
	protected void onResume() {
		super.onResume();
		//unlockScreen();
	}
			
	View.OnClickListener wakeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
	        finish();
        }			
	};
	
	View.OnClickListener snoozeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateSnooze(index, alarmSnoozes);
			finish();
		}
	};
	
	/*public ArrayList<Uri> randomizeUriArrayList(ArrayList<Uri> uriArrayList) {
		// Shuffles and array of URIs 
		ArrayList<Uri> randomizedArray = new ArrayList<Uri>(uriArrayList.size());
		ArrayList<Integer> randomIndexArray = generateRandomIndexList(uriArrayList.size());
		
		for(int i=0; i < randomIndexArray.size(); i += 1)
			randomizedArray.add(uriArrayList.get(randomIndexArray.get(i)));
		
		return randomizedArray;
	}
	
	public ArrayList<Integer> generateRandomIndexList(int size) {
		// List of distinct random integers between 0 and size. Ex: [2,5,1,4,3] ///
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
		}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waking_time, menu);
		return true;
	}
	
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
	
	public void getSnoozesPreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		alarmSnoozes[ALARM_1_INDEX] = preferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = preferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = preferences.getInt("alarm3Snooze", 0);
	}
	
	
	
}
	
	/*public void playSong(Uri uri, MediaPlayer mp){
    try{
        boolean mStartPlaying = true;
        mp = null;
        if (mStartPlaying==true){
            mp = new MediaPlayer();
            mp.setDataSource(getBaseContext(),uri);
            mp.prepare();
            mp.start();
        } 
        else{
            //mp.release();
            mp= null;
        }
        mStartPlaying = !mStartPlaying;
    }
    catch (IOException e){
        Log.e("0", "prepare() failed");
    }
}*/


/*public static Uri getAudioContentUri(Context context, File audioFile) {
    String filePath = audioFile.getAbsolutePath();
    Cursor cursor = context.getContentResolver().query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            new String[] { MediaStore.Audio.Media._ID },
            MediaStore.Audio.Media.DATA + "=? ",
            new String[] { filePath }, null);
    if (cursor != null && cursor.moveToFirst()) {
        int id = cursor.getInt(cursor
                .getColumnIndex(MediaStore.MediaColumns._ID));
        Uri baseUri = Uri.parse("content://media/external/audio/media");
        return Uri.withAppendedPath(baseUri, "" + id);
    } else {
        if (audioFile.exists()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.DATA, filePath);
            return context.getContentResolver().insert(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }
}*/




