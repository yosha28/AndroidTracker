package com.example.androidtrackerexm.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

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
