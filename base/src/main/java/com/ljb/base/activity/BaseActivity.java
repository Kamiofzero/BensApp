package com.ljb.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.ljb.base.adapter.BaseRecyclerViewAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<T extends ViewBinding, VM extends ViewModel> extends AppCompatActivity implements View.OnClickListener {
    protected T vb;
    protected VM vm;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            vb = (T) method.invoke(null, getLayoutInflater());
            setContentView(vb.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        setContentView(vb.getRoot());

        vm = new ViewModelProvider(this).get((Class<VM>) ((ParameterizedType) superclass).getActualTypeArguments()[1]);

    }

    protected abstract void initView();

    protected abstract void initData();
}
