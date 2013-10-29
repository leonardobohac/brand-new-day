package com.example.brandnewday;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewDebug.FlagToString;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class WakingTime extends Activity {//implements MediaPlayer.OnCompletionListener{
	MyApplication myApplication;
	
	int index;
	int snooze;
	int[] alarmSnoozes;
	int [] alarmHours;
	int [] alarmMinutes;
	int currentTrack = 0;
	private ArrayList<Uri> audioUris;
	private ArrayList<Uri> randomizedAudioUris;
	private ArrayList<String> audioPaths;
	private MediaPlayer mediaPlayer = null;
	PendingIntent pendingIntent;
	Intent intent;
	Toast toast;
	

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED); 
		setContentView(R.layout.waking_time);
		myApplication = getMyApplication();
		audioUris = myApplication.getAudioUris();
		randomizedAudioUris = new ArrayList<Uri>(audioUris.size());
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		
		
		/*Activity myActivity = (Activity)getBaseContext();
		Window window = myActivity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);*/

		
		Button wakeButton = (Button)findViewById(R.id.wake_button);
		Button snoozeButton = (Button)findViewById(R.id.snooze_button);
		
		wakeButton.setOnClickListener(wakeButtonOnClickListener);
		snoozeButton.setOnClickListener(snoozeButtonOnClickListener);
		
		snooze = alarmSnoozes[index];
		if(snooze == 0)
			snoozeButton.setVisibility(View.GONE);
		else
			snoozeButton.setVisibility(View.VISIBLE);
	
		String hourString = Integer.toString(alarmHours[index]);
		String minuteString = Integer.toString(alarmMinutes[index]);
		String alarmInfo = "Index: " + Integer.toString(index) + "  " + "Set for: " + hourString + ":" + minuteString;
		
		
		toast = Toast.makeText(getApplicationContext(), alarmInfo, Toast.LENGTH_LONG);
		toast.show();
		
		Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
        
		/*randomizedAudioUris = randomizeUriArrayList(audioUris);
		System.out.println(randomizedAudioUris.toString());
		if(this.randomizedAudioUris.size() != 0){
			 mediaPlayer = MediaPlayer.create(getApplicationContext(), this.randomizedAudioUris.get(currentTrack));
		     mediaPlayer.setOnCompletionListener(this);
		     mediaPlayer.start();
		}
		else
			System.out.println("No songs in playlist");*/
	}
	
				
	/*public void onCompletion(MediaPlayer arg0) {
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
	    }*/
			   
			 

		/*randomIndexList = generateRandomIndexList();
		for(int index=0; index < randomIndexList.size(); index += 1) {
			playSong(myPlaylist.get(randomIndexList.get(index)), mp);
		}*/
		
		//System.out.println(myStringPlaylist);
		
	

	/*@Override
	protected void onPause() {
		super.onPause();
		mediaPlayer.release();
		finish();
	}*/
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//mediaPlayer.reset();
		if(mediaPlayer != null)
			mediaPlayer.release();
	}
	

			
	View.OnClickListener wakeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			toast.cancel();
			
	        finish();
	        
        }			
	};
	
	View.OnClickListener snoozeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(WakingTime.this, AlarmService.class);
			stopService(i);
			myApplication.activateSnooze(index, alarmSnoozes);
			toast.cancel();
			finish();
		}
	};
	
	public ArrayList<Uri> randomizeUriArrayList(ArrayList<Uri> uriArrayList) {
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
		}
	
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




