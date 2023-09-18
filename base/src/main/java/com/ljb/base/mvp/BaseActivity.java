package com.ljb.base.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements View.OnClickListener, IView {
    protected T vb;
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
    }

    protected abstract void initView();

    protected abstract void initData();


    ProgressDialog pd;

    @Override
    public void showProgress(String msg) {
        if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
        }
        pd.setMessage(msg);
        pd.show();
    }

    @Override
    public void hideProgress() {
        if (pd != null) pd.dismiss();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void runOnUI(Runnable r) {
        runOnUiThread(r);
    }

}
