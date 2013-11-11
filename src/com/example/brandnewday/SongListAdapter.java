package com.example.brandnewday;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SongListAdapter extends ArrayAdapter<String> {

	protected static final String LOG_TAG = SongListAdapter.class.getSimpleName();
	
	private ArrayList<String> songs;;
	private int layoutResourceId;
	private Context context;

	public SongListAdapter(Context context, int layoutResourceId, ArrayList<String> songs) {
		super(context, layoutResourceId, songs);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.songs = songs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SongHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new SongHolder();
		holder.song = songs.get(position);
		//holder.removeSongButton = (ImageButton)row.findViewById(R.id.song_removeSong);
		holder.removeSongButton.setTag(holder.song);

		//holder.name = (TextView)row.findViewById(R.id.atomPay_name);
		setNameTextChangeListener(holder);
		//holder.value = (TextView)row.findViewById(R.id.atomPay_value);
		setValueTextListeners(holder);

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(SongHolder holder) {
		holder.name.setText(holder.song);
	}

	public static class SongHolder {
		String song;
		TextView name;
		TextView value;
		ImageButton removeSongButton;
	}
	
	private void setNameTextChangeListener(final SongHolder holder) {
		holder.name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				holder.song = (s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}

	private void setValueTextListeners(final SongHolder holder) {
		holder.value.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try{
					holder.song = s.toString();
				}catch (NumberFormatException e) {
					Log.e(LOG_TAG, "error reading double value: " + s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}
}
