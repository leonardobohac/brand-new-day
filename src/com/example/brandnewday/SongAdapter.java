package com.example.brandnewday;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

import org.apache.pig.impl.util.ObjectSerializer;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {
	private ArrayList<Song> songs;
	private LayoutInflater songInf;
	ArrayList<Uri> audioUris = new ArrayList<Uri>();
	String audioUrisFromPreferences;
	
		
	public SongAdapter(Context c, ArrayList<Song> theSongs){
		  songs=theSongs;
		  songInf=LayoutInflater.from(c);
		  
	}
		  
	
	  @Override
	  public int getCount() {
		  return songs.size();
	  }
	 
	  @Override
	  public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return null;
	  }
	 
	  @Override
	  public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
		  audioUrisFromPreferences = defaultPreferences.getString("audioUrisFromPreferences", "");
		  try {
			audioUris = deserialize(audioUrisFromPreferences);	
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	    //map to song layout
	    LinearLayout songLay = (LinearLayout)songInf.inflate
	        (R.layout.song, parent, false);
	    //get title and artist views
	    TextView songView = (TextView)songLay.findViewById(R.id.song_title);
	    TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
	    //get song using position
	    Song currSong = songs.get(position);
	    //get title and artist strings
	    songView.setText(currSong.getTitle());
	    artistView.setText(currSong.getArtist());
	    
    	long currSongID = currSong.getID();
    	
    	Uri trackUri = ContentUris.withAppendedId(
    	  android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    	  currSongID);
    	
    	
    	if(audioUris.contains(trackUri)==true){
    		songView.setTextColor(Color.parseColor("#33B6E5"));
    	}
    	else{
    		songView.setTextColor(Color.parseColor("#FFFFFF"));
    	}
    	
	    //set position as tag
	    songLay.setTag(position);
	    return songLay;
	  }
	  
	  public ArrayList<Uri> deserialize(String string) throws Exception {
			@SuppressWarnings("unchecked")
			ArrayList<String> stringArray = (ArrayList<String>) ObjectSerializer.deserialize(string);
			ArrayList<Uri> uriArray = new ArrayList<Uri>();
			for(int i=0; i < stringArray.size(); i+=1) {
				uriArray.add(Uri.parse(stringArray.get(i)));
			}
			return uriArray;
		}
			
		public String serialize(ArrayList<Uri> uriArray) throws Exception {
			ArrayList<String> stringArray = new ArrayList<String>();
			for(int i=0; i < uriArray.size(); i+=1) {
				stringArray.add((uriArray.get(i)).toString());
			}
			String string = ObjectSerializer.serialize(stringArray);
			return string;
			
		}
		
}