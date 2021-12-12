package com.example.androidtrackerexm.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

@Entity(tableName = "points", primaryKeys = {"latitude", "longitude"})
public class Point {
    @NonNull
    public Double latitude;
    @NonNull
    public Double longitude;
    @NonNull
    public String day;
    @NonNull
    public long userId;
}
