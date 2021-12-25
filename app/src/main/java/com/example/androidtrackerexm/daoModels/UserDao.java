package com.example.androidtrackerexm.daoModels;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidtrackerexm.models.User;

@Dao
public interface UserDao {

    @NonNull
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(@NonNull User user);

    @NonNull
    @Delete
    int  deleteAll(@NonNull User user);

    @NonNull
    @Update
    int update(@NonNull User user);

    @Query("SELECT password FROM users WHERE email LIKE :email")
   String getPassword( @NonNull String email);

    @Query("SELECT id FROM users WHERE email LIKE :email")
    long getId( @NonNull String email);

    @NonNull
    @Query("SELECT * FROM users WHERE id LIKE :id")
    User getAuthUser(long id);

}
