package com.ljb.bens.ui.myapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ljb.base.utils.LogUtil
import com.ljb.downloadx.DownloadInfo

class MyAppViewModel : ViewModel() {
    fun test() {
        LogUtil.i("heihei")
    }

    val downloadInfo = MutableLiveData<DownloadInfo>()


}