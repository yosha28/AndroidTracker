package com.example.androidtrackerexm.models;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public User()
    {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }




}
