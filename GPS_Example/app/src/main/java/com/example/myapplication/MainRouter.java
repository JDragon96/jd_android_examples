package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Examples.GPSExample;
import com.example.myapplication.Examples.SensorExample;

public class MainRouter extends Activity {
    Button btnRUN_GPS, btnRUN_SENSOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_route);

        // BUTTON BINDING
        btnRUN_GPS = (Button)findViewById(R.id.btnRUN_GPS);
        btnRUN_GPS.setOnClickListener(gps_run_listener);

        btnRUN_SENSOR = (Button)findViewById(R.id.btnRUN_SENSOR);
        btnRUN_SENSOR.setOnClickListener(sensor_run_listener);
    }


    ////////////////////////////////////////
    // BIND BUTTON FUNCTION
    Button.OnClickListener gps_run_listener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent gps_intent = new Intent(MainRouter.this, GPSExample.class);
            startActivity(gps_intent);
        }
    };
    Button.OnClickListener sensor_run_listener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent sensor_intent = new Intent(MainRouter.this, SensorExample.class);
            startActivity(sensor_intent);
        }
    };
}
