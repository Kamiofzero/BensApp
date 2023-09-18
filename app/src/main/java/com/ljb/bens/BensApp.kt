package com.ljb.bens

import android.app.Application
import android.content.Context
import com.ljb.base.utils.LogUtil

class BensApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.setDebug(true)
    }

    companion object {
        lateinit var context: Context
    }
}