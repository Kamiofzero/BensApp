package com.ljb.base.mvp;

public interface IPresenter<V extends IView, M extends IModel> {

    void setView(V view);

//    void setModel(M model);

    M createModel();

    V getView();

    void destroy();

}
