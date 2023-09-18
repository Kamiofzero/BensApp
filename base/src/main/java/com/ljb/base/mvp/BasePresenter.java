package com.ljb.base.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter<V, M> {

    private WeakReference<V> wrfV;
    protected M model;


    public BasePresenter() {
        model = createModel();
    }

    @Override
    public void setView(V view) {
        if (view != null)
            wrfV = new WeakReference<V>(view);
    }

//    @Override
//    public void setModel(M model) {
//        this.model = model;
//    }

    @Override
    public V getView() {
        return wrfV == null ? null : wrfV.get();
    }

    @Override
    public void destroy() {
        if (wrfV != null) {
            wrfV.clear();
            wrfV = null;
        }
        onViewDestroy();
    }


    protected abstract void onViewDestroy();
}
