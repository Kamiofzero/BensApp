package com.ljb.bens

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ljb.base.utils.LogUtil
import com.ljb.bens.beans.AppInfoBase
import com.ljb.bens.db.AppInfoDao
import com.ljb.downloadx.DownloadX

class BensApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.setDebug(true)
        DownloadX.init(context)
        appInfoDao =
            Room.databaseBuilder(context, AppInfoBase::class.java, "AppInfoDb").build().getAppInfoDao()
    }

    companion object {
        lateinit var context: Context

        lateinit var appInfoDao: AppInfoDao
    }
}