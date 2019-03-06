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
    private final int READ_SENSOR_RATE = 20; // sensor read rate in microsecond
    private final int UPDATE_VIEW_RATE = 15 * 1000; // refresh View rate in microsecond

    private int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        this.movingObject = findViewById(R.id.movingObject);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupGravitySensor(sensorManager);
    }

    public void ballClicked(View view) {
        resume();
    }

    private void resume() {
        View layout = findViewById(R.id.layout);
        int layoutRight = layout.getRight();
        int layoutBottom = layout.getBottom();

        int movingObjectWidth = movingObject.getWidth();
        int movingObjectHeight = movingObject.getHeight();

        this.RIGHTEST_POSITION = layoutRight - movingObjectWidth;
        this.BOTTOMMOST_POSITION = layoutBottom - movingObjectHeight;

        this.x = movingObject.getX();
        this.y = movingObject.getY();

        gameStarted = true;
        findViewById(R.id.pauseBanner).setVisibility(View.INVISIBLE);
    }


    private void setupGravitySensor(SensorManager sensorManager) {
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        SensorEventListener rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
                if (dT > READ_SENSOR_RATE) {
                    double gravityX = sensorEvent.values[0];
                    gravityX = -gravityX;
                    double gravityY = sensorEvent.values[1];
                    double gravityZ = sensorEvent.values[2];

                    if (gameStarted) {
                        final double time_slice = dT * US2S;
                        fx = vx == 0 ? gravityX - (gravityZ * MU_S) : gravityX - (gravityZ * MU_K);
                        fy = vy == 0 ? gravityY - (gravityZ * MU_S) : gravityY - (gravityZ * MU_K);

                        double ax = fx / MASS;
                        double ay = fy / MASS;


                        double newX = (0.5) * ax * Math.pow(time_slice, 2) + vx * time_slice + x;
                        double newY = (0.5) * ay * Math.pow(time_slice, 2) + vy * time_slice + y;
                        x = (newX >= RIGHTEST_POSITION) ? RIGHTEST_POSITION : (float) ((newX <= 0) ? 0 : newX);
                        y = (newY >= BOTTOMMOST_POSITION) ? BOTTOMMOST_POSITION : (float) ((newY <= 0) ? 0 : newY);

                        double newVX = ax * time_slice + vx;
                        double newVY = ay * time_slice + vy;
                        vx = (newX >= RIGHTEST_POSITION || newX <= 0) ? -newVX * DAMPING_COEFFICIENT : newVX;
                        vy = (newY >= BOTTOMMOST_POSITION || newY <= 0) ? -newVY * DAMPING_COEFFICIENT : newVY;

                    }
                    readSensorTimestamp = sensorEvent.timestamp;
                }
                dT = (sensorEvent.timestamp - refreshViewTimestamp) * NS2US;
                if (dT > UPDATE_VIEW_RATE && gameStarted) {
                    moveObject();
                    TextView sensorStatus = findViewById(R.id.gravitySensorStatus);
                    String sensorOutputs = getSensorStatus(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                    sensorStatus.setText(sensorOutputs);
                    refreshViewTimestamp = sensorEvent.timestamp;
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

    public void moveObject() {
        setPosition(x, y);
    }

    private void setPosition(double newX, double newY) {
        movingObject.setX((float) newX);
        movingObject.setY((float) newY);
    }
}