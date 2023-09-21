package com.ljb.bens.ui.myapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ljb.bens.beans.AppInfo
import com.ljb.bens.beans.AppInfoPkg

class MyAppViewModel : ViewModel() {

    val appInfoData = MutableLiveData<AppInfoPkg>()
    val appListData = MutableLiveData<List<AppInfo>>()

    private var myAppRepository: MyAppRepository = MyAppRepository.instance


    init {
        myAppRepository.setCallback(object : MyAppRepository.Callback {
            override fun onDataLoaded(appList: List<AppInfo>) {
                appListData.postValue(appList)
            }

            override fun onAppDownloadStatusChanged(appInfo: AppInfo, index: Int) {
                appInfoData.postValue(AppInfoPkg(appInfo, index))
            }

        })
    }

    fun download(url: String) {
        myAppRepository.download(url)
    }

    fun stopDownload(url: String) {
        myAppRepository.stopDownload(url)
    }

    fun cancelDownload(url: String) {
        myAppRepository.cancelDownload(url)
    }

    fun downloadAll() {}

    fun stopDownloadAll() {}


    fun getData() {
        myAppRepository.loadAppList()
    }

    fun launch(appName: String) {

    }


}