package com.example.androidtrackerexm.DaoModels;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidtrackerexm.Models.AuthUser;
import com.example.androidtrackerexm.Models.User;
@Dao
public interface AuthUserDao {

    @NonNull
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(@NonNull AuthUser user);


  /*  @Query("SELECT userEMail FROM authUser ")
    String getAuthUserMail();*/

    @Query("SELECT idUser FROM authUser WHERE id=(SELECT max(id) FROM authUser)")
  long getIdAuthUser();


    @Query("DELETE FROM authUser")
   void deleteAll();
}
