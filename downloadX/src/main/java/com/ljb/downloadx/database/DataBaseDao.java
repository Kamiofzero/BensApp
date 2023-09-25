package com.ljb.downloadx.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class DataBaseDao<T> {
    private SQLiteOpenHelper helper;

    public DataBaseDao(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    protected final SQLiteDatabase openReader() {
        return this.helper.getReadableDatabase();
    }

    protected final SQLiteDatabase openWriter() {
        return this.helper.getWritableDatabase();
    }

    protected final void closeDatabase(SQLiteDatabase database, Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (database != null && database.isOpen()) {
            database.close();
        }

    }

    protected abstract String getTableName();

    public int count() {
        return this.countColumn("id");
    }

    public int countColumn(String columnName) {
        String sql = "SELECT COUNT(?) FROM " + this.getTableName();
        SQLiteDatabase database = this.openReader();
        Cursor cursor = null;

        try {
            database.beginTransaction();
            cursor = database.rawQuery(sql, new String[]{columnName});
            int count = 0;
            if (cursor.moveToNext()) {
                count = cursor.getInt(0);
            }

            database.setTransactionSuccessful();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, cursor);
        }

        return 0;
    }

    public int deleteAll() {
        return this.delete((String) null, (String[]) null);
    }

    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase database = this.openWriter();

        try {
            database.beginTransaction();
            int result = database.delete(this.getTableName(), whereClause, whereArgs);
            database.setTransactionSuccessful();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, (Cursor) null);
        }

        return 0;
    }

    public List<T> getAll() {
        return this.get((String) null, (String[]) null);
    }

    public List<T> get(String selection, String[] selectionArgs) {
        return this.get((String[]) null, selection, selectionArgs, (String) null, (String) null, (String) null, (String) null);
    }

    public List<T> get(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        SQLiteDatabase database = this.openReader();
        List<T> list = new ArrayList();
        Cursor cursor = null;

        try {
            database.beginTransaction();
            cursor = database.query(this.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            while (!cursor.isClosed() && cursor.moveToNext()) {
                list.add(this.parseCursorToBean(cursor));
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, cursor);
        }

        return list;
    }

    public long replace(T t) {
        SQLiteDatabase database = this.openWriter();

        try {
            database.beginTransaction();
            long id = database.replace(this.getTableName(), (String) null, this.getContentValues(t));
            database.setTransactionSuccessful();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, (Cursor) null);
        }

        return 0L;
    }

    public long create(T t) {
        SQLiteDatabase database = this.openWriter();

        try {
            database.beginTransaction();
            long id = database.insert(this.getTableName(), (String) null, this.getContentValues(t));
            database.setTransactionSuccessful();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, (Cursor) null);
        }

        return 0L;
    }

    public int update(T t, String whereClause, String[] whereArgs) {
        SQLiteDatabase database = this.openWriter();

        try {
            database.beginTransaction();
            //根据列条件筛选出项，再根据ContentValue的键值对逐一对应修改该项的列
            int count = database.update(this.getTableName(), this.getContentValues(t), whereClause, whereArgs);
            database.setTransactionSuccessful();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            this.closeDatabase(database, (Cursor) null);
        }

        return 0;
    }

    public abstract T parseCursorToBean(Cursor cursor);

    public abstract ContentValues getContentValues(T t);
}