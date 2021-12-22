package com.example.androidtrackerexm.helpers;

import android.annotation.SuppressLint;

import com.example.androidtrackerexm.Models.AppDataBase;
import com.example.androidtrackerexm.Models.User;

public class UserRepository {
    private static UserRepository instance;

    AppDataBase db;

    @SuppressLint("UnsafeOptInUsageError")
    private UserRepository(AppDataBase databaseContext){
        this.db = databaseContext;
    }

    public static UserRepository getInstance(AppDataBase databaseContext){

        if(instance == null) instance = new UserRepository(databaseContext);
        return  instance;
    }
    public void addUser(User user) {

        db.getUserDao().insert(user);
    }
    public User getUser(int id) {

        return db.getUserDao().getAuthUser(id);
    }
    public void updateUser(User user){db.getUserDao().update(user);}

public void cleareAuthUser()
{
    db.getAuthUserDao().deleteAll();
}

}
