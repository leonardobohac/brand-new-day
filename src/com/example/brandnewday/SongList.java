package com.example.brandnewday;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.net.Uri;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SongList extends Activity {
	MyApplication myApplication;
	private ArrayList<Song> songList;
	ArrayList<Uri> audioUris = new ArrayList<Uri>();
	String audioUrisFromPreferences;
	ArrayList<Uri> randomAudioUris;
	private ListView songView;
	private static final String TAG = "BND";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Toast.makeText(getApplicationContext(), "Clique para adicionar/remover a música da playlist", Toast.LENGTH_LONG).show();

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_song_list);
		myApplication = getMyApplication();
		songView = (ListView)findViewById(R.id.song_list);
		songList = new ArrayList<Song>();
		getSongList();
		
		
		//sort alphabetically
		Collections.sort(songList, new Comparator<Song>(){
			  public int compare(Song a, Song b){
			    return a.getTitle().compareTo(b.getTitle());
			  }
			});
		
		final SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);
		
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		audioUrisFromPreferences = defaultPreferences.getString("audioUrisFromPreferences", "");
		//if(audioStringFromPreferences != ""){
			try {
				audioUris = myApplication.deserialize(audioUrisFromPreferences);
				//Log.d("uri", (audioUris.get(0)).toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		songView.setOnItemClickListener(new OnItemClickListener() { 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	//get song
	        	Song selectedSong = songList.get(position);
	        	//get id
	        	long currSong = selectedSong.getID();
	        	//set uri
	        	Uri trackUri = ContentUris.withAppendedId(
	        	  android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	        	  currSong);
	        	
	        	
	        	if(audioUris.contains(trackUri) == false){
	        		audioUris.add(trackUri);
	        		((TextView)((LinearLayout)view).getChildAt(0)).setTextColor(Color.parseColor("#33B6E5"));
	        		
	        		
	        	}
	        	else {
	        		audioUris.remove(trackUri);
	        		((TextView)((LinearLayout)view).getChildAt(0)).setTextColor(Color.parseColor("#FFFFFF"));
	        	}
	        	randomAudioUris = new ArrayList<Uri>(audioUris.size());
				randomAudioUris = randomizeUriArrayList(audioUris);
				
				SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				  SharedPreferences.Editor editor = defaultPreferences.edit();
					
				  try {
					editor.putString("audioUrisFromPreferences", myApplication.serialize(audioUris));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
				  try {
						editor.putString("randomAudioUrisFromPreferences", myApplication.serialize(randomAudioUris));
					} catch (Exception e) {
						// TODO Auto-generated catch block
								e.printStackTrace();
					}
				  editor.commit();
				  
				  

	        	/*Log.d(TAG, Integer.toString(audioUris.size()));
	        	for(int i=0; i<audioUris.size();i+=1){
	        		Log.d(TAG, audioUris.get(i).toString());
	        	}*/
	        	
	        }
	        
		});
	}
		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_list, menu);
		return true;
	}
	
	public void getSongList() {
		  //retrieve song info
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			  //get columns
			  int titleColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.TITLE);
			  int idColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media._ID);
			  int artistColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.ARTIST);
			  //add songs to list
			  do {
			    long thisId = musicCursor.getLong(idColumn);
			    String thisTitle = musicCursor.getString(titleColumn);
			    String thisArtist = musicCursor.getString(artistColumn);
			    songList.add(new Song(thisId, thisTitle, thisArtist));
			  }
			  while (musicCursor.moveToNext());
			}
		}

	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
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
		// List of distinct random integers between 1 and size. Ex: [2,5,1,4,3], with size = 5 //
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
}
