package com.example.myapplication;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class GravityActivity extends AppCompatActivity {

    private TextView scoreLabel;

    private ImageView black;

    private int blackX;
    private int blackY;

    private int blackSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGyroscopeSensor(sensorManager);
        setupGravitySensor(sensorManager);


        black = (ImageView) findViewById(R.id.black);
        blackSpeed = Math.round(768 / 45); // 768 / 45 = 17.06... => 17

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);

        black.setX(150);
        black.setY(150);

//        setupProximitySensor(sensorManager);
//        setupRotationVectorSensor(sensorManager);

        scoreLabel.setText("Score : 0");
    }

    private void setupRotationVectorSensor(SensorManager sensorManager) {
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Create a listener
        SensorEventListener rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, sensorEvent.values);

                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }

                if (orientations[2] > 45) {
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                } else if (orientations[2] < -45) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (Math.abs(orientations[2]) < 10) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register it
        sensorManager.registerListener(rvListener,
                rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setupGravitySensor(SensorManager sensorManager) {
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // Create a listener
        SensorEventListener rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                TextView gravityX = findViewById(R.id.gravityX);
                gravityX.setText(String.valueOf(sensorEvent.values[0]));

                TextView gravityY = findViewById(R.id.gravityY);
                gravityY.setText(String.valueOf(sensorEvent.values[1]));

                TextView gravityZ = findViewById(R.id.gravityZ);
                gravityZ.setText(String.valueOf(sensorEvent.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register it
        sensorManager.registerListener(rvListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setupGyroscopeSensor(SensorManager sensorManager) {
        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Create a listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                TextView gyroscopeX = findViewById(R.id.gyroscopeX);
                gyroscopeX.setText(String.valueOf(sensorEvent.values[0]));
                TextView gyroscopeY = findViewById(R.id.gyroscopeY);
                gyroscopeY.setText(String.valueOf(sensorEvent.values[1]));
                TextView gyroscopeZ = findViewById(R.id.gyroscopeZ);
                gyroscopeZ.setText(String.valueOf(sensorEvent.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setupProximitySensor(SensorManager sensorManager) {
        final Sensor proximitySensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            Log.e("error", "Proximity sensor not available.");
            finish(); // Close app
        }

        // Create listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < Objects.requireNonNull(proximitySensor).getMaximumRange()) {
                    // Detected something nearby
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else {
                    // Nothing is nearby
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register it, specifying the polling interval in microseconds
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
    }
}
