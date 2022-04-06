package com.example.myapplication.Examples;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class SensorExampleLegacy extends Activity{

    private SensorManager sensorManager;
    private float[] floatGeoMagnetic = new float[3];
    private float[] floatOrientation = new float[3];

    final float[] rotationMatrix = new float[9];
    final float[] orientationAngles = new float[3];

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    // magnetic text
    TextView textMX, textMY, textMZ;
    // orientation text
    TextView textAZIMUTH, textROLL, textPITCH;

    // SENSORS
    Sensor m_mag_sensor, m_acc_sensor, m_ori_sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_example);

        // SENSOR MANAGER SETTING
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        m_mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        m_acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Orientation 센서 활용을 위한 아래 방법은 legacy임
        //m_ori_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        sensorManager.registerListener(magenticListener, m_mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(orientationListener, m_mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);

        // TEXT BINDING
        textMX = findViewById(R.id.textMX);
        textMY = findViewById(R.id.textMY);
        textMZ = findViewById(R.id.textMZ);
        textPITCH = findViewById(R.id.textPITCH);
        textAZIMUTH = findViewById(R.id.textAZIMUTH);
        textROLL = findViewById(R.id.textROLL);

    }

    SensorEventListener magenticListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            floatGeoMagnetic = event.values;
            textMX.setText(String.format("%.1f", floatGeoMagnetic[0]));
            textMY.setText(String.format("%.1f", floatGeoMagnetic[1]));
            textMZ.setText(String.format("%.1f", floatGeoMagnetic[2]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener orientationListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
