package com.ljb.bens.beans

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ljb.bens.db.AppInfoDao

@Database(entities = [AppInfo::class], version = 1)
abstract class AppInfoBase : RoomDatabase() {
    abstract fun getAppInfoDao(): AppInfoDao

}