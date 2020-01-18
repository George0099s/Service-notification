package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn:
                startService();
                break;

            case R.id.btn2:
                stopService();
                break;
        }
    }
    
    private void startService(){
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }
    private void stopService(){
        Intent intent = new Intent(this, TimerService.class);
        intent.setAction(TimerService.ACTION_CLOSE);
        stopService(intent);
        Log.d("123", "stopService: ");
    }
    
}
