package com.example.shivr.e_commerce;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.radius;

/**
 * Created by shivr on 24/04/2017.
 */

public class GeoFenceLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    public List uwiFenceList;


    @Override
    public void onCreate() {
        super.onCreate();

        uwiFenceList = new ArrayList();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(!Utils.getSharedPrefsBoolean(Utils.getSharedPrefs(getApplicationContext()), Utils.geoFencesSet)) {
            Log.i("Location","GeoFence Set");
            enableLocationDetection();
            Utils.insertSharedPrefs(Utils.geoFencesSet, true, Utils.getSharedPrefs(getApplicationContext()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location Changed",""+location.getLatitude());
        Log.i("Location Changed",""+location.getLongitude());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, getGeofencePendingIntent());
        mGoogleApiClient.disconnect();
        Log.i("Hello", "I am being killed!");
    }

    private void enableLocationDetection(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, getLocationRequest(), this);
            createFence();
            addFence();
        }
        else{
            Log.i("Notice-Denied","Location Access Denied.");
            this.stopSelf();
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    private void createFence(){
        Map details = new HashMap();
        details.put("latitude",Utils.latitude );
        details.put("longitude", Utils.longtitude );
        details.put("radius",200);
        details.put("ID", 0);

        uwiFenceList.add(new Geofence.Builder()
                .setRequestId(String.valueOf(0))
                .setCircularRegion(
                        (double)details.get("latitude"),
                        (double)details.get("longitude"),
                        radius
                )
                .setLoiteringDelay(300000)
                .setExpirationDuration(-1)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                .build()
        );
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(uwiFenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        Intent intent = new Intent(this, GeoFenceIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addFence(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationService", "Attempting to setup Add a GeoFence");
            if (uwiFenceList != null &&  uwiFenceList.size() > 0) {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(this);
            }else{
                Log.d("LocationService", "No values in the geoFence List");
                return;
            }
        }else{
            Log.d("LocationService", "Permission for Location not given");
            return;
        }
        Log.d("LocationService", "Geofence setup complete");
    }
}
