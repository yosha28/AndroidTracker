package com.example.androidtrackerexm.Models;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.androidtrackerexm.DaoModels.AuthUserDao;
import com.example.androidtrackerexm.DaoModels.PointDao;
import com.example.androidtrackerexm.DaoModels.UserDao;

@SuppressLint("RestrictedApi")
@Database(entities = {User.class, Point.class,AuthUser.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    @NonNull
    public abstract UserDao getUserDao();

    @NonNull
    public abstract PointDao getPointDao();

    @NonNull
    public abstract AuthUserDao getAuthUserDao();


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("db-migrate", "success");
            database.execSQL("ALTER TABLE users ADD COLUMN phone TEXT");
        }
    };


}
