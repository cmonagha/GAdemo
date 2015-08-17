package com.example.user.gademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private CgaView cgaView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "OnCreate RAN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cgaView = (CgaView) findViewById(R.id.cgaView);

        gestureDetector = new GestureDetector(this, gestureListener);
    }

    @Override
    public void onPause()
    {
        Log.i("MainActivity", "OnPause RAN");
        super.onPause(); // call the super method
        cgaView.stopGame(); // terminates the game
    }

    @Override
    protected void onDestroy()
    {
        Log.i("MainActivity", "OnDestroy RAN");
        super.onDestroy();
        cgaView.releaseResources();
    }

    @Override
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

    };
}
