package com.example.androidtrackerexm.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "authUser")
public class AuthUser {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long idUser;

}
