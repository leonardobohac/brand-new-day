package com.example.brandnewday;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyPlaylist extends Activity {
	MyApplication myApplication;
	public ArrayList<Uri> audioUris;
	ArrayList<String> audioPaths;

	
	Button newSongButton;
	Button okButton;
	private static final int PICKFILE_RESULT_CODE = 1;
	ListView myUriPlaylistListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_playlist);
		myApplication = getMyApplication();
		audioUris = myApplication.getAudioUris();
		
		newSongButton = (Button)findViewById(R.id.add_new_song_button);
		okButton = (Button)findViewById(R.id.playlistOkButton);

		okButton.setOnClickListener(okButtonOnClickListener);
		newSongButton.setOnClickListener(newSongButtonOnClickListener);
		
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
			  System.out.println("URI: " + selectedAudioUri);
			  //String filemanagerString = selectedAudioUri.getPath();
			  //System.out.println("FilemanagerString: " + filemanagerString);
			  //String selectedAudioPath = getPathFromUri(selectedAudioUri);

			  audioUris.add(selectedAudioUri);

			  

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
	}
	
	
	public String getPathFromUri(Uri contentUri) {
		//converted to the REAL path  - like * /storage/sdcard/audio.mp3 *
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	
	
	
	  
	  
	  //audioPaths.add(selectedAudioPath);
	  /*if(selectedAudioPath!=null)
		  System.out.println("selectedAudioPath is the right one for you!");
	  else
		  System.out.println("filemanagerstring is the right one for you!");*/
		   
	  
//String songRelativePath = (data.getData().getPath());
	  //Uri songUri = Uri.parse("content://media" + songRelativePath);
	  //String songUriString = ("contenṭ://media" + songRelativePath);
	  
	  /*if(myStringPlaylist.contains(songUriString))
		  Toast.makeText(getApplicationContext(),"Song already in list!",1).show();
	  else
		  myStringPlaylist.add(songUriString);*/
	  
	  //Log.d("URI: ", songUri.toString());
	  //String path = getPathFromUri(songUri);
	  //System.out.println(path);
	  //System.out.println("Playlist after result: " + myStringPlaylist);
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
	

	/*private String getFullFilePath(Context context, String filename) {  
	    File directory = context.getExternalFilesDir(null);
	    File file = new File(directory, filename);
	    if (!file.canRead()) {
	        // error handling
	    }
	    return file.getAbsolutePath();  
	}*/
	


	/*String[] STAR = { "*" };     
	Uri allaudiosong = MediaStore.Audio.Media.getContentUri("external");
	String audioselection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
	Cursor cursor;
	cursor = managedQuery(allaudiosong, STAR, audioselection, null, null);
	
	if (cursor != null) {
	    if (cursor.moveToFirst()) {
	        do {
	            String song_name = cursor
	                   .getString(cursor
	                             .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
	            System.out.println("Audio Song Name= "+song_name);
	
	            int song_id = cursor.getInt(cursor
	                    .getColumnIndex(MediaStore.Audio.Media._ID));
	            System.out.println("Audio Song ID= "+song_id);
	
	            String fullpath = cursor.getString(cursor
	                    .getColumnIndex(MediaStore.Audio.Media.DATA));
	            System.out.println("Audio Song FullPath= "+fullpath);
	
	            String album_name = cursor.getString(cursor
	                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));
	            System.out.println("Audio Album Name= "+album_name);
	
	            int album_id = cursor.getInt(cursor
	                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	            System.out.println("Audio Album Id= "+album_id);
	
	            String artist_name = cursor.getString(cursor
	                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
	            System.out.println("Audio Artist Name= "+artist_name);
	
	            int artist_id = cursor.getInt(cursor
	                    .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
	            System.out.println("Audio Artist ID= "+artist_id);
	
	        }while (cursor.moveToNext());
	    }
	}*/
	
	
			
}	
			
			
		
		
	

 	

        
