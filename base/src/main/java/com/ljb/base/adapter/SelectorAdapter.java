package com.ljb.base.adapter;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectorAdapter<T extends BeanKey, V extends ViewBinding, VM extends ViewModel, H extends BaseViewHolder<SelectorAdapter.SelectorObject<T>, V, VM>> extends BaseRecyclerViewAdapter<SelectorAdapter.SelectorObject<T>, V, VM, H> {

    protected boolean f_select_mode;


    public SelectorAdapter(Context context) {
        super(context);
        init();
    }

    public SelectorAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        init();
    }

    void init() {
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (f_select_mode) {
                    cancelSelect();
                } else {
                    select();
                }

                return false;
            }
        });
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (f_select_mode) {
                    SelectorObject selectorObject = dataList.get(position);
                    selectorObject.setSelected(!selectorObject.isSelected);
                    notifyItemChanged(position);
                } else {
                    if (itemClickWithoutSelectListener != null)
                        itemClickWithoutSelectListener.onItemClick(view, position);
                }
            }
        });
    }


    public void select() {
        f_select_mode = true;
        notifyDataSetChanged();
        if (onSelectChangedListener != null) onSelectChangedListener.onSelectChanged(f_select_mode);
    }

    public void cancelSelect() {
        f_select_mode = false;
        for (SelectorObject obj : dataList) {
            obj.setSelected(false);
        }
        notifyDataSetChanged();
        if (onSelectChangedListener != null) onSelectChangedListener.onSelectChanged(f_select_mode);
    }

    public void allSelect(boolean b) {
        for (SelectorObject obj : dataList) {
            obj.setSelected(b);
        }
        notifyDataSetChanged();
    }

    public boolean isSelect() {
        return f_select_mode;
    }


    public List<T> getSelectedList() {
        List<T> selectedList = new ArrayList<>();
        for (SelectorObject obj : dataList) {
            if (obj.isSelected()) selectedList.add((T) obj.getObj());
        }
        return selectedList;
    }


    public void setSourceDataList(List<T> dataList) {
        List<SelectorObject<T>> list = new ArrayList<>();
        for (T t : dataList) {
            list.add(new SelectorObject<>(t));
        }
        setDataList(list);
    }


    public void insertSourceData(T t) {
        addDataByIndex(new SelectorObject<>(t), 0);
    }

    public void removeSourceData(int index) {
        removeDataByIndex(index);
    }


    public void setSourceData(T t, int index) {
        setDataByIndex(new SelectorObject<>(t), index);
    }

    public void updateSourceUI(T t, int index) {
        updateUIByIndex(new SelectorObject<>(t), index);
    }


    public interface OnItemClickWithoutSelectListener {
        void onItemClick(View view, int position);
    }

    OnItemClickWithoutSelectListener itemClickWithoutSelectListener;

    public void setItemClickWithoutSelectListener(OnItemClickWithoutSelectListener listener) {
        this.itemClickWithoutSelectListener = listener;
    }


    public interface OnSelectChangedListener {
        void onSelectChanged(boolean isSelect);
    }

    OnSelectChangedListener onSelectChangedListener;

    public void setOnSelectChangedListener(OnSelectChangedListener onSelectChangedListener) {
        this.onSelectChangedListener = onSelectChangedListener;
    }


    public static class SelectorObject<T extends BeanKey> implements BeanKey {

        boolean isSelected;

        T t;

        public SelectorObject(T t) {
            this.t = t;
        }

        public SelectorObject() {
        }


        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public boolean isSelected() {
            return isSelected;
        }

        public void setObject(T t) {
            this.t = t;
        }

        public T getObj() {
            return t;
        }

        @Override
        public String getKey() {
            return t.getKey();
        }
    }


    @Override
    public void updateUIByIndex(SelectorObject<T> data, int index) {
        super.updateUIByIndex(data, index);
    }
}