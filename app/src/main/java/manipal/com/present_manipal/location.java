package manipal.com.present_manipal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

public class location extends AppCompatActivity {
    TextView txt;
    private FusedLocationProviderClient mFusedLocationClient;
    String lati;
    String longi;
    private LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        location();
    }
    public void location(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(location.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
            return;
        }
        /*mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {
                            Toast.makeText(location.this,"Found Your Location!",Toast.LENGTH_SHORT).show();
                            lati= String.valueOf(location.getLatitude());
                            longi=String.valueOf(location.getLongitude());
                        }
                        else{
                            Intent i=new Intent(Intent.ACTION_VIEW);
                            startActivity(i);
                        }
                    }
                });*/
        mLocationRequest=new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setInterval(2000);
        LocationSettingsRequest.Builder build=new LocationSettingsRequest.Builder();
        build.addLocationRequest(mLocationRequest);
        LocationSettingsRequest lsr=build.build();
        SettingsClient settingsClient=LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(lsr);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                onLocationChanged(locationResult.getLastLocation());
            }
        },Looper.myLooper());
    }
    private void onLocationChanged(Location location) {
        longi= String.valueOf(location.getLongitude());
        lati= String.valueOf(location.getLatitude());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location();
                }
        }
    }

    public void refresh(View view) {
        location();
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,display_class.class);
        i.putExtra("longitude",longi);
        i.putExtra("latitude",lati);
        mFusedLocationClient.removeLocationUpdates(new LocationCallback());
        startActivity(i);
        finish();
    }
}