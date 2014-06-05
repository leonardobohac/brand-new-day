package com.example.brandnewday;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Settings extends Activity {
	ImageView mon;
	ImageView tue;
	ImageView wed;
	ImageView thu;
	ImageView fri;
	ImageView sat;
	ImageView sun;
	public boolean[] selected_days;  //days that will play this alarm - [mon, tue, wed, ...]
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_settings);
		
		mon = (ImageView)findViewById(R.id.monday);
		mon.setOnClickListener(dayButtonListener);
		tue = (ImageView)findViewById(R.id.tuesday);
		tue.setOnClickListener(dayButtonListener);
		wed = (ImageView)findViewById(R.id.wednesday);
		wed.setOnClickListener(dayButtonListener);
		thu = (ImageView)findViewById(R.id.thursday);
		thu.setOnClickListener(dayButtonListener);
		fri = (ImageView)findViewById(R.id.friday);
		fri.setOnClickListener(dayButtonListener);
		sat = (ImageView)findViewById(R.id.saturday);
		sat.setOnClickListener(dayButtonListener);
		sun = (ImageView)findViewById(R.id.sunday);
		sun.setOnClickListener(dayButtonListener);
		
		
		
	}

	View.OnClickListener dayButtonListener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	ImageView day =(ImageView)v;
	    	if(day.isSelected()){
	    		day.setImageResource(R.drawable.btn_inactive);
	    		day.setSelected(false);
	    	}
	    	else{
	    		day.setImageResource(R.drawable.btn_active);
	    		day.setSelected(true);
	    	}
	    }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
