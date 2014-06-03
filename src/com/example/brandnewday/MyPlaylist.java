package com.example.brandnewday;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyPlaylist extends Activity {
	MyApplication myApplication;
	ArrayList<Uri> audioUris = new ArrayList<Uri>();
	ArrayList<String> audioPaths = new ArrayList<String>();
	ArrayList<Uri> randomAudioUris;
	String[] mMusicList;

	String audioUrisFromPreferences;
	String audioPathsFromPreferences;
	Button newSongButton;
	Button okButton;
	private static final int PICKFILE_RESULT_CODE = 1;
	ListView playlistListView;
	String songName;
	ListView listView;
	ArrayAdapter<String> adapter;
	private static final String TAG = "BND";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_playlist);
		myApplication = getMyApplication();
		
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
		//}
		
		audioPathsFromPreferences = defaultPreferences.getString("audioPathsFromPreferences", "");
		try {
			audioPaths = myApplication.deserializePaths(audioPathsFromPreferences);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		newSongButton = (Button)findViewById(R.id.add_new_song_button);
		okButton = (Button)findViewById(R.id.playlistOkButton);

		okButton.setOnClickListener(okButtonOnClickListener);
		newSongButton.setOnClickListener(newSongButtonOnClickListener);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, audioPaths);
		listView = (ListView) findViewById(R.id.playlist_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() { 
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	audioUris.remove(position);
	            audioPaths.remove(position);
	            randomAudioUris = new ArrayList<Uri>(audioUris.size());
				randomAudioUris = randomizeUriArrayList(audioUris);
	            SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor editor = defaultPreferences.edit();
				try {
					editor.putString("audioUrisFromPreferences", myApplication.serialize(audioUris));
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				try {
					editor.putString("randomAudioUrisFromPreferences", myApplication.serialize(randomAudioUris));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					editor.putString("audioPathsFromPreferences", myApplication.serializePaths(audioPaths));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				editor.commit();
	            listView.setAdapter(adapter);	             
	        }
		}); 
	}
		
	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}

	View.OnClickListener okButtonOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			finish();
		}
	};
	
	View.OnClickListener newSongButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		    i.setType("audio/*");
		    Intent c = Intent.createChooser(i, "Choose Nice Songs");
		    startActivityForResult(c,PICKFILE_RESULT_CODE);
		}
	};
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  switch(requestCode){
	  case PICKFILE_RESULT_CODE:
		  if(resultCode==RESULT_OK) {
			  Uri selectedAudioUri = data.getData();
			  //System.out.println("audioUris: " + audioUris.get(0));

			  String selectedAudioPath = getPathFromUri(selectedAudioUri);
			  String songName = getSongNameFromPath(selectedAudioPath);
			  if(audioUris.contains(selectedAudioUri) == false) {
				  audioUris.add(selectedAudioUri);
				  audioPaths.add(songName);
			  }
			  else
				  Toast.makeText(getApplicationContext(), "song already in list!", Toast.LENGTH_SHORT).show();
			  
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
			try {
				editor.putString("audioPathsFromPreferences", myApplication.serializePaths(audioPaths));
			} catch (Exception e) {
				// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					editor.commit();
		
				listView.setAdapter(adapter);
			  }
			  break;
		  }
	  
	}

	@Override
	protected void onResume() {
		super.onResume();
		}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = defaultPreferences.edit();
		try {
			editor.putString("audioUrisFromPreferences", myApplication.serialize(audioUris));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			editor.putString("audioPathsFromPreferences", myApplication.serializePaths(audioPaths));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		editor.commit();
	}
	
	
	public String getPathFromUri(Uri contentUri) {
		//converted to the REAL path  - like * /storage/sdcard/audio.mp3 *
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	public String getSongNameFromPath(String path) {
		File filename = new File(path);
		String fileNameString = filename.getName();
		String songName = fileNameString.substring(0, fileNameString.lastIndexOf('.'));
		int firstLetterIndex = 0;
		for(int i=0; i < songName.length(); i += 1) {
			if (Character.isLetter(songName.charAt(i))){
					firstLetterIndex = i;
					break;
			}
		}
		
		return songName.substring(firstLetterIndex, songName.length());
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
	
	
	
	  
	  
	/*public static Uri getAudioContentUri(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null, null);
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
	}
	

	private String getFullFilePath(Context context, String filename) {  
	    File directory = context.getExternalFilesDir(null);
	    File file = new File(directory, filename);
	    if (!file.canRead()) {
	        // error handling
	    }
	    return file.getAbsolutePath();  
	}*/
	


	
	
	
			
}	
			
			
		
		
	

 	

        
