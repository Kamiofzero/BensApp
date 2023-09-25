package com.ljb.downloadx;

import android.text.TextUtils;

import com.ljb.downloadx.util.DownloadLog;
import com.ljb.downloadx.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends Task {


    private final static String TAG = "DownloadTask";

    public String url;

    public String fileName;
    public long fileSize;
    public long fileDownloadSize;
    public int fileDownloadPercent;

    public String fileDownloadPath;

    public int status;

    long createTime;

    long completeTime;

    public static final int STATUS_IDLE = 0;
    public static final int STATUS_WAITING = 1;
    public static final int STATUS_PREPARING = 2;
    public static final int STATUS_PREPARED = 3;
    public static final int STATUS_DOWNLOADING = 4;
    public static final int STATUS_STOPPING = 5;
    public static final int STATUS_STOPPED = 6;
    public static final int STATUS_DOWNLOADED = 7;
    public static final int STATUS_ERROR = 8;
    public static final int STATUS_CANCEL = 9;
    public static final int STATUS_CANCELED = 10;


    String storagePath = Const.context.getExternalFilesDir(null) + "/DownloadTask";

    public DownloadTask() {

    }

    Call call;

    public DownloadTask(DownloadInfo info) {
        super();
        this.url = info.getUrl();
        this.fileName = info.getFileName();
        this.fileSize = info.getFileSize();
        this.fileDownloadSize = info.getFileDownloadSize();
        this.fileDownloadPercent = info.getFileDownloadPercent();
        this.fileDownloadPath = info.getFileDownloadPath();
        this.status = info.getStatus();
        this.createTime = info.getCreateTime();
        this.completeTime = info.getCompleteTime();
    }


    private Response networkRequest() {
        status = STATUS_PREPARING;
        callback();
        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url).get();
        if (fileDownloadSize > 0) {
            builder.addHeader("RANGE", "bytes=" + fileDownloadSize + "-"/* + fileSize*/);
            DownloadLog.i(TAG, "task[" + url + "] continue download from " + fileDownloadSize + " bytes");
        } else {
            DownloadLog.i(TAG, "task[" + url + "] start download");
        }

        //创建Request
        Request request = builder.build();
        //创建Call对象
        call = client.newCall(request);
        //通过execute()方法获得请求响应的Response对象
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            DownloadLog.i(TAG, "task[" + url + "] network request fail");
            status = STATUS_ERROR;
            callback();
            return null;
        }


        if (response == null || !response.isSuccessful()) {
            DownloadLog.i(TAG, "task[" + url + "] network request fail");
            status = STATUS_ERROR;
            callback();
            return null;
        }

        DownloadLog.i(TAG, "task[" + url + "] network request success");

        if (fileSize == 0) {
            String fileLengthStr = response.header("Content-Length");
            fileSize = new Long(fileLengthStr);
            DownloadLog.i(TAG, "file size : " + fileSize);
        }

        if (TextUtils.isEmpty(fileName)) {
            String fileName = response.header("Content-Disposition");
            if (fileName == null || fileName.length() < 1) {
                // 通过截取URL来获取文件名
                URL downloadUrl = null;
                try {
                    downloadUrl = new URL(url);
                    // 获得实际下载文件的URL
                    fileName = downloadUrl.getFile();
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    fileName = URLDecoder.decode(fileName.substring(fileName.indexOf("filename=") + 9), "UTF-8");
                    // 存在文件名会被包含在""里面，所以要去掉，否则读取异常
                    fileName = fileName.replaceAll("\"", "");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            this.fileName = fileName;
            fileDownloadPath = storagePath + "/" + fileName;
            DownloadLog.i(TAG, "file fileName : " + fileName);
            DownloadLog.i(TAG, "storage path : " + storagePath);
            status = STATUS_PREPARED;
            callback();
//            DataBaseManager.getInstance().update(downloadInfo);
        }
        return response;
    }

    private void download(Response response) {
        DownloadLog.i(TAG, "task[" + url + "] download start");
        status = STATUS_DOWNLOADING;
        callback();
        long progressSize = fileDownloadSize;
        RandomAccessFile raf = null;
        InputStream is = null;

        try {
            is = response.body().byteStream();
            File folder = new File(storagePath);
            if (!folder.exists()) folder.mkdirs();
//            FileOutputStream fos = new FileOutputStream(new File(storagePath));
            raf = new RandomAccessFile(new File(fileDownloadPath), "rw");
            byte[] buff = new byte[5120];
            int len;
            int progressPercent = 0;
            raf.seek(progressSize);
            while ((len = is.read(buff)) != -1) {
                raf.write(buff, 0, len);
                progressSize += len;
                fileDownloadSize = progressSize;
                progressPercent = (int) (((float) progressSize / (float) fileSize) * 100);
                if (progressPercent != fileDownloadPercent) {
                    fileDownloadPercent = progressPercent;
                    callback();
                }

                if (status == STATUS_STOPPING || status == STATUS_CANCEL) {
                    break;
                }
//                Thread.sleep(500);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            DownloadLog.i(TAG, "task[" + url + "] download fail");
            status = STATUS_ERROR;
            callback();
            return;
        } finally {
            try {
                if (is != null) is.close();
                if (raf != null) raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (status == STATUS_STOPPING) {
            status = STATUS_STOPPED;
            DownloadLog.i(TAG, "task[" + url + "] stopped");
        } else if (status == STATUS_CANCEL) {
            status = STATUS_CANCELED;
            FileUtil.deleteFile(fileDownloadPath);
            DownloadLog.i(TAG, "task[" + url + "] canceled");
        } else if (fileDownloadSize == fileSize) {
            status = STATUS_DOWNLOADED;
            DownloadLog.i(TAG, "task[" + url + "] downloaded");
        }
        callback();
    }


    @Override
    void doTask() {
        Response response = networkRequest();
        if (response != null) download(response);
    }


    public void stop() {
        if (status <= STATUS_DOWNLOADING) {
            status = STATUS_STOPPING;
            DownloadLog.i(TAG, "task[" + url + "] stopping");
        }
    }

    public void cancel() {
        if (status < STATUS_DOWNLOADED) {
            status = STATUS_CANCEL;
            DownloadLog.i(TAG, "task[" + url + "] canceling");
        }
    }

    public void waitFor() {
        status = STATUS_WAITING;
        DownloadLog.i(TAG, "task[" + url + "] waiting");
        callback();
    }

    private void callback() {
        callback.onStateChanged(DownloadInfo.copyFromTask(this));
    }

    DownloadCallback callback;

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }


}
