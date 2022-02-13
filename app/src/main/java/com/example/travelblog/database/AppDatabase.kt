package com.example.travelblog.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travelblog.http.Blog

@Database(entities = [Blog::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun blogDao():BlogDAO
}