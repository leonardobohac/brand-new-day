package com.example.brandnewday;

import java.util.ArrayList;
import java.util.Random;

import org.apache.pig.impl.util.ObjectSerializer;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Ringtones extends ListActivity {
	ListAdapter listadapter;
	Cursor mCursor2;
	RingtoneManager mRingtoneManager2;
	Uri localuri2;
	int position;
	ArrayList<Uri> audioUris = new ArrayList<Uri>();
	String audioUrisFromPreferences;
	ArrayList<Uri> randomAudioUris;
	

	//adds a menu item from the res/menu/menu.xml
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.brand_new_day, menu);
	return true;
	} 


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	

	mRingtoneManager2 = new RingtoneManager(this); //adds ringtonemanager
	mRingtoneManager2.setType(RingtoneManager.TYPE_RINGTONE); //sets the type to ringtones
	mRingtoneManager2.setIncludeDrm(true); //get list of ringtones to include DRM

	mCursor2 = mRingtoneManager2.getCursor(); //appends my cursor to the ringtonemanager

	startManagingCursor(mCursor2); //starts the cursor query

	//prints output for diagnostics
	String test = mCursor2.getString(mCursor2.getColumnIndexOrThrow(RingtoneManager.EXTRA_RINGTONE_TITLE));
	Log.d(null, test, null);

	String[] from = {mCursor2.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX)}; // get the list items for the listadapter could be TITLE or URI

	int[] to = {android.R.id.text1}; //sets the items from above string to listview

	//new listadapter, created to use android checked template
	SimpleCursorAdapter listadapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, mCursor2, from, to );
	setListAdapter(listadapter);

	//prints output for diagnostics
	String test2 = listadapter.toString();
	Log.d(null, test2, null);

	//adds listview so I can get data from it
	ListView lv = getListView();
	lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
	SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	audioUrisFromPreferences = defaultPreferences.getString("audioUrisFromPreferences", "");
	//if(audioStringFromPreferences != ""){
		try {
			audioUris = deserialize(audioUrisFromPreferences);
			//Log.d("uri", (audioUris.get(0)).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(lv, v, position, id);
	
	//just a system test to make sure the items check are being registered
	int i = lv.getCheckedItemCount();
	System.out.println(i);

	//gets the ids of the selected items
	@SuppressWarnings("deprecation")
	long[] ids = lv.getCheckItemIds();
	String string_ids = ids.toString();
	Log.d("tag", string_ids);

	//creates a uri for the items selected
	Uri localuri = ContentUris.withAppendedId(MediaStore.Audio.Media.getContentUriForPath(string_ids), id);
	String diag = localuri.toString();
	System.out.println(diag);

	//shows a toast to make sure the uri selected has the correct path
	Toast toasted = Toast.makeText(this, diag, Toast.LENGTH_SHORT);
	toasted.show();
	
	if(audioUris.contains(localuri) == false){
		audioUris.add(localuri);
		
	}
	else {
		audioUris.remove(localuri);
	}
	randomAudioUris = new ArrayList<Uri>(audioUris.size());
	randomAudioUris = randomizeUriArrayList(audioUris);
	
	SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	  SharedPreferences.Editor editor = defaultPreferences.edit();
		
	  try {
		editor.putString("audioUrisFromPreferences", serialize(audioUris));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  try {
			editor.putString("randomAudioUrisFromPreferences", serialize(randomAudioUris));
		} catch (Exception e) {
			// TODO Auto-generated catch block
					e.printStackTrace();
		}
	  editor.commit();

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

