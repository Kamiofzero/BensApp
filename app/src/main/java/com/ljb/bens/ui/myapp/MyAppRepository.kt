package com.ljb.bens.ui.myapp

import com.ljb.base.utils.LogUtil
import com.ljb.bens.beans.AppInfo
import com.ljb.downloadx.DownloadX

class MyAppRepository {

    private var appList = arrayListOf<AppInfo>()

    private var downloadX: DownloadX = DownloadX.getInstance()

    private var callback: Callback? = null

    companion object {

        val instance: MyAppRepository by lazy { MyAppRepository() }

//        private lateinit var instance: MyAppRepository
//
//        fun getInstance(): MyAppRepository {
//            @Synchronized if (instance == null) instance = MyAppRepository()
//            return instance
//        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {
        downloadX.setTaskProgressListener {
            LogUtil.i(it.toString())
            for ((i, temp) in appList.withIndex()) {
                if (temp.url.equals(it.url)) {
                    temp.downloadPercent = it.fileDownloadPercent
                    temp.status = it.status
                    temp.storagePath = it.fileDownloadPath
                    callback?.onAppDownloadStatusChanged(temp, i)
                }
            }
        }
    }

    fun getAppList(): List<AppInfo> {
        return appList
    }


    fun download(url: String) {
        downloadX.download(url)
    }

    fun stopDownload(url: String) {
        downloadX.stopDownload(url)
    }

    fun cancelDownload(url: String) {
        downloadX.cancelDownload(url)
    }

    fun downloadAll() {}

    fun stopDownloadAll() {}


    fun loadAppList() {
        //TODO 本地无数据

        appList.add(
            AppInfo(
                "bilibili",
                "视频弹幕网站",
                "https://dl.hdslb.com/mobile/latest/android64/iBiliPlayer-bili.apk?t=1652253615000&amp;spm_id_from=333.47.b_646f776e6c6f61642d6c696e6b.1"

            )
        )
        appList.add(
            AppInfo(
                "QQ",
                "聊天软件",
                "https://downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.8.88.7830_537117916_HB.64.apk"

            )
        )
        appList.add(AppInfo("via", "简洁浏览器", "https://res.viayoo.com/v1/via-release-cn.apk"))
        callback?.onDataLoaded(appList)
    }


    interface Callback {
        fun onDataLoaded(appList: List<AppInfo>)

        fun onAppDownloadStatusChanged(appInfo: AppInfo, index: Int)

    }

}