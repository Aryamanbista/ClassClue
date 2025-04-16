package com.example.classclue;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}