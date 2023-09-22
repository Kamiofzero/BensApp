package com.ljb.bens.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ljb.bens.beans.AppInfo

@Dao
interface AppInfoDao {

    @Query("SELECT * FROM AppInfo")
    fun getAll(): List<AppInfo>?

    @Query("SELECT * FROM AppInfo WHERE appName LIKE :appName")
    fun getByName(appName: String): AppInfo

    @Insert
    fun insert(bean: AppInfo)

    @Insert
    fun insertAll(vararg bean: AppInfo)

    @Insert
    fun insertAll(beans: List<AppInfo>)

    @Delete
    fun delete(bean: AppInfo)

    @Delete
    fun deleteAll(vararg bean: AppInfo)


    @Update
    fun update(bean: AppInfo)

}