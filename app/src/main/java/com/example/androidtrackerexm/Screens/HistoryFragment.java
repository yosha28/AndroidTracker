package com.example.androidtrackerexm.Screens;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.Models.AppDataBase;
import com.example.androidtrackerexm.Models.Point;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.databinding.FragmentHistoryBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryFragment extends Fragment implements OnMapReadyCallback {
    AppDataBase db;
    GoogleMap googleMap;
    Calendar dateAndTime = Calendar.getInstance();
    EditText currentDateTime;
    FragmentHistoryBinding binding;
    SupportMapFragment historyFragment;
    Marker locationMarker;
    Polyline polyline1 = null;
    List<LatLng> track;
    public HistoryFragment() {
        super(R.layout.fragment_history);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        binding = FragmentHistoryBinding.bind(view);

        db = App.getInstance().getDatabase();

        getActivity().findViewById(R.id.blockCalendar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.btnCalendar).setVisibility(View.VISIBLE);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        currentDateTime = getActivity().findViewById(R.id.etDate);

        setInitialDateTime();

        getActivity().findViewById(R.id.btnCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        getActivity().findViewById(R.id.btnLeftDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateAndTime.add(Calendar.DAY_OF_YEAR, -1);
                setInitialDateTime();
            }
        });
        getActivity().findViewById(R.id.btnRightDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateAndTime.add(Calendar.DAY_OF_YEAR, +1);
                setInitialDateTime();
            }
        });

    }

    private void setInitialDateTime() {
        currentDateTime.setText(DateUtils.formatDateTime(getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        historyFragment.getMapAsync(this);

    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

        }
    };

    public void setDate(View v) {
        new DatePickerDialog(getActivity(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        String date = DateUtils.formatDateTime(getActivity(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

      googleMap.clear();

        List<Point> listPoints = App.getInstance().getDatabase().getPointDao().getAllPointsByDate(date, db.getAuthUserDao().getIdAuthUser());

        if (listPoints.size() > 0) {
          track = new ArrayList<LatLng>();
            for (Point point : listPoints) {
                track.add(new LatLng(point.latitude, point.longitude));
            }

            polyline1 = googleMap.addPolyline(new PolylineOptions()
                  .color(R.color.purple_500)
                    .width(20)
                    .addAll(track));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(track.get(0).latitude, track.get(0).longitude), 12);

            setMarkerOptions(0,120);
            setMarkerOptions(0,240);

            googleMap.animateCamera(cameraUpdate);
        }

    }
    public void setMarkerOptions(int num,float color)
    {
        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(new LatLng(track.get(num).latitude,track.get(num).longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(	color));

        this.locationMarker = this.googleMap.addMarker(markerOptions2);

    }


}