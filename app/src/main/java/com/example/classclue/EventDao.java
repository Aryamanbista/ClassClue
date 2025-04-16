package com.example.classclue;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    List<Event> getAll();


}