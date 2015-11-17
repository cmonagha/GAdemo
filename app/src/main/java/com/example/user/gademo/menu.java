package com.example.user.gademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;

/**
 * Created by christophermonaghan on 10/16/15.
 */
public class menu extends Activity implements View.OnClickListener {

    private GestureDetector gestureDetector;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        findViewById(R.id.StartBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.StartBtn) {
            Intent intent = new Intent(menu.this, MainActivity.class);
            intent.putExtra("gane", 1);
            startActivity(intent);
        }
    }



}
