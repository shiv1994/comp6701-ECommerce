package com.example.shivr.e_commerce;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import static android.R.attr.id;

/**
 * Created by shivr on 25/04/2017.
 */

public class GeoFenceIntentService extends IntentService{

    private SharedPreferences sharedPreferences;
    private List<Coupon> couponList;
    private Coupon selectedCoupon;

    public GeoFenceIntentService() {
        super("geoFencesDetection");
    }

    protected void onHandleIntent(Intent intent) {

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

            if (geofencingEvent.hasError()) {
                Log.e("TAG", ""+geofencingEvent.getErrorCode());
                return;
            }

            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

                // Send notification and log the transition details.
                if(checkTimeInterval()) {
                    sendNotification();
                }
                else{
                    Log.i("Notice","Not in time range of a day!");
                }

            }
            else {
                Log.e("TAG","Error");
            }
        }

        private void sendNotification(){
            //Send specification of time interval to shared preferences for record keeping.
            Utils.insertSharedPrefs(System.currentTimeMillis(), Utils.systemTimeMillis, Utils.getSharedPrefs(getApplicationContext()));
            // Instantiate a Builder object.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentText("You just received a coupon!");
            builder.setSmallIcon(R.drawable.cart);
            // Creates an Intent for the Activity
            Intent notifyIntent = new Intent(this, MainActivity.class);
            //Set the action for the pending intent.
            notifyIntent.setAction(Intent.ACTION_SEND);
            // Sets the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Creates the PendingIntent
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            // Puts the PendingIntent into the notification builder
            builder.setContentIntent(notifyPendingIntent);
            // Notifications are issued by sending them to the
            // NotificationManager system service.
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds an anonymous Notification object from the builder, and
            // passes it to the NotificationManager
            mNotificationManager.notify(id, builder.build());

            new CouponRequestTask(getApplicationContext()).execute();
        }

        private Boolean checkTimeInterval(){
//            Forcing all notifications to be delivered.
//            if(System.currentTimeMillis() - Utils.getSharedPrefsLong(Utils.getSharedPrefs(getApplicationContext()),Utils.systemTimeMillis) >  86400000)
//                return true;
//            else
//                return false;
            return true;
        }

    private class CouponRequestTask extends AsyncTask<Void, Void, String> {

        private Context context;

        public CouponRequestTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(Void... voids){

            HttpsURLConnection urlConnection=null;
            String resp="";
            SSLHelper sslHelper = SSLHelper.getInstance(context);

            // pulls down coupon info
            try {
                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL("https://67.205.172.180/wp-json/wc/v2/coupons");
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setSSLSocketFactory(sslHelper.getSSLContext().getSocketFactory());

                // credentials
                String user = "ck_970486c965566c58a881f7501f244bc77bebd732";
                String pass = "cs_070469e8a858dd957792d25fe51ec8234aedabbf";
                String auth = user + ":" + pass;
                byte [] encoded  = Base64.encode(auth.getBytes(), Base64.NO_WRAP);
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

                int x;
                String name;
                String id;
                String disc;
                String desc;

                for(x=0;x<jsonArray.length();x++){
                    JSONObject temp = jsonArray.getJSONObject(x);
                    id = temp.getString("code");
                    disc = temp.getString("amount");
                    desc = temp.getString("description");

                    couponList.add(new Coupon(Jsoup.parse(id).text(), Double.parseDouble(disc), Jsoup.parse(desc).text()));
                }
                getRandomCoupon();

                Log.i(">>>",""+selectedCoupon.getDescription());

                //Store to shared preferences.
                sharedPreferences = Utils.getSharedPrefs(getApplicationContext());
                Utils.insertSharedPrefs(Utils.couponCode, selectedCoupon.getCode(), sharedPreferences);
                Utils.insertSharedPrefs(Utils.couponDesc, selectedCoupon.getDescription(), sharedPreferences);
                Utils.insertSharedPrefs(Utils.couponAmountOff, selectedCoupon.getAmountOff().toString(), sharedPreferences);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private Coupon getRandomCoupon(){
        selectedCoupon=null;

        // get a randomly select coupon from the list of coupons
        Random random = new Random();
        selectedCoupon = couponList.get(random.nextInt(couponList.size()));

        return selectedCoupon;
    }


}
