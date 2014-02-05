package com.ergonzalez.gestiondecpm;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
 
public class SplashScreenActivity extends Activity {
 
    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 1000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
	        NotificationManager mNotificationManager =
	        		(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
			mNotificationManager.cancel(1);
        
        
       
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.splash_screen);
 
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
 
                
                Intent mainIntent = new Intent().setClass(
                        SplashScreenActivity.this, Principal.class);
                startActivity(mainIntent);
 
                
                finish();
            }
        };
 
        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
 
}