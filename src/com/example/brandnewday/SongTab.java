package com.example.brandnewday;

import android.os.Bundle;
import android.app.TabActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class SongTab extends TabActivity 
{
            /** Called when the activity is first created. */
            @Override
            public void onCreate(Bundle savedInstanceState)
            {
                    super.onCreate(savedInstanceState);
                    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setContentView(R.layout.tab_host);

                    // create the TabHost that will contain the Tabs
                    TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


                    TabSpec tab1 = tabHost.newTabSpec("First Tab");
                    TabSpec tab2 = tabHost.newTabSpec("Second Tab");
 

                   // Set the Tab name and Activity
                   // that will be opened when particular Tab will be selected
                    tab1.setIndicator("Músicas");
                    tab1.setContent(new Intent(this,SongList.class));
                    
                    tab2.setIndicator("RingTones");
                    tab2.setContent(new Intent(this,Ringtones.class));

       
                    
                    /** Add the tabs  to the TabHost to display. */
                    tabHost.addTab(tab1);
                    tabHost.addTab(tab2);

            }
} 