package com.ljb.base.adapter;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseViewHolder<T, V extends ViewBinding, VM extends ViewModel> extends RecyclerView.ViewHolder {

    protected V vb;
    protected VM vm;


    public BaseViewHolder(V vb, VM vm) {
        super(vb.getRoot());
        this.vb = vb;
        this.vm = vm;
    }

    /**
     * adapter notify 数据时调用
     * @param data
     */
    public abstract void bind(T data);


    /**
     * 不通过adapter notify去更新，而是直接修改对应item view的ui，并更新对应的数据对象
     * 主要应用于item view的局部控件刷新，避免notify整体刷新导致闪烁
     * @param data
     */
    public abstract void updateUI(T data);
}