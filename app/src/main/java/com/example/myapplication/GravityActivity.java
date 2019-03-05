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
    public static final double DAMPING_COEFFICIENT = .5;
    private final static double TIME_SLICE_SECONDS = .02;

    private int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;

    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private double fx;
    private double fy;

    private double MASS = 0.01;

    private double MU_S = .15;
    private double MU_K = .1;

    private boolean gameStarted = false;
    private View movingObject;

    public void screenClick(View view) {
        if (gameStarted) {
            this.x = movingObject.getX();
            this.y = movingObject.getY();

            findViewById(R.id.pauseBanner).setVisibility(View.VISIBLE);

            gameStarted = false;
        } else
            resume();
    }

    public void ballClicked(View view) {
        resume();
    }

    private void resume() {
        View layout = findViewById(R.id.layout);
        int layoutRight = layout.getRight();
        int layoutBottom = layout.getBottom();

        this.movingObject = findViewById(R.id.movingObject);
        int movingObjectWidth = movingObject.getWidth();
        int movingObjectHeight = movingObject.getHeight();

        this.RIGHTEST_POSITION = layoutRight - movingObjectWidth;
        this.BOTTOMMOST_POSITION = layoutBottom - movingObjectHeight;

        this.x = movingObject.getX();
        this.y = movingObject.getY();

        gameStarted = true;
        findViewById(R.id.pauseBanner).setVisibility(View.INVISIBLE);
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
                gravityX = -gravityX;
                double gravityY = sensorEvent.values[1];
                double gravityZ = sensorEvent.values[2];

                TextView sensorStatus = findViewById(R.id.gravitySensorStatus);
                String sensorOutputs = getSensorStatus(gravityX, gravityY, gravityZ);
                sensorStatus.setText(sensorOutputs);

                if (gameStarted) {
                    fx = vx == 0 ? gravityX - (gravityZ * MU_S) : gravityX - (gravityZ * MU_K);
                    double ax = fx / MASS;

                    fy = vy == 0 ? gravityY - (gravityZ * MU_S) : gravityY - (gravityZ * MU_K);
                    double ay = fy / MASS;

                    moveObject(ax, ay);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(rvListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private String getSensorStatus(double gravityX, double gravityY, double gravityZ) {
        return String.format(Locale.ENGLISH, "gravity_x: %.4f\ngravity_y: %.4f\ngravity_z: %.4f\nx: %.4f\ny: %.4f\nvx: %.4f\nvy: %.4f\n", gravityX, gravityY, gravityZ, x, y, vx, vy);
    }

    public void moveObject(double ax, double ay) {
        double newX = (0.5) * ax * Math.pow(TIME_SLICE_SECONDS, 2) + vx * TIME_SLICE_SECONDS + x;
        double newY = (0.5) * ay * Math.pow(TIME_SLICE_SECONDS, 2) + vy * TIME_SLICE_SECONDS + y;
        x = (newX >= RIGHTEST_POSITION) ? RIGHTEST_POSITION : (float) ((newX <= 0) ? 0 : newX);
        y = (newY >= BOTTOMMOST_POSITION) ? BOTTOMMOST_POSITION : (float) ((newY <= 0) ? 0 : newY);

        double newVX = ax * TIME_SLICE_SECONDS + vx;
        double newVY = ay * TIME_SLICE_SECONDS + vy;
        vx = (newX >= RIGHTEST_POSITION || newX <= 0) ? -newVX * DAMPING_COEFFICIENT : newVX;
        vy = (newY >= BOTTOMMOST_POSITION || newY <= 0) ? -newVY * DAMPING_COEFFICIENT : newVY;

        setPosition(newX, newY);
    }

    private void setPosition(double newX, double newY) {
        movingObject.setX((float) newX);
        movingObject.setY((float) newY);
    }
}