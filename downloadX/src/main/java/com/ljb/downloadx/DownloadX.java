package com.ljb.downloadx;

import java.util.HashMap;

public class DownloadX {

    private static DownloadX downloadX;

    TaskManager taskManager;

    HashMap<String, DownloadTask> taskMap;

    private DownloadX() {
        taskManager = new TaskManager();
        taskMap = new HashMap<>();
    }

    public static DownloadX getInstance() {
        synchronized (DownloadX.class) {
            if (downloadX == null) downloadX = new DownloadX();
        }
        return downloadX;
    }

    public DownloadInfo getDownloadInfo(String url) {
        DownloadTask task = taskMap.get(url);
        if (task != null) return DownloadInfo.copyFromTask(task);
        return null;
    }

    public void download(String url) {
        if (url == null) return;
        DownloadTask task = taskMap.get(url);
        if (task == null) {
            task = new DownloadTask();
            task.url = url;
            task.setCallback(downloadCallback);
            taskMap.put(url, task);
        }
        taskManager.addTask(task);
    }


    public void stopDownload(String url) {
        if (url == null) return;
        DownloadTask task = taskMap.get(url);
        if (task == null) {
            task.stop();
            taskManager.removeTask(task);
        }
    }

    public void cancelDownload(String url) {

    }

    public void downloadAll() {

    }

    public void stopAllDownload() {

    }

    public void cancelAllDownload() {

    }

    DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void onStateChanged(DownloadInfo info) {

        }
    };

}
