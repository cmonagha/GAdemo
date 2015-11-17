package com.example.user.gademo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;

import com.example.user.gademo.CBob.CgaView;

public class MainActivity extends Activity implements View.OnClickListener {

    private CgaView cgaView;
    private GestureDetector gestureDetector;
    //private GameEngine mGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "OnCreate RAN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cgaView = (CgaView) findViewById(R.id.cgaView);
        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.pauseButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);
        //gestureDetector = new GestureDetector(this, gestureListener);
        //mGameEngine = new GameEngine(this);
        // if selection is afadfa'
        //new CBobsMap.addtoEngine
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Log.i("MainActivity", "OnPause RAN");
        super.onPause(); // call the super method
        cgaView.killThread(); // terminates the game
    }

    @Override
    protected void onDestroy()
    {
        Log.i("MainActivity", "OnDestroy RAN");
        super.onDestroy();
        cgaView.releaseResources();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.i("MainActivity", "OnTouchEvent RAN");
        int action = event.getAction();

        if(action==MotionEvent.ACTION_DOWN)
        {
            cgaView.start();
        }

        return gestureDetector.onTouchEvent(event);
    }

    SimpleOnGestureListener gestureListener = new SimpleOnGestureListener()
    {

    };*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playButton) {
            cgaView.start();
        }
        else if (v.getId() == R.id.pauseButton) {
            cgaView.stopGame(); // terminates the game
            Log.i("oaase", "Pause Ran");
        }
        else if (v.getId() == R.id.resetButton){
            cgaView.reset();
            Log.i("B", "This ran");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }
}
