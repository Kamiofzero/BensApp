package com.ljb.downloadx.database;

import static com.ljb.downloadx.DownloadTask.STATUS_DOWNLOADED;
import static com.ljb.downloadx.DownloadTask.STATUS_IDLE;
import static com.ljb.downloadx.DownloadTask.STATUS_STOPPED;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ljb.downloadx.DownloadInfo;
import com.ljb.downloadx.util.DownloadLog;
import com.ljb.downloadx.util.FileUtil;

import java.io.File;
import java.util.List;

public class DownloadFileDao extends DataBaseDao<DownloadInfo> {

    private final static String TAG = "DownloadFileDao";


    public DownloadFileDao(Context context) {
        super(new DownloadFileHelper(context));
    }

    @Override
    protected String getTableName() {
        return DownloadFileHelper.tableName;
    }


    public DownloadInfo get(String key) {
        String selection = "id=?";
        String[] selectionArgs = new String[]{key};
        List<DownloadInfo> infos = this.get(selection, selectionArgs);
        return infos.size() > 0 ? (DownloadInfo) infos.get(0) : null;
    }

    public void delete(String taskKey) {
        this.delete("id=?", new String[]{taskKey});
    }

    public int update(DownloadInfo downloadInfo) {
        return this.update(downloadInfo, "id=?", new String[]{downloadInfo.id});
    }

    public List<DownloadInfo> getAll() {
        return this.get((String[]) null, (String) null, (String[]) null, (String) null, (String) null, "id ASC", (String) null);
    }

    @Override
    public DownloadInfo parseCursorToBean(Cursor cursor) {
        @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
        @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex("url"));
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
        @SuppressLint("Range") String storagePath = cursor.getString(cursor.getColumnIndex("storagePath"));
        @SuppressLint("Range") long fileSize = cursor.getLong(cursor.getColumnIndex("fileSize"));
        long fileDownloadedSize = 0;
        if (storagePath != null) {//如果下载时请求失败，则无法知道下载文件名，文件未被创建
            File file = new File(storagePath);
//            DownloadLog.i(TAG, "file path: " + storagePath + " , file size: " + fileSize);
            if (file.exists()) {
                fileDownloadedSize = FileUtil.getFileSize(file);
//                DownloadLog.i(TAG, "file downloaded size: " + fileDownloadedSize);
            }
        }

        int downloadPercent = (int) (((float) fileDownloadedSize / (float) fileSize) * 100);
        @SuppressLint("Range") long createTime = cursor.getLong(cursor.getColumnIndex("createTime"));
        @SuppressLint("Range") long completeTime = cursor.getLong(cursor.getColumnIndex("completeTime"));

        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setId(id);
        downloadInfo.setUrl(url);
        downloadInfo.setFileName(name);
        downloadInfo.setFileDownloadPath(storagePath);
        downloadInfo.setFileSize(fileSize);
        downloadInfo.setFileDownloadSize(fileDownloadedSize);
        downloadInfo.setFileDownloadPercent(downloadPercent);

        int state = 0;
        if (fileSize == 0 || fileDownloadedSize == 0) {
            state = STATUS_IDLE;
        } else if (fileDownloadedSize == fileSize) {//如果下载时请求失败，则无法知道文件大小
            state = STATUS_DOWNLOADED;
        } else {
            state = STATUS_STOPPED;
        }

        downloadInfo.setStatus(state);
        downloadInfo.setCreateTime(createTime);
        downloadInfo.setCompleteTime(completeTime);
        return downloadInfo;
    }

    @Override
    public ContentValues getContentValues(DownloadInfo info) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", info.getId());
        contentValues.put("url", info.getUrl());
        contentValues.put("name", info.getFileName());
        contentValues.put("storagePath", info.getFileDownloadPath());
        contentValues.put("fileSize", info.getFileSize());
        contentValues.put("createTime", info.getCreateTime());
        contentValues.put("completeTime", info.getCompleteTime());

        return contentValues;
    }
}
