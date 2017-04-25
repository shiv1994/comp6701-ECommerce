package com.example.shivr.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivr.e_commerce.UI.map;
import com.example.shivr.e_commerce.UI.settings;
import com.example.shivr.e_commerce.UI.view_all_products;
import com.example.shivr.e_commerce.UI.view_product_detail;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, view_all_products.OnFragmentInteractionListener, view_product_detail.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, map.OnFragmentInteractionListener, settings.OnFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_CODE = 101;
    private SharedPreferences sharedPreferences;
    private GoogleSignInAccount googleSignInAccount;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private TextView name, email;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);


        setContentView(R.layout.activity_main);

        checkPermissions();

        sharedPreferences = Utils.getSharedPrefs(this.getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.textViewUserName);
        email = (TextView) header.findViewById(R.id.textViewUserEmail);
        userImage = (ImageView) header.findViewById(R.id.imageViewUserImage);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignInAccount = Utils.getUserInfo(sharedPreferences);

        if (googleSignInAccount != null) {
            Log.i("Test GSO Object", "User Info" + googleSignInAccount.getEmail());
            name.setText(googleSignInAccount.getDisplayName());
            email.setText(googleSignInAccount.getEmail());
            userImage.setImageURI(googleSignInAccount.getPhotoUrl());
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Connected!","Connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            getSupportActionBar().setTitle("All Items");
            launchFragment(new view_all_products());
        }
        if(id == R.id.nav_maps){
            getSupportActionBar().setTitle("Map");
//            startActivity(new Intent(this, MapsActivity.class));
            launchFragment(new map());
        }

        if(id== R.id.nav_settings){
            getSupportActionBar().setTitle("Settings");
            launchFragment(new settings());
        }

        else if (id == R.id.nav_signOut) {
            this.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        final Context context = this.getApplicationContext();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Utils.insertSharedPrefsBool(Utils.signedInBoolKey, false, sharedPreferences);
                        startActivity(new Intent(context, SignIn.class));
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private void launchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, fragment)
                .addToBackStack("tag")
                .commit();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permissions","Granted");
                    //Start service.
                    Intent intent = new Intent(this, GeoFenceLocationService.class);
                    startService(intent);
                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(),"Location Awareness Not Enabled!",Toast.LENGTH_LONG);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Connected!","Denied!");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.i("Connected!","Denied!");
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
        else {
            Log.i("Connected!","Granted!");
            Intent intent = new Intent(this, GeoFenceLocationService.class);
            startService(intent);
        }
    }
}
