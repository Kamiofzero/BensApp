package com.ljb.bens.beans;

import static com.ljb.downloadx.DownloadTask.STATUS_IDLE;

import com.ljb.base.adapter.BeanKey;

public class AppInfo implements BeanKey {

    public String key;

    public String appName;
    public String appDescription;

    public String url;

    public String storagePath;

    public int downloadPercent;

    public int status;

    public AppInfo(String appName, String appDescription, String url) {
        this.appName = appName;
        this.appDescription = appDescription;
        this.url = url;
        status = STATUS_IDLE;
    }


    @Override
    public String getKey() {
        return url;
    }
}
