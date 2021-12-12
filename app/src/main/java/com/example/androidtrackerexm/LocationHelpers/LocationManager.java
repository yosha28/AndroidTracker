package com.example.androidtrackerexm.LocationHelpers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationManager implements DefaultLifecycleObserver {

    private final FusedLocationProviderClient locationProviderClient;
    private final LocationCallback locationCallback;
    private final LocationRequest locationRequest;
    private Context context;
    private boolean enabled = false;

    public LocationManager(LocationCallback locationCallback,
                           Context context

    ) {
        this.locationCallback = locationCallback;
        this.context = context;

        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (enabled) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
      //  this.locationProviderClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates(){
        this.locationProviderClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, null);
    }

    @SuppressLint("MissingPermission")
    public void enable() {
        enabled = true;

       // if(lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {

            this.locationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
      //  }
    }

  /*  public void disable(){
        enabled = false;
        this.locationProviderClient.removeLocationUpdates(locationCallback);
    }*/

   /* @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        this.locationProviderClient.removeLocationUpdates(locationCallback);
    }*/
}

