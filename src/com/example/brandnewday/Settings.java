package com.example.brandnewday;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	MyApplication myApplication;
	static NumberFormat formatter = new DecimalFormat("00");
	
	RelativeLayout mon;
	RelativeLayout tue;
	RelativeLayout wed;
	RelativeLayout thu;
	RelativeLayout fri;
	RelativeLayout sat;
	RelativeLayout sun;
	
	ArrayList<RelativeLayout> layout_of_days;
	
	ImageView check;
	ImageView inc_hour;
	ImageView dec_hour;
	ImageView inc_d_minute;
	ImageView inc_u_minute;
	ImageView dec_d_minute;
	ImageView dec_u_minute;
	TextView hour;
	TextView d_minute;
	TextView u_minute;
	TextView inc_snooze;
	TextView dec_snooze;
	TextView snooze;
	SeekBar volume_seekBar;
	
	Context context = this;

	protected int index;
	protected int new_hour;
	protected int new_d_minute;
	protected int new_u_minute;
	protected int new_snooze;
	protected float volume;
	static final int ALARM_1_INDEX = 0;
	static final int ALARM_2_INDEX = 1;
	static final int ALARM_3_INDEX = 2;
	static final boolean ALARM_ALREADY_ACTIVE = true;
	static final boolean ALARM_NOT_ACTIVE = false;
	public int[] alarmHours, alarmMinutes, alarmSnoozes;
	public float[] volumes;
	public boolean[] alarmActivated;
	public static float volumeMedium = 0.5f;
	public ArrayList<String> alarm1_days;
	public ArrayList<String> alarm2_days;
	public ArrayList<String> alarm3_days;
	public ArrayList<ArrayList<String>> alarm_days;
	public ArrayList<String> current_alarm_days;
	public String alarm1_days_preferences;
	public String alarm2_days_preferences;
	public String alarm3_days_preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		index = intent.getExtras().getInt("index");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_settings);
		
		myApplication = getMyApplication();
		alarmHours = new int[3]; //myApplication.getAlarmHours();
		alarmMinutes = new int[3]; //myApplication.getAlarmMinutes();
		alarmSnoozes = new int[3]; //myApplication.getAlarmSnoozes();
		alarmActivated = new boolean[4]; //myApplication.getAlarmActivated();
		volumes = new float[3];
		
		alarm_days = new ArrayList<ArrayList<String>>(3);
		alarm1_days = new ArrayList<String>();
		alarm2_days = new ArrayList<String>();
		alarm3_days = new ArrayList<String>();
		current_alarm_days = new ArrayList<String>();
		layout_of_days = new ArrayList<RelativeLayout>(7);
		
		
		SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		alarmHours[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Hour", 7);
		alarmHours[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Hour", 8);
		alarmHours[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Hour", 9);
		alarmMinutes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Minute", 30);
		alarmMinutes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Minute", 15);
		alarmMinutes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Minute", 45);
		alarmSnoozes[ALARM_1_INDEX] = defaultPreferences.getInt("alarm1Snooze", 0);
		alarmSnoozes[ALARM_2_INDEX] = defaultPreferences.getInt("alarm2Snooze", 0);
		alarmSnoozes[ALARM_3_INDEX] = defaultPreferences.getInt("alarm3Snooze", 0);
		alarmActivated[ALARM_1_INDEX] = defaultPreferences.getBoolean("alarm1Activated", false);
		alarmActivated[ALARM_2_INDEX] = defaultPreferences.getBoolean("alarm2Activated", false);
		alarmActivated[ALARM_3_INDEX] = defaultPreferences.getBoolean("alarm3Activated", false);
		volumes[ALARM_1_INDEX] = defaultPreferences.getFloat("alarm1Volume", volumeMedium);
		volumes[ALARM_2_INDEX] = defaultPreferences.getFloat("alarm2Volume", volumeMedium);
		volumes[ALARM_3_INDEX] = defaultPreferences.getFloat("alarm3Volume", volumeMedium);
		alarm1_days_preferences = defaultPreferences.getString("alarm1_days_preferences", "");
		alarm2_days_preferences = defaultPreferences.getString("alarm2_days_preferences", "");
		alarm3_days_preferences = defaultPreferences.getString("alarm3_days_preferences", "");
		
	
			try {
				alarm1_days = myApplication.deserializeStrings(alarm1_days_preferences);
				alarm2_days = myApplication.deserializeStrings(alarm2_days_preferences);
				alarm3_days = myApplication.deserializeStrings(alarm3_days_preferences);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		alarm_days.add(ALARM_1_INDEX, alarm1_days);
		alarm_days.add(ALARM_2_INDEX, alarm2_days);
		alarm_days.add(ALARM_3_INDEX, alarm3_days);
		current_alarm_days = alarm_days.get(index);
		
		mon = (RelativeLayout)findViewById(R.id.mon);
		tue = (RelativeLayout)findViewById(R.id.tue);
		wed = (RelativeLayout)findViewById(R.id.wed);
		thu = (RelativeLayout)findViewById(R.id.thu);
		fri = (RelativeLayout)findViewById(R.id.fri);
		sat = (RelativeLayout)findViewById(R.id.sat);
		sun = (RelativeLayout)findViewById(R.id.sun);
		
		layout_of_days.add(sun);
		layout_of_days.add(mon);
		layout_of_days.add(tue);
		layout_of_days.add(wed);
		layout_of_days.add(thu);
		layout_of_days.add(fri);
		layout_of_days.add(sat);
		
		
		check = (ImageView)findViewById(R.id.check);
		inc_hour = (ImageView)findViewById(R.id.increase_hour);
		dec_hour = (ImageView)findViewById(R.id.decrease_hour);
		inc_d_minute = (ImageView)findViewById(R.id.increase_d_minute);
		dec_d_minute = (ImageView)findViewById(R.id.decrease_d_minute);
		inc_u_minute = (ImageView)findViewById(R.id.increase_u_minute);
		dec_u_minute = (ImageView)findViewById(R.id.decrease_u_minute);
		
		hour = (TextView)findViewById(R.id.settings_hour);
		d_minute = (TextView)findViewById(R.id.settings_d_minute);
		u_minute = (TextView)findViewById(R.id.settings_u_minute);
		
		volume_seekBar = (SeekBar)findViewById(R.id.volume_seekBar);
		
		snooze = (TextView)findViewById(R.id.snooze);
		inc_snooze = (TextView)findViewById(R.id.increase_snooze);
		dec_snooze = (TextView)findViewById(R.id.decrease_snooze);
		
		mon.setOnClickListener(day_button_Listener);
		tue.setOnClickListener(day_button_Listener);
		wed.setOnClickListener(day_button_Listener);
		thu.setOnClickListener(day_button_Listener);
		fri.setOnClickListener(day_button_Listener);
		sat.setOnClickListener(day_button_Listener);
		sun.setOnClickListener(day_button_Listener);
		
		inc_hour.setOnClickListener(inc_hour_Listener);
		dec_hour.setOnClickListener(dec_hour_Listener);
		inc_d_minute.setOnClickListener(inc_d_minute_Listener);
		dec_d_minute.setOnClickListener(dec_d_minute_Listener);
		inc_u_minute.setOnClickListener(inc_u_minute_Listener);
		dec_u_minute.setOnClickListener(dec_u_minute_Listener);
		inc_snooze.setOnClickListener(inc_snooze_Listener);
		dec_snooze.setOnClickListener(dec_snooze_Listener);
		check.setOnClickListener(check_Listener);
		volume_seekBar.setOnSeekBarChangeListener(OnSeekBarChangeListener);
		
		new_hour = alarmHours[index];
		new_d_minute = alarmMinutes[index]/10;
		new_u_minute = alarmMinutes[index]%10;
		new_snooze = alarmSnoozes[index];
		volume = volumes[index];
		
		/* UPDATE VALUES */
		hour.setText(formatter.format(new_hour));
		d_minute.setText(Integer.toString(new_d_minute));
		u_minute.setText(Integer.toString(new_u_minute));
		snooze.setText(Integer.toString(new_snooze));
		volume_seekBar.setProgress((int)(volume*100));
		enable_days(current_alarm_days, layout_of_days);
	}

	
	View.OnClickListener inc_hour_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_hour == 23)
	    		new_hour = 0;
	    	else
	    		new_hour += 1;
	    	hour.setText(formatter.format(new_hour));
	    }
	};
	View.OnClickListener dec_hour_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_hour == 0)
	    		new_hour = 23;
	    	else
	    		new_hour -= 1;
	    	hour.setText(formatter.format(new_hour));
	    }
	};
	View.OnClickListener inc_d_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_d_minute == 5)
	    		new_d_minute = 0;
	    	else
	    		new_d_minute += 1;
	    	d_minute.setText(Integer.toString(new_d_minute));
	    	
	    }
	};
	View.OnClickListener dec_d_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_d_minute == 0)
	    		new_d_minute = 5;
	    	else
	    		new_d_minute -= 1;
	    	d_minute.setText(Integer.toString(new_d_minute));
	    }
	};
	View.OnClickListener inc_u_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_u_minute == 9)
	    		new_u_minute = 0;
	    	else
	    		new_u_minute += 1;
	    	u_minute.setText(Integer.toString(new_u_minute));
	    }
	};
	View.OnClickListener dec_u_minute_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_u_minute == 0)
	    		new_u_minute = 9;
	    	else
	    		new_u_minute -= 1;
	    	u_minute.setText(Integer.toString(new_u_minute));
	    }
	};
	View.OnClickListener dec_snooze_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_snooze > 0)
	    		new_snooze -= 1;
	    	snooze.setText(Integer.toString(new_snooze));	    	
	    }
	};
	View.OnClickListener inc_snooze_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	if(new_snooze < 30)
	    		new_snooze += 1;
	    	snooze.setText(Integer.toString(new_snooze));	    	
	    }
	};
	View.OnClickListener day_button_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	RelativeLayout rl = (RelativeLayout)v;
	    	if(rl.isSelected() == true){
	    		rl.setBackground(getResources().getDrawable(R.drawable.btn_inactive_small));
	    		rl.setSelected(false);
	    		if(rl == sun){
	    			current_alarm_days.remove("1");
	    		}
	    		else if(rl == mon){
	    			current_alarm_days.remove("2");
	    		}
	    		else if(rl == tue){
	    			current_alarm_days.remove("3");
	    		}
	    		else if(rl == wed){
	    			current_alarm_days.remove("4");
	    		}
	    		else if(rl == thu){
	    			current_alarm_days.remove("5");
	    		}
	    		else if(rl == fri){
	    			current_alarm_days.remove("6");
	    		}
	    		else if(rl == sat){
	    			current_alarm_days.remove("7");
	    		}
	    		alarm_days.set(index, current_alarm_days);
	    		
	    	}
	    	else{
	    		rl.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
	    		rl.setSelected(true);
	    		if(rl == sun){
	    			current_alarm_days.add("1");
	    		}
	    		else if(rl == mon){
	    			current_alarm_days.add("2");
	    		}
	    		else if(rl == tue){
	    			current_alarm_days.add("3");
	    		}
	    		else if(rl == wed){
	    			current_alarm_days.add("4");
	    		}
	    		else if(rl == thu){
	    			current_alarm_days.add("5");
	    		}
	    		else if(rl == fri){
	    			current_alarm_days.add("6");
	    		}
	    		else if(rl == sat){
	    			current_alarm_days.add("7");
	    		}
	    		alarm_days.set(index, current_alarm_days);
	    		
	    		
	    	}		
	    }
	};
	
	private void enable_days(ArrayList<String> current_alarm_days, ArrayList<RelativeLayout> days_layout){
		sun = days_layout.get(0);
		mon = days_layout.get(1);
		tue = days_layout.get(2);
		wed = days_layout.get(3);
		thu = days_layout.get(4);
		fri = days_layout.get(5);
		sat = days_layout.get(6);

		
		for(int i=0; i<current_alarm_days.size(); i++){
			String active_day = current_alarm_days.get(i);
			switch(Integer.parseInt(active_day)) {
				case 1:
					sun.setSelected(true);
					sun.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
				
				case 2:
					mon.setSelected(true);
					mon.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
				
				case 3:
					tue.setSelected(true);
					tue.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
				
				case 4:
					wed.setSelected(true);
					wed.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
					
				case 5:
					thu.setSelected(true);
					thu.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
					
				case 6:
					fri.setSelected(true);
					fri.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
					
				case 7:
					sat.setSelected(true);
					sat.setBackground(getResources().getDrawable(R.drawable.btn_active_small));
					break;
			}
		}
	}
	
	View.OnClickListener check_Listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	alarmHours[index] = new_hour;
			alarmMinutes[index] = new_d_minute*10 + new_u_minute;
			alarmSnoozes[index] = new_snooze;
			alarmActivated[index] = true;
			
			SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = defaultPreferences.edit();
			editor.putInt("alarm1Hour", alarmHours[ALARM_1_INDEX]);
			editor.putInt("alarm2Hour", alarmHours[ALARM_2_INDEX]);
			editor.putInt("alarm3Hour", alarmHours[ALARM_3_INDEX]);
			editor.putInt("alarm1Minute", alarmMinutes[ALARM_1_INDEX]);
			editor.putInt("alarm2Minute", alarmMinutes[ALARM_2_INDEX]);
			editor.putInt("alarm3Minute", alarmMinutes[ALARM_3_INDEX]);
			editor.putInt("alarm1Snooze", alarmSnoozes[ALARM_1_INDEX]);
			editor.putInt("alarm2Snooze", alarmSnoozes[ALARM_2_INDEX]);
			editor.putInt("alarm3Snooze", alarmSnoozes[ALARM_3_INDEX]);
			editor.putBoolean("alarm1Activated", alarmActivated[ALARM_1_INDEX]);
			editor.putBoolean("alarm2Activated", alarmActivated[ALARM_2_INDEX]);
			editor.putBoolean("alarm3Activated", alarmActivated[ALARM_3_INDEX]);
			editor.putFloat("alarm1Volume", volumes[ALARM_1_INDEX]);
			editor.putFloat("alarm2Volume", volumes[ALARM_2_INDEX]);
			editor.putFloat("alarm3Volume", volumes[ALARM_3_INDEX]);
			
			try {
				editor.putString("alarm1_days_preferences", myApplication.serializeStrings(alarm1_days));
				editor.putString("alarm2_days_preferences", myApplication.serializeStrings(alarm2_days));
				editor.putString("alarm3_days_preferences", myApplication.serializeStrings(alarm3_days));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			editor.commit();
			
			
			myApplication.activateAlarm(index, alarmHours, alarmMinutes, alarmSnoozes);
			Toast.makeText(getApplicationContext(),"Alarme Ativado! Sonhe com os anjos", Toast.LENGTH_SHORT).show();
			finish();
	    }
	};
	
	SeekBar.OnSeekBarChangeListener OnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			double volume = progress/100.0;
			volumes[index] = (float)volume;
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	protected MyApplication getMyApplication() {
		return (MyApplication)getApplication();
	}
}
