package com.example.androidtrackerexm.DaoModels;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidtrackerexm.Models.Point;

import java.util.List;
@Dao
public interface PointDao {

  /*  @NonNull
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(@NonNull List<Point> pointList);*/

    @NonNull
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(@NonNull Point point);

    @NonNull
    @Delete
    int  delete(@NonNull Point point);

    @NonNull
    @Update
    int update(@NonNull Point point);

    @Query("SELECT * FROM points")
    List<Point> getAllPoints();


    @Query("SELECT * FROM points WHERE  day LIKE :day AND  userId LIKE :id")
    List<Point> getAllPointsByDate(String day,long id);


  /*  @Query("SELECT * FROM points WHERE (:day IS NULL OR day LIKE :day) " +
            "AND (userId IS NULL OR userId LIKE :id) ")
    List<Point> getAllPointsByDate(String day,long id);*/

    @Query("SELECT * FROM points WHERE day LIKE :day  ")
    List<Point> getPointsByDate(String day);

}
