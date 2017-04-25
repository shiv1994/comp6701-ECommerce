package com.example.shivr.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private SharedPreferences sharedPreferences;
    private GoogleSignInAccount googleSignInAccount;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private TextView name, email;
    private ImageView userImage;
    private Button buttonViewProds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = Utils.getSharedPrefs(this.getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        name = (TextView)header.findViewById(R.id.textViewUserName);
        email = (TextView)header.findViewById(R.id.textViewUserEmail);
        userImage = (ImageView) header.findViewById(R.id.imageViewUserImage);

        buttonViewProds = (Button) findViewById(R.id.buttonViewAllProds);
        buttonViewProds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext() ,"Switching!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(view.getContext() , ViewAllProducts.class));
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignInAccount = Utils.getUserInfo(sharedPreferences);

        if(googleSignInAccount!=null) {
            Log.i("Test GSO Object", "User Info" + googleSignInAccount.getEmail());
            name.setText(googleSignInAccount.getDisplayName());
            email.setText(googleSignInAccount.getEmail());
            userImage.setImageURI(googleSignInAccount.getPhotoUrl());
        }

        new MainActivity.HttpRequestTask().execute();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_signOut) {
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

    private class HttpRequestTask extends AsyncTask<Void, Void, String>{

        protected String doInBackground(Void... voids){

            HttpsURLConnection urlConnection=null;
            String resp="";
            SSLHelper sslHelper = SSLHelper.getInstance(MainActivity.this);

//            try {
//                String endpoint = "/wc-auth/v1/authorize";
//                String queryString = "app_name=test&scope=read_write&user_id=" + googleSignInAccount.getEmail()
//                        + "&return_url=https://67.205.172.180/&callback_url" +
//                        "=https://67.205.172.180/handle_keys.php";
//
//                // Tell the URLConnection to use a SocketFactory from our SSLContext
//                URL url = new URL("https://67.205.172.180" + endpoint + "?" + queryString);
//                urlConnection = (HttpsURLConnection) url.openConnection();
//                urlConnection.setSSLSocketFactory(sslHelper.getSSLContext().getSocketFactory());
//
////                // create JSON object to hold paramaters to be passed
////                JSONObject params = new JSONObject();
////                params.putOpt("app_name", new String("test"));
////                params.putOpt("scope", new String("read_write"));
////                params.putOpt("user_id", googleSignInAccount.getDisplayName());
////                params.putOpt("return_url", new String("https://67.205.172.180/return-page"));
////                params.putOpt("callback_url", new String("https://67.205.172.180/callback-endpoint"));
//
//                // configuring connection to allow data to be posted
//                //urlConnection.setDoOutput(true);
//                // set content length to avoid internal buffering
//                //urlConnection.setFixedLengthStreamingMode(params.toString().getBytes().length);
//                Log.v("http", "connecting");
//
//                urlConnection.setHostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String s, SSLSession sslSession) {
//                        return true;
//                    }
//                });
//                urlConnection.connect();
//
//                Log.v("http", "connected");
//
//                resp = Integer.toString(urlConnection.getResponseCode());
//            }
//            catch(SSLPeerUnverifiedException e){
//                e.printStackTrace();
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//            finally {
//                if(urlConnection != null)
//                    urlConnection.disconnect();
//            }

            // pulls down product info
            try {
                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL("https://67.205.172.180/wp-json/wc/v2/products");
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setSSLSocketFactory(sslHelper.getSSLContext().getSocketFactory());

                // credentials
                String user = "ck_970486c965566c58a881f7501f244bc77bebd732";
                String pass = "cs_070469e8a858dd957792d25fe51ec8234aedabbf";
                String auth = user + ":" + pass;
                byte [] encoded  = Base64.encode(auth.getBytes(),Base64.NO_WRAP);
                urlConnection.setRequestProperty("Authorization", "Basic " +
                        new String(encoded));

                urlConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {

                        return true;
                    }
                });
                urlConnection.connect();

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    resp = result.toString();
                }
            }
            catch(SSLPeerUnverifiedException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return resp;
        }

        protected void onPostExecute(String response){
            JSONArray jsonArray=null;
            try{
                jsonArray = new JSONArray(response);

                int x, id;
                String name;

                for(x=0;x<jsonArray.length();x++){
                    JSONObject temp = jsonArray.getJSONObject(x);
                    id = temp.getInt("id");
                    name = temp.getString("name");

                    System.out.println("ID: " + id + " Name: " + name);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
        }
    }
}
