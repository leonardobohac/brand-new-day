package com.example.brandnewday;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WakingTime extends Activity {
	MyApplication myApplication;
	int index;
	int snooze;
	int[] alarmSnoozes;
	int [] alarmHours;
	int [] alarmMinutes;
	ArrayList<Uri> audioUris;
	ArrayList<String> audioPaths;
	//private ArrayList<Integer> randomIndexList;
	PendingIntent pendingIntent;
	//Uri songUri;
	Intent intent;
	Toast toast;
	MediaPlayer mp;
	//private ArrayList<String> myStringPlaylist;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waking_time);
		myApplication = getMyApplication();
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		alarmHours = myApplication.getAlarmHours();
		alarmMinutes = myApplication.getAlarmMinutes();
		alarmSnoozes = myApplication.getAlarmSnoozes();
		//stringAudioUris = (myApplication.getAudioUris()).toString();
		//stringAudioPaths = myApplication.getAudioPaths();
		
		//audioUris = myApplication.getAudioUris();
		/*Uri firstUri = audioUris.get(0);
		playSong(firstUri, mp);*/
		
		
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
		}
		
		/*randomIndexList = generateRandomIndexList();
		for(int index=0; index < randomIndexList.size(); index += 1) {
			playSong(myPlaylist.get(randomIndexList.get(index)), mp);
		}*/
		
		//System.out.println(myStringPlaylist);
		
	

/*	@Override
	protected void onPause() {
		super.onPause();
		mp.release();
		finish();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mp.release();
		finish();
	}*/
	
	View.OnClickListener wakeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			toast.cancel();
	        finish();
	        
        }			
	};
	
	View.OnClickListener snoozeButtonOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			myApplication.activateSnooze(index, alarmSnoozes);
			toast.cancel();
			finish();
		}
	};
	
	public void playSong(Uri uri, MediaPlayer mp){
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
	}
	
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
	
	/*public ArrayList<Integer> generateRandomIndexList() {
		Random generator = new Random();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<Integer> randomIndexList = new ArrayList<Integer>();
		
		for(int i=0; i < myStringPlaylist.size(); i += 1) {
			indexList.add(i);
		}
		for(int j=0; j < myStringPlaylist.size(); j += 1) {
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

}




