package com.example.myapplication;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.Math.sqrt;

public class GyroscopeActivity extends AppCompatActivity {

    private static final float EPSILON = 0.0001f;
    private int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;


    private float[] currentRotationVector = new float[3];

    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private double fx;
    private double fy;

    private double MASS = 0.01;

    private double MU_S = .15;
    private double MU_K = .1;

    private float readSensorTimestamp = 1;
    private float refreshViewTimestamp = 1;
    private static final float NS2US = 1.0f / 1000.0f; // ns to microsecond
    private static final float US2S = 1.0f / 1000000.0f; // ns to microsecond
    private final int readSensorRate = 20; // sensor read rate in microsecond
    private final int updateViewRate = 15 * 1000; // refresh View rate in microsecond


    SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGyroscopeSensor(sensorManager);
    }

    public  void resetSensor(View view) {
        currentRotationVector[0] = 0;
        currentRotationVector[1] = 0;
        currentRotationVector[2] = 0;
    }


    private void setupGyroscopeSensor(SensorManager sensorManager) {
        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Create a listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // In case of anticlockwise rotation, it will be positive.
                float gyroscopeX = sensorEvent.values[0]; // angular velocity along the X (rad / s)
                float gyroscopeY = sensorEvent.values[1]; // Y
                float gyroscopeZ = sensorEvent.values[2]; // Z

                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }

                float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
                if (dT > readSensorRate) {
                    // Axis of the rotation sample, not normalized yet.
                    float axisX = sensorEvent.values[0];
                    float axisY = sensorEvent.values[1];
                    float axisZ = sensorEvent.values[2];

                    currentRotationVector[0] += (float) Math.toDegrees(axisX * dT * US2S);
                    currentRotationVector[1] += (float) Math.toDegrees(axisY * dT * US2S);
                    currentRotationVector[2] += (float) Math.toDegrees(axisZ * dT * US2S);

                    TextView sensorStatus = findViewById(R.id.gyroscopeSensorStatus);
                    String sensorOutputs = String.format(Locale.ENGLISH, "x: %.4f\ny: %.4f\nz: %.4f\n",
                            currentRotationVector[0],
                            currentRotationVector[1],
                            currentRotationVector[2]
                    );
                    sensorStatus.setText(sensorOutputs);


                    readSensorTimestamp = sensorEvent.timestamp;
                }
                dT = (sensorEvent.timestamp - refreshViewTimestamp) * NS2US;
                if (dT > updateViewRate) {
                    // refresh views here
                    refreshViewTimestamp = sensorEvent.timestamp;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
