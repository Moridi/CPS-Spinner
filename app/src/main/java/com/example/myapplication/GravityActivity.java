package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class GravityActivity extends AppCompatActivity {
    private final static double TIME_SLICE_SECONDS = .02;

    private int layoutRight;
    private int layoutBottom;

    private int movingObjectWidth;
    private int movingObjectHeight;

    private int rightestPosition;
    private int bottommostPosition;

    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private boolean gameStarted = false;
    private View movingObject;

    public void ballClicked(View view) {
        View layout = findViewById(R.id.layout);
        this.layoutRight = layout.getRight();
        this.layoutBottom = layout.getBottom();

        this.movingObject = findViewById(R.id.movingObject);
        this.movingObjectWidth = movingObject.getWidth();
        this.movingObjectHeight = movingObject.getHeight();

        this.rightestPosition = layoutRight - movingObjectWidth;
        this.bottommostPosition = layoutBottom - movingObjectHeight;

        this.x = movingObject.getX();
        this.y = movingObject.getY();

        gameStarted = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGravitySensor(sensorManager);
    }

    private void setupGravitySensor(SensorManager sensorManager) {
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        SensorEventListener rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double gravityX = sensorEvent.values[0];
                double gravityY = sensorEvent.values[1];
                double gravityZ = sensorEvent.values[2];

                TextView sensorStatus = findViewById(R.id.gravitySensorStatus);
                String sensorOutputs = String.format(Locale.ENGLISH, "gravity_x: %.4f\ngravity_y: %.4f\ngravity_z: %.4f\nx: %.4f\ny: %.4f\nvx: %.4f\nvy: %.4f\n", gravityX, gravityY, gravityZ, x, y, vx, vy);
                sensorStatus.setText(sensorOutputs);

                if (gameStarted) {
                    int scale = 200;
                    moveObject(-gravityX * scale, gravityY * scale);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(rvListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void moveObject(double ax, double ay) {
        double newX = (1 / 2F) * ax * Math.pow(TIME_SLICE_SECONDS, 2) + vx * TIME_SLICE_SECONDS + x;
        double newY = (1 / 2F) * ay * Math.pow(TIME_SLICE_SECONDS, 2) + vy * TIME_SLICE_SECONDS + y;
        x = (newX >= rightestPosition) ? rightestPosition : (float) ((newX <= 0) ? 0 : newX);
        y = (newY >= bottommostPosition) ? bottommostPosition : (float) ((newY <= 0) ? 0 : newY);

        double newVX = ax * TIME_SLICE_SECONDS + vx;
        double newVY = ay * TIME_SLICE_SECONDS + vy;
        vx = (newX >= rightestPosition || newX <= 0) ? 0 : newVX;
        vy = (newY >= bottommostPosition || newY <= 0) ? 0 : newVY;

        setPosition(newX, newY);
    }

    private void setPosition(double newX, double newY) {
        movingObject.setX((float) newX);
        movingObject.setY((float) newY);
    }
}