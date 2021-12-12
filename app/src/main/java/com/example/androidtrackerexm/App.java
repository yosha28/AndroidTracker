package com.example.androidtrackerexm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.room.Room;


import com.example.androidtrackerexm.Models.AppDataBase;
import com.example.androidtrackerexm.Models.User;
import com.example.androidtrackerexm.Screens.MapFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class App extends Application {

    public static App instance=null;
    public static AppDataBase database;
    @Nullable
    private User user;
    public PublishSubject<Location> locationPublishSubject;

    @SuppressLint({"LogConditional", "UnsafeOptInUsageError"})
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        locationPublishSubject = PublishSubject.create();

        database = Room.databaseBuilder(App.this, AppDataBase.class, "database")
                .setAutoCloseTimeout(1, TimeUnit.HOURS)
                .allowMainThreadQueries()
                .build();

       user = database.getUserDao().getAuthUser(database.getAuthUserDao().getIdAuthUser());

    }
    public static App getInstance() {
        return instance;
    }

    public  AppDataBase getDatabase() {
        return database;
    }

    @Nullable
    public User getUser() {
        return user;
    }
}
