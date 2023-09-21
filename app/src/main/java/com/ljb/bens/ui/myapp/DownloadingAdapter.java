package com.ljb.bens.ui.myapp;


import static com.ljb.downloadx.DownloadTask.STATUS_CANCELED;
import static com.ljb.downloadx.DownloadTask.STATUS_DOWNLOADED;
import static com.ljb.downloadx.DownloadTask.STATUS_DOWNLOADING;
import static com.ljb.downloadx.DownloadTask.STATUS_ERROR;
import static com.ljb.downloadx.DownloadTask.STATUS_IDLE;
import static com.ljb.downloadx.DownloadTask.STATUS_PREPARING;
import static com.ljb.downloadx.DownloadTask.STATUS_STOPPED;
import static com.ljb.downloadx.DownloadTask.STATUS_WAITING;

import android.content.Context;
import android.view.View;

import com.ljb.base.adapter.BaseViewHolder;
import com.ljb.base.adapter.SelectorAdapter;
import com.ljb.base.utils.LogUtil;
import com.ljb.bens.R;
import com.ljb.bens.beans.AppInfo;
import com.ljb.bens.beans.AppInfoPkg;
import com.ljb.bens.databinding.ItemFileBinding;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DownloadingAdapter extends SelectorAdapter<AppInfo, ItemFileBinding, MyAppViewModel, DownloadingAdapter.MyViewHolder> {


    public DownloadingAdapter(Context context) {
        super(context);
    }


//    @Override
//    protected ItemFileBinding initViewBinding(LayoutInflater layoutInflater, ViewGroup container) {
//        return ItemFileBinding.inflate(layoutInflater, container, false);
//    }

//    @Override
//    protected MyViewHolder createViewHolder(ItemFileBinding vb, MyAppViewModel vm) {
//        return new MyViewHolder(vb, vm);
//    }

    public class MyViewHolder extends BaseViewHolder<SelectorObject<AppInfo>, ItemFileBinding, MyAppViewModel> {
        int status = -1;

        public MyViewHolder(ItemFileBinding vb, MyAppViewModel myAppViewModel) {
            super(vb, myAppViewModel);
        }

//        public MyViewHolder(ItemFileBinding vb, MyAppViewModel vm) {
//            super(vb, vm);
//        }

        @Override
        public void bind(SelectorAdapter.SelectorObject<AppInfo> data) {
            AppInfo bean = data.getObj();
            LogUtil.i(data.getObj().appName + " bind");
            vb.name.setText(bean.appName);
            vb.description.setText(bean.appDescription);
            bindChangeableUI(bean);

            vb.btnDownload.setOnClickListener(v -> {
                LogUtil.i(bean.appName + " click");

                if (status == STATUS_IDLE || status == STATUS_STOPPED || status == STATUS_ERROR) {
                    LogUtil.i(bean.appName + " download");
                    vm.download(bean.url);
                } else if (status == STATUS_DOWNLOADING) {
                    LogUtil.i(bean.appName + " stopDownload");
                    vm.stopDownload(bean.url);
                } else if (status == STATUS_DOWNLOADED) {
                    LogUtil.i(bean.appName + " launch");
                    vm.launch(bean.appName);
                }

            });

            vb.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> data.setSelected(isChecked));

            if (data.isSelected()) {
                vb.cbSelect.setChecked(true);
            } else {
                vb.cbSelect.setChecked(false);
            }

            if (f_select_mode) {
                vb.btnDownload.setVisibility(View.GONE);
                vb.cbSelect.setVisibility(View.VISIBLE);
            } else {
                vb.btnDownload.setVisibility(View.VISIBLE);
                vb.cbSelect.setVisibility(View.GONE);
            }
        }

        @Override
        public void updateUI(SelectorObject<AppInfo> data) {
            bindChangeableUI(data.getObj());

        }


        private void bindChangeableUI(AppInfo data) {
            LogUtil.i("bindChangeableUI");
            int progress = data.downloadPercent;
            if (vb.pb.getProgress() != progress) {
                vb.pb.setProgress(progress);
                vb.btnDownload.setText(progress+ "%");
                LogUtil.i(data.appName + " p: " + data.downloadPercent);
            }
            int tempStatus = data.status;
            LogUtil.i("data status: " + data.status);

            if (status != tempStatus) {
                status = tempStatus;
                LogUtil.i("status: " + data.status);
                if (status == STATUS_IDLE) {
                    vb.btnDownload.setText(context.getString(R.string.download));
                    LogUtil.i(data.appName + " state : IDLE");
                } else if (status == STATUS_WAITING) {
                    vb.btnDownload.setText(context.getString(R.string.waiting));
                    LogUtil.i(data.appName + " state : WAITING");
                } else if (status == STATUS_PREPARING) {
                    vb.btnDownload.setText(context.getString(R.string.preparing));
                    LogUtil.i(data.appName + " state : PREPARE_DOWNLOADING");
                } else if (status == STATUS_STOPPED) {
                    vb.btnDownload.setText(context.getString(R.string.download));
                    LogUtil.i(data.appName + " state : STOP");
                } else if (status == STATUS_DOWNLOADING) {
//                    vb.btnDownload.setText(context.getString(R.string.stop));
                    vb.pb.setVisibility(View.VISIBLE);
                    LogUtil.i(data.appName + " state : DOWNLOADING");
                } else if (status == STATUS_DOWNLOADED) {
                    vb.btnDownload.setText(context.getString(R.string.launch));
                    vb.pb.setProgress(0);
                    vb.pb.setVisibility(View.INVISIBLE);
                    LogUtil.i(data.appName + " state : DOWNLOADED");
                } else if (status == STATUS_CANCELED) {
                    vb.btnDownload.setText(context.getString(R.string.download));
                    vb.pb.setProgress(0);
                    vb.pb.setVisibility(View.INVISIBLE);
                    LogUtil.i(data.appName + " state : ERROR");
                } else if (status == STATUS_ERROR) {
                    vb.btnDownload.setText(context.getString(R.string.retry));
                    LogUtil.i(data.appName + " state : ERROR");
                }
            }
        }
    }

    @Override
    public void setSourceDataList(List<AppInfo> dataList) {
        super.setSourceDataList(dataList);
        if (recyclerView != null) {
            if (this.dataList.size() == 0) {
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void update(@Nullable AppInfoPkg pkg) {
        LogUtil.i("update");
        updateSourceUI(pkg.appInfo, pkg.index);
    }


    public void insertData(AppInfo fileBean) {
        insertSourceData(fileBean);
        if (recyclerView != null) {
            if (this.dataList.size() == 1) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
//
//    public void onDownloaded(AppInfo fileBean) {
//        int index = getIndexForDownloadingBean(fileBean.url);
////        dataList.remove(index);
////        notifyItemRemoved(index);
//        removeSourceData(index);
//        if (recyclerView != null) {
//            if (this.dataList.size() == 0) {
//                recyclerView.setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//
//    public void onDownloadCancel(AppInfo fileBean) {
//        onDownloaded(fileBean);
//    }
//
//    private void onDownloading(AppInfo fileBean) {
//        int index = getIndexForDownloadingBean(fileBean.url);
//        updateSourceUI(fileBean, index);
//    }


//    public int getIndexForDownloadingBean(String key) {
//        for (int i = 0; i < dataList.size(); i++) {
//            AppInfo fileBean = dataList.get(i).getObj();
//            if (fileBean.url.equals(key)) {
//                return i;
//            }
//        }
//        return -1;
//    }


}
