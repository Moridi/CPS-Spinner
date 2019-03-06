//package com.example.myapplication;
//
//import android.graphics.Color;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.Locale;
//
//import static java.lang.Math.sqrt;
//
//public class GyroscopeActivity extends AppCompatActivity {
//
//    private static final float EPSILON = 0.0001f;
//    private int RIGHTEST_POSITION;
//    private int BOTTOMMOST_POSITION;
//
//
//    private float[] currentRotationVector = new float[3];
//
//    private float x;
//    private float y;
//
//    private double vx = 0;
//    private double vy = 0;
//
//    private double fx;
//    private double fy;
//
//    private double MASS = 0.01;
//
//    private double MU_S = .15;
//    private double MU_K = .1;
//
//    private float readSensorTimestamp = 1;
//    private float refreshViewTimestamp = 1;
//    private static final float NS2US = 1.0f / 1000.0f; // ns to microsecond
//    private static final float US2S = 1.0f / 1000000.0f; // ns to microsecond
//    private final int readSensorRate = 20; // sensor read rate in microsecond
//    private final int updateViewRate = 15 * 1000; // refresh View rate in microsecond
//
//
//    SensorManager sensorManager = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gyroscope);
//
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        setupGyroscopeSensor(sensorManager);
//    }
//
//    public  void resetSensor(View view) {
//        currentRotationVector[0] = 0;
//        currentRotationVector[1] = 0;
//        currentRotationVector[2] = 0;
//    }
//
//
//    private void setupGyroscopeSensor(SensorManager sensorManager) {
//        Sensor gyroscopeSensor =
//                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//
//        // Create a listener
//        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                // In case of anticlockwise rotation, it will be positive.
//                float gyroscopeX = sensorEvent.values[0]; // angular velocity along the X (rad / s)
//                float gyroscopeY = sensorEvent.values[1]; // Y
//                float gyroscopeZ = sensorEvent.values[2]; // Z
//
//                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
//                    getWindow().getDecorView().setBackgroundColor(Color.RED);
//                }
////
////                float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
////                if (dT > readSensorRate) {
////                    // Axis of the rotation sample, not normalized yet.
////                    float axisX = sensorEvent.values[0];
////                    float axisY = sensorEvent.values[1];
////                    float axisZ = sensorEvent.values[2];
////
////                    currentRotationVector[0] += (float) Math.toDegrees(axisX * dT * US2S);
////                    currentRotationVector[1] += (float) Math.toDegrees(axisY * dT * US2S);
////                    currentRotationVector[2] += (float) Math.toDegrees(axisZ * dT * US2S);
////
////                    TextView sensorStatus = findViewById(R.id.gyroscopeSensorStatus);
////                    String sensorOutputs = String.format(Locale.ENGLISH, "x: %.4f\ny: %.4f\nz: %.4f\n",
////                            currentRotationVector[0],
////                            currentRotationVector[1],
////                            currentRotationVector[2]
////                    );
////                    sensorStatus.setText(sensorOutputs);
////
////
////                    readSensorTimestamp = sensorEvent.timestamp;
////                }
////                dT = (sensorEvent.timestamp - refreshViewTimestamp) * NS2US;
////                if (dT > updateViewRate) {
////                    // refresh views here
////                    refreshViewTimestamp = sensorEvent.timestamp;
////                }
//
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//            }
//        };
//
//        // Register the listener
//        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//}

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

public class GyroscopeActivity extends AppCompatActivity {
    public static final double DAMPING_COEFFICIENT = .5;


    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private double fx;
    private double fy;

    private double MASS = 0.05;

    private double MU_S = .15;
    private double MU_K = .1;

    private float readSensorTimestamp = 1;
    private float refreshViewTimestamp = 1;
    private static final float NS2US = 1.0f / 1000.0f; // ns to microsecond
    private static final float US2S = 1.0f / 1000000.0f; // ns to microsecond
    private final int READ_SENSOR_RATE = 20; // sensor read rate in microsecond
    private final int UPDATE_VIEW_RATE = 15 * 1000; // refresh View rate in microsecond
    private final double STANDARD_GRAVITY = 9.80665;

    private int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;

    double gravityX;
    double gravityY;
    double gravityZ;

    private double tetha[] = {0, 0, Math.PI / 2};
//    private double tethaY = 0;
//    private double tethaZ = 0;
//
    private boolean gameStarted = false;
    private View movingObject;

    public void screenClick(View view) {
        if (gameStarted) {
            this.x = movingObject.getX();
            this.y = movingObject.getY();

            tetha[0] = 0;
            tetha[1] = 0;
            tetha[2] = 0;

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
//        setupGravitySensor(sensorManager);
        setupGyroscopeSensor(sensorManager);
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


        private void setupGyroscopeSensor(SensorManager sensorManager) {
        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Create a listener
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
                if (dT > READ_SENSOR_RATE) {

                    double wY = sensorEvent.values[0];
                    double wX = sensorEvent.values[1];
                    double wZ = sensorEvent.values[2];

                    tetha[0] += wX * dT * US2S;
                    tetha[1] += wY * dT * US2S;
                    tetha[2] += wZ * dT * US2S;

                    gravityX = STANDARD_GRAVITY * Math.sin(tetha[0]);
                    gravityY = STANDARD_GRAVITY * Math.sin(tetha[1]);
                    gravityZ = STANDARD_GRAVITY * Math.cos(tetha[2]);

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
                    String sensorOutputs = getSensorStatus(gravityX, gravityY, gravityZ);
                    sensorOutputs += "\n";

//                    sensorOutputs += getSensorStatus((float) Math.toDegrees(tetha[0]),
//                            (float) Math.toDegrees(tetha[1]), (float) Math.toDegrees(tetha[2]));

//                    sensorOutputs += "\n";
//                    sensorOutputs += getSensorStatus(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                    sensorStatus.setText(sensorOutputs);
                    refreshViewTimestamp = sensorEvent.timestamp;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}



//    double wX = sensorEvent.values[0];
//    double wY = sensorEvent.values[1];
//    double wZ = sensorEvent.values[2];
//
//                    if (gameStarted) {
//
//                            tetha[0] += wX * dT * US2S;
//                            tetha[1] += wY * dT * US2S;
//                            tetha[2] += wZ * dT * US2S;
//
//                            gravityX = STANDARD_GRAVITY * Math.sin(tetha[0]);
////                        double gravityY = MASS * STANDARD_GRAVITY * Math.cos(tetha[1]);
//                            double gravityY = 0;
//                            double gravityZ = MASS * STANDARD_GRAVITY * Math.cos(tetha[2]);
