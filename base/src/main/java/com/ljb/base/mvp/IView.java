package com.ljb.base.mvp;

public interface IView {

    void showProgress(String msg);

    void hideProgress();

    void showToast(String msg);

    void runOnUI(Runnable r);
}
