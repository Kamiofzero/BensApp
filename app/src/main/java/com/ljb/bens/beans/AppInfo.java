package com.ljb.bens.beans;

import static com.ljb.downloadx.DownloadTask.STATUS_IDLE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ljb.base.adapter.BeanKey;

@Entity
public class AppInfo implements BeanKey {
    @PrimaryKey
    @NonNull
    public String key;

    public String appName;
    public String appDescription;
    public String url;
    public String storagePath;
    public int downloadPercent;
    public long apkSize;
    public long apkDownloadSize;


    public int status;

    public AppInfo(String appName, String appDescription, String url) {
        this.appName = appName;
        this.appDescription = appDescription;
        this.url = url;
        this.key = url;
        status = STATUS_IDLE;
    }


    @Override
    public String getKey() {
        return key;
    }


    @Override
    public String toString() {
        return "AppInfo{" + "key='" + key + '\'' + ", appName='" + appName + '\'' + ", appDescription='" + appDescription + '\'' + ", url='" + url + '\'' + ", storagePath='" + storagePath + '\'' + ", downloadPercent=" + downloadPercent + ", apkSize=" + apkSize + ", apkDownloadSize=" + apkDownloadSize + ", status=" + status + '}';
    }
}
