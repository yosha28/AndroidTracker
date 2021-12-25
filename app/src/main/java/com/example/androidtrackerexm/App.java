package com.example.androidtrackerexm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.room.Room;


import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.helpers.UserRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class App extends Application {

    public static App instance=null;
    public static AppDataBase database;
    UserRepository userRepository;
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
        userRepository = UserRepository.getInstance(database);
     //  user = database.getUserDao().getAuthUser(database.getAuthUserDao().getIdAuthUser());

    }
    public static App getInstance() {
        return instance;
    }

    public  AppDataBase getDatabase() {
        return database;
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }
   /* @Nullable
    public User getUser() {
        return user;
    }*/
}
