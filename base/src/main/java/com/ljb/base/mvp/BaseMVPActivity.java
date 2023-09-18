package com.ljb.base.mvp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class BaseMVPActivity<T extends ViewBinding, V extends IView, P extends BasePresenter> extends BaseActivity<T>  {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter != null) presenter.setView(createView());
        initView();
        initData();
    }

    protected abstract P createPresenter();

    protected abstract V createView();

    public Context getContext() {
        return context;
    }
}
