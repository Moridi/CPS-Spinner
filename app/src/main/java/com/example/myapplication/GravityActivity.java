package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

public class GravityActivity extends AppCompatActivity {
    SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGravitySensor(sensorManager);
    }

    private void setupGravitySensor(SensorManager sensorManager) {
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        SensorEventListener rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float gravityX = sensorEvent.values[0];
                float gravityY = sensorEvent.values[1];
                float gravityZ = sensorEvent.values[2];

                TextView sensorStatus = findViewById(R.id.gravitySensorStatus);
                String sensorOutputs = String.format(Locale.ENGLISH, "x: %.4f\ny: %.4f\nz: %.4f\n", gravityX, gravityY, gravityZ);
                sensorStatus.setText(sensorOutputs);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(rvListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
