package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the gravity button
     */
    public void switchToGravityActivity(View view) {
        Intent intent = new Intent(this, GravityActivity.class);
        startActivity(intent);

    }
    public void switchToCanvasActivity(View view) {
        Intent intent = new Intent(this, CanvasTest.class);
        startActivity(intent);
    }

}
