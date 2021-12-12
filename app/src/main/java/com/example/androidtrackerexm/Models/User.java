package com.example.androidtrackerexm.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.rxjava3.annotations.Nullable;


@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(index = true)
    public  String email;

    public String password;

    @Nullable
    public byte[] avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }




}
