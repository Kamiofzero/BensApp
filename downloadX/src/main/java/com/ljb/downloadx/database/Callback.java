package com.ljb.downloadx.database;

public interface Callback<T> {

    void onSuccess(T t);

    void onFail();
}
