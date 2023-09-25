package com.ljb.downloadx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.ljb.downloadx.DownloadInfo;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseManager {

    private static DataBaseManager manager;

    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;


    private DownloadFileDao downloadFileDao;
    private Lock mLock = new ReentrantLock();

    private DataBaseManager() {
    }


    public static DataBaseManager getInstance() {
        synchronized (DataBaseManager.class) {
            if (manager == null)
                manager = new DataBaseManager();
        }
        return manager;
    }

    public void init(Context context) {
        downloadFileDao = new DownloadFileDao(context);
    }

    public void insert(DownloadInfo info) {

        mLock.lock();
        try {
            downloadFileDao.create(info);
        } finally {
            mLock.unlock();
        }
    }


    public void update(DownloadInfo info) {
        mLock.lock();
        try {
            downloadFileDao.update(info);
        } finally {
            mLock.unlock();
        }
    }

    public void delete(DownloadInfo info) {
        mLock.lock();
        try {
            downloadFileDao.delete(info.getId());
        } finally {
            mLock.unlock();
        }
    }


    public List<DownloadInfo> query() {
        List<DownloadInfo> list;
        mLock.lock();
        try {
            list = downloadFileDao.getAll();
        } finally {
            mLock.unlock();
        }
        return list;
    }
}
