package com.ljb.downloadx;

public abstract class Task implements Runnable {

    abstract void doTask();

    @Override
    public void run() {
        doTask();
    }
}
