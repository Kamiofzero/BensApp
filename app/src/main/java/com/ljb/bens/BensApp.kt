package com.ljb.bens

import android.app.Application
import android.content.Context
import com.ljb.base.utils.LogUtil
import com.ljb.downloadx.DownloadX

class BensApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.setDebug(true)
        DownloadX.init(context)
    }

    companion object {
        lateinit var context: Context
    }
}