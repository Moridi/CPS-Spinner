package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

public class GyroscopeActivity extends AppCompatActivity {
    SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGyroscopeSensor(sensorManager);
    }


    private void setupGyroscopeSensor(SensorManager sensorManager) {
        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Create a listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float gyroscopeX = sensorEvent.values[0];
                float gyroscopeY = sensorEvent.values[1];
                float gyroscopeZ = sensorEvent.values[2];

                TextView sensorStatus = findViewById(R.id.gyroscopeSensorStatus);
                String sensorOutputs = String.format(Locale.ENGLISH, "x: %.4f\ny: %.4f\nz: %.4f\n", gyroscopeX, gyroscopeY, gyroscopeZ);
                sensorStatus.setText(sensorOutputs);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
