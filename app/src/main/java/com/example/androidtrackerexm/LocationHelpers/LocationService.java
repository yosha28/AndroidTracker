package com.example.androidtrackerexm.LocationHelpers;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.Screens.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    public final static String TAG = LocationService.class.getCanonicalName();
    private FusedLocationProviderClient locationProviderClient;
    private LocationCallback locationCallback;

    public static final Integer BACKGROUND_RATIONALE_CHANGE_ID=147316723;


    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                } else {
                    for (Location location : locationResult.getLocations()) {
                        App.instance.locationPublishSubject.
                               onNext(location);
                    }
                }
            }
        };

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      startForeground(BACKGROUND_RATIONALE_CHANGE_ID,
               showFileSyncNotification(intent.getStringExtra("msg")));

       return super.onStartCommand(intent, flags, startId);

    }
   private Notification showFileSyncNotification(String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                getFileSyncChanel())
               // .setSmallIcon(R.drawable.ic_message)
              //  .setContentTitle("File is syncing")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        return builder.build();
    }
    private String getFileSyncChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("sync_channel_id",
                    "Allow sync file in background",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        return "sync_channel_id";
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationProviderClient.removeLocationUpdates(locationCallback);
    }
}