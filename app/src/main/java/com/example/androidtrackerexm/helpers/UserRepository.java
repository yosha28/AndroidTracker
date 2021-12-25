package com.example.androidtrackerexm.helpers;

import android.annotation.SuppressLint;

import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.AuthUser;
import com.example.androidtrackerexm.models.User;

public class UserRepository {
    private static UserRepository instance;

    AppDataBase db;

    @SuppressLint("UnsafeOptInUsageError")
    private UserRepository(AppDataBase databaseContext) {
        this.db = databaseContext;
    }

    public static UserRepository getInstance(AppDataBase databaseContext) {

        if (instance == null) instance = new UserRepository(databaseContext);
        return instance;
    }

    public void addUser(User user) {
        db.getUserDao().insert(user);
    }

    public User getUser(int id) {
        return db.getUserDao().getAuthUser(id);
    }

    public void updateUser(User user) {
        db.getUserDao().update(user);
    }

    public void clearAuthUser() {
        db.getAuthUserDao().deleteAll();
    }
//db.getUserDao().getAuthUser(authUser.idUser);
    public User getAuthUser()
    {
        return db.getUserDao().getAuthUser(db.getAuthUserDao().getIdAuthUser());
    }
    public String getExistingPass(String mail)
    {
       return db.getUserDao().getPassword(mail);
    }
    public void addAuthUser(AuthUser authUser)
    {
        db.getAuthUserDao().insert(authUser);
    }
    public Long getIdUserForMail(String mail)
    {
      return  db.getUserDao().getId(mail);
    }


}
