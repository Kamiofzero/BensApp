package com.ljb.bens.ui.myapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ljb.bens.beans.AppInfo
import com.ljb.downloadx.DownloadInfo
import com.ljb.downloadx.DownloadX

class MyAppViewModel : ViewModel() {

    private var downloadX: DownloadX = DownloadX.getInstance()
    val downloadInfo = MutableLiveData<DownloadInfo>()
    val downloadList = MutableLiveData<List<AppInfo>>()

    init {
        downloadX.setTaskProgressListener { downloadInfo.value = it }
    }

    fun download(url: String) {

    }

    fun stopDownload(url: String) {

    }

    fun cancelDownload(url: String) {}

    fun downloadAll() {}

    fun stopDownloadAll() {}


    fun getData() {
        var appList = arrayListOf<AppInfo>()

        appList.add(
            AppInfo(
                "bilibili",
                "视频弹幕网站",
                "https://downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.8.88.7830_537117916_HB.64.apk"
            )
        )
        appList.add(
            AppInfo(
                "QQ",
                "聊天软件",
                "https://dl.hdslb.com/mobile/latest/android64/iBiliPlayer-bili.apk?t=1652253615000&amp;spm_id_from=333.47.b_646f776e6c6f61642d6c696e6b.1"
            )
        )
        appList.add(AppInfo("via", "简洁浏览器", "https://res.viayoo.com/v1/via-release-cn.apk"))
        downloadList.value = appList
    }


}