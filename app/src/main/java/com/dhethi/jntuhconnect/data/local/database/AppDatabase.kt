package com.dhethi.jntuhconnect.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhethi.jntuhconnect.data.local.dao.StudentDetailsDao
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity

@Database(entities = [StudentDetailsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDetailsDao(): StudentDetailsDao
}
