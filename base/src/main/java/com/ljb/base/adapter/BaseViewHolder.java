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

    public abstract void bind(T data);

    public abstract void updateUI(T data);
}