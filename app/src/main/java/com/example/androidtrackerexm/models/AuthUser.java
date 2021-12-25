package com.example.androidtrackerexm.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "authUser")
public class AuthUser {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long idUser;

}
