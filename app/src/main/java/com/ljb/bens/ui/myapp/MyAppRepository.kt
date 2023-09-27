package com.ljb.bens.ui.myapp

import com.ljb.base.utils.LogUtil
import com.ljb.bens.BensApp
import com.ljb.bens.beans.AppInfo
import com.ljb.downloadx.DownloadInfo
import com.ljb.downloadx.DownloadTask.STATUS_IDLE
import com.ljb.downloadx.DownloadTask.STATUS_PREPARED
import com.ljb.downloadx.DownloadX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyAppRepository {
    private val TAG: String = "MyAppRepository"
    private var appList = arrayListOf<AppInfo>()

    private var downloadX: DownloadX = DownloadX.getInstance()

    private var callback: Callback? = null

    private var dao = BensApp.appInfoDao

    private var initData: Boolean = false;

    companion object {

        val instance: MyAppRepository by lazy { MyAppRepository() }

    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {
        downloadX.setTaskProgressListener {
            LogUtil.i(TAG, "onTaskProgressChanged $it")
            for ((i, temp) in appList.withIndex()) {
                if (temp.url.equals(it.url)) {
                    temp.downloadPercent = it.fileDownloadPercent
                    temp.status = it.status
                    if (it.status == STATUS_PREPARED) {
                        temp.apkSize = it.fileSize
                        temp.storagePath = it.fileDownloadPath
                        GlobalScope.launch {
                            LogUtil.i(TAG, "update item $temp")
                            dao.update(temp)
                        }
                    }
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
        if (!initData) {
            initData = true
            GlobalScope.launch {
                LogUtil.i(TAG, Thread.currentThread().name)
                var list = dao.getAll()
                LogUtil.i(TAG, "load app list: ${list?.size ?: 0} item loaded\n")
                if (list?.isNotEmpty() == true) {
                    list.forEach {
                        var info: DownloadInfo? = downloadX.getDownloadInfo(it.url)
                        if (info != null) {
                            it.apkSize = info.getFileSize()
                            it.apkDownloadSize = info.getFileDownloadSize()
                            it.downloadPercent = info.getFileDownloadPercent()
                            it.storagePath = info.getFileDownloadPath()
                            it.status = info.getStatus()
                        } else {
                            it.status = STATUS_IDLE
                        }
//                        if (it.apkSize == 0L) {
//                            it.status = STATUS_IDLE
//                        } else {
//                            it.apkDownloadSize = ApkUtil.getApkSize(it.storagePath)
//                            it.downloadPercent =
//                                ((it.apkDownloadSize.toDouble()) / (it.apkSize.toDouble()) * 100).toInt()
//                            if (it.apkDownloadSize == it.apkSize) {
//                                it.status = STATUS_DOWNLOADED
//                            } else if (it.apkDownloadSize > 0) {
//                                it.status = STATUS_STOPPED
//                            } else {
//                                it.status = STATUS_IDLE
//                            }
//                        }
                        LogUtil.i(TAG, it.toString())
                    }
                    appList.addAll(list)
                } else {
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
                    appList.add(
                        AppInfo(
                            "via", "简洁浏览器", "https://res.viayoo.com/v1/via-release-cn.apk"
                        )
                    )
                    dao.insertAll(appList)
                }
                callback?.onDataLoaded(appList)
            }
        } else callback?.onDataLoaded(appList)
    }

    fun getApp(index: Int): AppInfo {
        return appList[index]
    }


    interface Callback {
        fun onDataLoaded(appList: List<AppInfo>)

        fun onAppDownloadStatusChanged(appInfo: AppInfo, index: Int)

    }

}