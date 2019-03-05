package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void gyroscopeButtonClicked(View view) {
        Intent intent = new Intent(this, GyroscopeActivity.class);
        startActivity(intent);
    }

    public void gravityButtonClicked(View view) {
        Intent intent = new Intent(this, GravityActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
