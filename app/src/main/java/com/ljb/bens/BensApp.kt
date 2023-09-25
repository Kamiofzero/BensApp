package com.ljb.bens

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ljb.base.utils.LogUtil
import com.ljb.bens.beans.AppInfoBase
import com.ljb.bens.db.AppInfoDao
import com.ljb.downloadx.DownloadX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BensApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.setDebug(true)
        DownloadX.getInstance().init(context)
        appInfoDao = Room.databaseBuilder(context, AppInfoBase::class.java, "AppInfo.db")
//            .allowMainThreadQueries()//允许主线程中操作数据库
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    LogUtil.i("db create")
                }
            }).build().getAppInfoDao()
        GlobalScope.launch { DownloadX.getInstance().loadData() }
    }

    companion object {
        lateinit var context: Context

        lateinit var appInfoDao: AppInfoDao
    }
}