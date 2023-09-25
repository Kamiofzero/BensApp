package com.ljb.downloadx;

import static com.ljb.downloadx.DownloadTask.STATUS_PREPARED;

import android.content.Context;

import com.ljb.downloadx.database.DataBaseManager;
import com.ljb.downloadx.util.DownloadLog;

import java.util.HashMap;
import java.util.List;

public class DownloadX {
    private final static String TAG = "DownloadX";

    private static DownloadX downloadX;

    TaskManager taskManager;

    DataBaseManager dataBaseManager;

    HashMap<String, DownloadTask> taskMap;

    private DownloadX() {
        taskManager = new TaskManager();
        dataBaseManager = DataBaseManager.getInstance();
        taskMap = new HashMap<>();
    }

    public static DownloadX getInstance() {
        synchronized (DownloadX.class) {
            if (downloadX == null) downloadX = new DownloadX();
        }
        return downloadX;
    }

    public void init(Context context) {
        Const.context = context;
        dataBaseManager.init(context);
    }

    public void loadData() {
        List<DownloadInfo> downloadInfoList = dataBaseManager.query();
        DownloadLog.i(TAG, "load " + downloadInfoList.size() + " items");
        for (DownloadInfo info : downloadInfoList) {
            DownloadTask task = new DownloadTask(info);
            task.setCallback(downloadCallback);
            taskMap.put(info.url, task);
            DownloadLog.i(TAG, info.toString());
        }
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
            dataBaseManager.insert(DownloadInfo.copyFromTask(task));
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
            dataBaseManager.delete(DownloadInfo.copyFromTask(task));
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
            if (info.status == STATUS_PREPARED) dataBaseManager.update(info);
            if (taskProgressListener != null) taskProgressListener.onTaskProgress(info);
        }
    };

    TaskProgressListener taskProgressListener;

    public void setTaskProgressListener(TaskProgressListener taskProgressListener) {
        this.taskProgressListener = taskProgressListener;
    }
}
