package com.example.shivr.e_commerce;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.R.attr.id;

/**
 * Created by shivr on 25/04/2017.
 */

public class GeoFenceIntentService extends IntentService{

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
            notifyIntent.setAction(String.valueOf(R.string.notification_coupon));
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
        }

        private Boolean checkTimeInterval(){
//            Forcing all notifications to be delivered.
//            if(System.currentTimeMillis() - Utils.getSharedPrefsLong(Utils.getSharedPrefs(getApplicationContext()),Utils.systemTimeMillis) >  86400000)
//                return true;
//            else
//                return false;
            return true;
        }


}
