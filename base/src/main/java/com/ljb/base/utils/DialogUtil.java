package com.ljb.base.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.viewbinding.ViewBinding;

public class DialogUtil {


    public static void showDialog(Context context, String message) {
        createDialog(context, message).show();
    }

    public static void showDialog(Context context, String message, Callback callback) {
        createDialog(context, message, callback).show();
    }

    public static void showDialog(Context context, String message, TwoButtonCallback callback) {
        createDialog(context, message, callback).show();
    }

    public interface Callback {

        void onClick();

    }


    public interface TwoButtonCallback {

        void onPositiveClick();

        void onNegativeClick();
    }


    private static Dialog createDialog(Context context, String message) {
        AlertDialog.Builder builder = createBuilder(context, message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }


    private static Dialog createDialog(Context context, String message, final Callback callback) {
        AlertDialog.Builder builder = createBuilder(context, message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (callback != null)
                    callback.onClick();
            }
        });
        return builder.create();
    }


    private static Dialog createDialog(Context context, String message, final TwoButtonCallback callback) {
        AlertDialog.Builder builder = createBuilder(context, message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (callback != null)
                    callback.onPositiveClick();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (callback != null)
                    callback.onNegativeClick();
            }
        });
        return builder.create();
    }

    private static AlertDialog.Builder createBuilder(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        return builder;
    }


    public static class DialogHolder<T extends ViewBinding> {

        public Dialog dialog;

        public T viewBinding;
    }


    public abstract static class DialogBuilder<T extends ViewBinding> {

        private Context context;

        public abstract T initViewBinding(LayoutInflater inflater);

        public DialogBuilder(Context context) {
            this.context = context;
        }

        public DialogHolder show() {
            Dialog dialog = new Dialog(context);
            DialogHolder<T> holder = new DialogHolder<T>();
            holder.dialog = dialog;
            holder.viewBinding = initViewBinding(LayoutInflater.from(context));
            dialog.setContentView(holder.viewBinding.getRoot());
            dialog.show();
            return holder;
        }

    }
}
