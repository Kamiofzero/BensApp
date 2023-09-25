package com.ljb.downloadx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ljb.downloadx.util.DownloadLog;

public class DownloadFileHelper extends SQLiteOpenHelper {
    private final static String TAG = "DownloadFileHelper";

    static String tableName = "file_download";
    static String dbName = "file_download.db";
    static int version = 1;


    public DownloadFileHelper(Context context) {
        super(context, dbName, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table " + tableName + "("
                + "id varchar,"
                + "url varchar,"
                + "name varchar,"
                + "storagePath varchar,"
                + "fileSize long,"
                + "createTime long,"
                + "completeTime long"
                + ")";
        db.execSQL(create_table);
        DownloadLog.i(TAG,"db create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
