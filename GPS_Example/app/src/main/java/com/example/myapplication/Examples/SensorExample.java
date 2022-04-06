package com.example.myapplication.Examples;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

public class SensorExample extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor proximity;

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

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

        // 해당 엑티비티를 portrait mode로 고정시킨다.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // SENSOR MANAGER SETTING
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        
        // Orientation 센서 활용을 위한 아래 방법은 legacy임
        //m_ori_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //sensorManager.registerListener(magenticListener, m_mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(orientationListener, m_mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);

        // TEXT BINDING
        textMX = findViewById(R.id.textMX);
        textMY = findViewById(R.id.textMY);
        textMZ = findViewById(R.id.textMZ);
        textPITCH = findViewById(R.id.textPITCH);
        textAZIMUTH = findViewById(R.id.textAZIMUTH);
        textROLL = findViewById(R.id.textROLL);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 센서 정확도가 변경될 때 하고자 하는 코드 작성
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                break;
            default:
                return;
        }

        // rotation 행렬을 계산한다.
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        /*
        Azimuth : The direction (north/south/east/west) the device is pointing. 0 is magnetic north.
        Pitch : The top-to-bottom tilt of the device. 0 is flat.
        Roll : The left-to-right tilt of the device. 0 is flat.
         */
        textAZIMUTH.setText(String.format("%.4f", azimuth));
        textROLL.setText(String.format("%.4f", roll));
        textPITCH.setText(String.format("%.4f", pitch));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 센서 갱신 차단
        sensorManager.unregisterListener(this);
    }
}
