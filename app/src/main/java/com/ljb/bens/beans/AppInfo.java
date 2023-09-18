package com.ljb.bens.beans;

public class AppInfo {

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
    }
}
