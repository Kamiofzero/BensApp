package com.ljb.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.ljb.base.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T extends BeanKey, V extends ViewBinding, VM extends ViewModel, H extends BaseViewHolder<T, V, VM>> extends RecyclerView.Adapter<H> {


    protected Context context;

    protected List<T> dataList = new ArrayList<>();
    protected RecyclerView recyclerView;
    protected VM vm;


    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public void bindViewModel(VM vm) {
        this.vm = vm;
    }

    public void setDataList(List<T> dataList) {
        this.dataList.clear();
        if (dataList != null) {
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void setDataByIndex(T data, int index) {
        if (data != null && index >= 0 && index < dataList.size()) {
            dataList.set(index, data);
            notifyItemChanged(index);
        }
    }

    public void addDataByIndex(T data, int index) {
        if (data != null) {
            dataList.add(index, data);
            notifyItemInserted(index);
        }
    }


    public void removeDataByIndex(int index) {
        dataList.remove(index);
        notifyItemRemoved(index);
    }


    public void updateUIByIndex(T data, int index) {
        H holder = getViewHolderByIndex(index);
        if (holder != null) holder.updateUI(data);
    }


    public void updateUI(T data) {
        int index = 0;
        for (; index < dataList.size(); index++) {
            if (dataList.get(index).getKey().equals(data.getKey())) {
                H holder = getViewHolderByIndex(index);
                if (holder != null) holder.updateUI(data);
            }
        }
    }

    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
        V vb = null;
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            vb = (V) method.invoke(null, LayoutInflater.from(context));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
//        V vb = initViewBinding(LayoutInflater.from(context), parent);

        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) superclass).getActualTypeArguments()[2];
        Class<H> hClass = (Class<H>) ((ParameterizedType) superclass).getActualTypeArguments()[3];
        LogUtil.i(hClass.toString());
        LogUtil.i(getClass().toString());
        for (Constructor constructor : hClass.getConstructors()) {
            LogUtil.i(constructor.toString());
        }
        H hInstance = null;
        try {

            Constructor<H> constructor = hClass.getConstructor(getClass(), aClass, vmClass);
            hClass.getConstructors();
            hInstance = constructor.newInstance(this, vb, vm);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

//        return createViewHolder(vb, vm);
        return hInstance;
    }


    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        T data = dataList.get(position);

        BaseOnClickListener itemClickListener = new BaseOnClickListener() {
            @Override
            protected void onClick(View view, int position) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(view, position);
            }
        };
        itemClickListener.setPosition(position);
        holder.itemView.setOnClickListener(itemClickListener);

        BaseOnLongClickListener baseOnLongClickListener = new BaseOnLongClickListener() {
            @Override
            protected boolean onLongClick(View view, int position) {
                if (onItemLongClickListener != null)
                    return onItemLongClickListener.onItemLongClick(view, position);

                return false;
            }
        };
        baseOnLongClickListener.setPosition(position);
        holder.itemView.setOnLongClickListener(baseOnLongClickListener);
        holder.bind(data);
    }

//    protected abstract V initViewBinding(LayoutInflater layoutInflater, ViewGroup container);

//    protected abstract H createViewHolder(V vb, VM vm);


    @Override

    public int getItemCount() {
        return dataList.size();
    }


    public abstract static class BaseOnClickListener implements View.OnClickListener {

        int position;


        private void setPosition(int position) {
            this.position = position;
        }

        protected abstract void onClick(View view, int position);

        @Override
        public void onClick(View v) {
            onClick(v, position);
        }
    }

    public abstract static class BaseOnLongClickListener implements View.OnLongClickListener {

        int position;

        private void setPosition(int position) {
            this.position = position;
        }

        protected abstract boolean onLongClick(View view, int position);

        @Override
        public boolean onLongClick(View v) {
            return onLongClick(v, position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;


    protected H getViewHolderByIndex(int index) {
        if (recyclerView != null) return (H) recyclerView.findViewHolderForAdapterPosition(index);
        return null;
    }

}
