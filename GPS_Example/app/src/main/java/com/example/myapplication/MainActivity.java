package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LocationListener {
    double lat1;
    double lon1;
    private double latitude;
    private double longitude;

    TextView textGPSProvider, textCurLat, textCurLon, textLat1, textLon1, textConnectCheck;
    Button btnGPS1Test, btnSaveGPS;
    protected LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView
        textCurLat = findViewById(R.id.textCurLat);
        textCurLon = findViewById(R.id.textCurLon);
        textLat1 = findViewById(R.id.textLat1);
        textLon1 = findViewById(R.id.textLon1);
        textGPSProvider = findViewById(R.id.textGPSProvider);

        // Button
        btnGPS1Test = findViewById(R.id.btnGetGPS1);
        btnSaveGPS = findViewById(R.id.btnSaveGPS);
        
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
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        latitude = bestLocation.getLatitude();
        longitude = bestLocation.getLongitude();
        textCurLat.setText(Double.toString(latitude) + "_init");
        textCurLon.setText(Double.toString(longitude) + "_init");
        textGPSProvider.setText(bestLocation.getProvider());
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
        TableLayout layout = (TableLayout)findViewById(R.id.table_main);
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
    public void getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                int hasFineLocationPermission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                } else {
                    return;
                }

                if (isGPSEnabled) {
                    // 1초, 2미터 거리변화 감지 시 GPS값 측정
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);
                }
            }

        } catch (Exception e) {
            Log.d("@@@", "" + e.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Location bestlocation = getLastKnownLocation();
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