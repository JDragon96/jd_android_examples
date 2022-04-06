package com.example.myapplication.Examples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.GPS_MODEL;

import java.util.ArrayList;
import java.util.List;

public class GPSExample extends AppCompatActivity
        implements LocationListener {

    private double latitude;
    private double longitude;
    private double accuracy = 100;
    List<GPS_MODEL> model_list;

    TextView textGPSProvider, textCurLat, textCurLon, textBearing, textBearingAcc, textGPSAcc;
    Button btnGPS1Test, btnSaveGPS, btnClear, btnMail;
    TableLayout layout;

    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_example);

        layout = (TableLayout)findViewById(R.id.table_main);

        // TextView
        textCurLat = findViewById(R.id.textCurLat);
        textCurLon = findViewById(R.id.textCurLon);
        textBearing = findViewById(R.id.textBearing);
        textBearingAcc = findViewById(R.id.textBearingAcc);
        textGPSProvider = findViewById(R.id.textGPSProvider);
        textGPSAcc = findViewById(R.id.textGPSAcc);

        // Button
        btnGPS1Test = findViewById(R.id.btnGetGPS1);
        btnSaveGPS = findViewById(R.id.btnSaveGPS);
        btnSaveGPS.setOnClickListener(saveListener);
        btnClear = findViewById(R.id.btnGPSClear);
        btnClear.setOnClickListener(clearListener);
        btnMail = findViewById(R.id.btnMail);
        btnMail.setOnClickListener(mailListener);
        
        addTable();

        // GPS 권한 설정
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
        
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        boolean gps_check = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_check = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(network_check)
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this);
        }

        if(gps_check)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this);
        }

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        
        ///////////////////////////////// 파라미터 초기 설정
        latitude = bestLocation.getLatitude();
        longitude = bestLocation.getLongitude();
        textCurLat.setText(Double.toString(latitude) + "_init");
        textCurLon.setText(Double.toString(longitude) + "_init");
        textGPSProvider.setText(bestLocation.getProvider());
        textBearing.setText(Float.toString(bestLocation.getBearing()));
        model_list = new ArrayList();
    }

    Button.OnClickListener clearListener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            model_list.clear();
            layout.removeViews(1, Math.max(0, layout.getChildCount() - 1));
        }
    };

    Button.OnClickListener saveListener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            if(accuracy < 15)
            {
                GPS_MODEL model = new GPS_MODEL();
                model.setLat(latitude);
                model.setLon(longitude);
                model.setIndex(model_list.size());

                model_list.add(model);
                addOnTable(model);
            }
        }
    };

    Button.OnClickListener mailListener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            // 메일 내용 세팅
            String s = new String("");
            for(GPS_MODEL model: model_list)
            {
                String new_string = new String(Integer.toString(model.getIndex()) + ", " +
                        Double.toString(model.getLat()) + ", " +
                        Double.toString(model.getLon()) + "\n");
                s += new_string;
            }


            // 메일 전송
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            String[] address = {"보내고싶은 사람 메일 입력하기"};
            email.putExtra(Intent.EXTRA_EMAIL, address);
//            email.putExtra(Intent.EXTRA_SUBJECT, "test@test");
            email.putExtra(Intent.EXTRA_TEXT, s);
            startActivity(email);
        }
    };

    private void addOnTable(GPS_MODEL model)
    {
        TableRow tbrow = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(Integer.toString(model_list.size()));
        tv0.setTextColor(Color.BLACK);

        TextView tv1 = new TextView(this);
        tv1.setText(Double.toString(model.getLat()));
        tv1.setTextColor(Color.BLACK);

        TextView tv2 = new TextView(this);
        tv2.setText(Double.toString(model.getLon()));
        tv2.setTextColor(Color.BLACK);

        tbrow.addView(tv0);
        tbrow.addView(tv1);
        tbrow.addView(tv2);
        layout.addView(tbrow);
    }

    private Location getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void addTable()
    {
        TableRow tbrow = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText("INDEX");
        tv0.setTextColor(Color.BLACK);

        TextView tv1 = new TextView(this);
        tv1.setText("Latitude");
        tv1.setTextColor(Color.BLACK);

        TextView tv2 = new TextView(this);
        tv2.setText("Longitude");
        tv2.setTextColor(Color.BLACK);

        tbrow.addView(tv0);
        tbrow.addView(tv1);
        tbrow.addView(tv2);

        layout.addView(tbrow);
    }

/////////////////////////////////////////////////////////////////////////////////////////
//  GPS 관련
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onLocationChanged(Location location)
    {
        Location bestlocation = getLastKnownLocation();

        accuracy = bestlocation.getAccuracy();
        textGPSAcc.setText(Double.toString(accuracy));

        latitude = bestlocation.getLatitude();
        longitude = bestlocation.getLongitude();
        textCurLat.setText(Double.toString(latitude));
        textCurLon.setText(Double.toString(longitude));
        textGPSProvider.setText(bestlocation.getProvider());
    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }
}