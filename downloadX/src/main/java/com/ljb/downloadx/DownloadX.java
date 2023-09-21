package com.ljb.downloadx;

import android.content.Context;

import com.ljb.downloadx.util.DownloadLog;

import java.util.HashMap;

public class DownloadX {
    private final static String TAG = "DownloadX";

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

    public static void init(Context context) {
        Const.context = context;
    }

    public DownloadInfo getDownloadInfo(String url) {
        DownloadTask task = taskMap.get(url);
        if (task != null) return DownloadInfo.copyFromTask(task);
        return null;
    }

    public void download(String url) {
        if (url == null) return;
        DownloadLog.i(TAG, "download url[" + url + "]");
        DownloadTask task = taskMap.get(url);
        if (task == null) {
            task = new DownloadTask();
            task.url = url;
            task.setCallback(downloadCallback);
            taskMap.put(url, task);
            DownloadLog.i(TAG, "create task[" + url + "]");
        }
        if (taskManager.isFullTask()) task.waitFor();
        taskManager.addTask(task);
    }


    public void stopDownload(String url) {
        if (url == null) return;
        DownloadLog.i(TAG, "stopDownload url[" + url + "]");
        DownloadTask task = taskMap.get(url);
        if (task != null) {
            task.stop();
            taskManager.removeTask(task);
        }
    }

    public void cancelDownload(String url) {
        if (url == null) return;
        DownloadLog.i(TAG, "cancelDownload url[" + url + "]");
        DownloadTask task = taskMap.get(url);
        if (task != null) {
            task.cancel();
            taskManager.removeTask(task);
            taskMap.remove(task);
        }
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

            if (taskProgressListener != null) taskProgressListener.onTaskProgress(info);
        }
    };

    TaskProgressListener taskProgressListener;

    public void setTaskProgressListener(TaskProgressListener taskProgressListener) {
        this.taskProgressListener = taskProgressListener;
    }
}
