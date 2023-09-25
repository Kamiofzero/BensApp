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
    suspend fun getAll(): List<AppInfo>?

    @Query("SELECT * FROM AppInfo WHERE appName LIKE :appName")
    suspend fun getByName(appName: String): AppInfo

    @Insert
    suspend fun insert(bean: AppInfo)

    @Insert
    suspend fun insertAll(vararg bean: AppInfo)

    @Insert
    suspend fun insertAll(beans: List<AppInfo>)

    @Delete
    suspend fun delete(bean: AppInfo)

    @Delete
    suspend fun deleteAll(vararg bean: AppInfo)


    @Update
    suspend fun update(bean: AppInfo)

}