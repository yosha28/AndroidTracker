package com.example.androidtrackerexm.screens.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.locationHelpers.LocationManager;
import com.example.androidtrackerexm.locationHelpers.LocationService;
import com.example.androidtrackerexm.MainActivity;
import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.Point;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.databinding.FragmentMapBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;


public class MapFragment extends Fragment
        implements OnMapReadyCallback {

    public static final String TAG = MainActivity.class.getCanonicalName();
FragmentMapBinding binding;

    GoogleMap googleMap;
    Marker locationMarker;
    Circle circle;
    LocationManager locationManager;

    LocationCallback locationCallback;
    AppDataBase db;
    int testPoint = 0;
    View headerNav;
    TextView textViewNav;
    ImageView imageView;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = App.getInstance().getDatabase();
        startLocationService();
      //  setProfileNavMenu();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        binding = FragmentMapBinding.bind(view);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.blockCalendar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.btnCalendar).setVisibility(View.GONE);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this::onMapReady);
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateLocationMarker(locationResult.getLastLocation());
            }
        };

        locationManager = new LocationManager(locationCallback,
                getActivity().getApplicationContext());

        this.locationManager.enable();
        return view;
    }


    public void updateLocationMarker(Location location) {
        Log.d(TAG, location.toString());

        if (this.googleMap == null) {
            return;
        }

        if (this.circle == null) {
           circle = googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(location.getLatitude(), location.getLongitude()))
                    .radius(100)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.RED));


        } else {
            circle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));

            saveLocationPoints(circle);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()),
                14
        );
        googleMap.animateCamera(cameraUpdate);
    }

    public void saveLocationPoints(Circle circle) {

        Point point = new Point();
        //для эмулятора тест трека
        //   testPoint += 1;
        point.latitude =circle.getCenter().latitude;

        point.longitude = circle.getCenter().longitude;

        String date = DateUtils.formatDateTime(getActivity(),
                Calendar.getInstance().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        point.day = date;
        point.userId = db.getAuthUserDao().getIdAuthUser();

        db.getPointDao().insert(point);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startLocationService() {
        if (isLocationPermissionAvailable()) {
            this.locationManager.enable();
            Context context = getActivity().getApplicationContext();
            Intent intent = new Intent(context, LocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION

                }, 1);
            } else {
                this.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
            }

        }


    }

    public boolean isLocationPermissionAvailable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
            //  && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        } else {
            return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && isLocationPermissionAvailable()) {
            startLocationService();
        }
    }

    public void setProfileNavMenu() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        headerNav = navigationView.getHeaderView(0);
     //   imageView = headerNav.findViewById(R.id.imageView);
        textViewNav = headerNav.findViewById(R.id.tvNHMail);

        User tmpUser = db.getUserDao().getAuthUser(db.getAuthUserDao().getIdAuthUser());
        //   User tmpUser=App.getInstance().getUser();
     /*   if (tmpUser.avatar != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(tmpUser.avatar, 0, tmpUser.avatar.length));
        else {

        }*/
        textViewNav.setText(tmpUser.email);
    }

}