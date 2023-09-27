package com.ljb.bens.ui.myapp

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import com.ljb.base.adapter.BaseViewHolder
import com.ljb.base.adapter.SelectorAdapter
import com.ljb.base.utils.LogUtil
import com.ljb.bens.R
import com.ljb.bens.beans.AppInfo
import com.ljb.bens.beans.AppInfoPkg
import com.ljb.bens.databinding.ItemFileBinding
import com.ljb.bens.ui.myapp.DownloadingAdapter.MyViewHolder
import com.ljb.downloadx.DownloadTask

class DownloadingAdapter(context: Context?) :
    SelectorAdapter<AppInfo?, ItemFileBinding?, MyAppViewModel?, MyViewHolder?>(context) {
    private val TAG = "DownloadingAdapter"

    init {
        setItemClickWithoutSelectListener { view, position ->
            Intent(context, AppDetailActivity::class.java).apply {
                this.putExtra(
                    "index", position
                )
            }.let {
                context?.startActivity(it)
            }
        }
    }

    //    @Override
    //    protected ItemFileBinding initViewBinding(LayoutInflater layoutInflater, ViewGroup container) {
    //        return ItemFileBinding.inflate(layoutInflater, container, false);
    //    }
    //    @Override
    //    protected MyViewHolder createViewHolder(ItemFileBinding vb, MyAppViewModel vm) {
    //        return new MyViewHolder(vb, vm);
    //    }
    inner class MyViewHolder(vb: ItemFileBinding?, myAppViewModel: MyAppViewModel?) :
        BaseViewHolder<SelectorObject<AppInfo?>?, ItemFileBinding?, MyAppViewModel?>(
            vb, myAppViewModel
        ) {
        private var status = -1

        //        public MyViewHolder(ItemFileBinding vb, MyAppViewModel vm) {
        //            super(vb, vm);
        //        }


        override fun bind(data: SelectorObject<AppInfo?>?) {

            val bean = data?.obj ?: return
            //            LogUtil.i(TAG, data.getObj().appName + " bind");
            vb!!.name.text = bean.appName
            vb!!.description.text = bean.appDescription
            bindChangeableUI(bean)
            vb!!.btnDownload.setOnClickListener { v: View? ->
                if (status == DownloadTask.STATUS_IDLE || status == DownloadTask.STATUS_STOPPED || status == DownloadTask.STATUS_ERROR) {
                    LogUtil.i(bean.appName + " download")
                    vm!!.download(bean.url)
                } else if (status == DownloadTask.STATUS_DOWNLOADING) {
                    LogUtil.i(bean.appName + " stopDownload")
                    vm!!.stopDownload(bean.url)
                } else if (status == DownloadTask.STATUS_DOWNLOADED) {
                    LogUtil.i(bean.appName + " launch")
                    vm!!.launch(bean.appName)
                }
            }
            vb!!.cbSelect.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                data.setSelected(
                    isChecked
                )
            }
            vb!!.cbSelect.isChecked = data.isSelected
            if (f_select_mode) {
                vb!!.btnDownload.visibility = View.GONE
                vb!!.cbSelect.visibility = View.VISIBLE
            } else {
                vb!!.btnDownload.visibility = View.VISIBLE
                vb!!.cbSelect.visibility = View.GONE
            }
        }


        override fun updateUI(data: SelectorObject<AppInfo?>?) {
            data!!.obj?.let { bindChangeableUI(it) }
        }

        private fun bindChangeableUI(data: AppInfo) {
//            LogUtil.i(TAG,"bindChangeableUI");
            val progress = data.downloadPercent
            if (vb!!.pb.progress != progress) {
                vb!!.pb.progress = progress
                vb!!.btnDownload.text = "$progress%"
                LogUtil.i(TAG, data.appName + " progress: " + data.downloadPercent)
            }
            val tempStatus = data.status
            //            LogUtil.i(TAG,"data status: " + data.status);
            if (status != tempStatus) {
                status = tempStatus
                //                LogUtil.i(TAG,"status: " + data.status);
                if (status == DownloadTask.STATUS_IDLE) {
                    vb!!.btnDownload.text = context.getString(R.string.download)
                    LogUtil.i(TAG, data.appName + " state : IDLE")
                } else if (status == DownloadTask.STATUS_WAITING) {
                    vb!!.btnDownload.text = context.getString(R.string.waiting)
                    LogUtil.i(TAG, data.appName + " state : WAITING")
                } else if (status == DownloadTask.STATUS_PREPARING) {
                    vb!!.btnDownload.text = context.getString(R.string.preparing)
                    LogUtil.i(TAG, data.appName + " state : PREPARE_DOWNLOADING")
                } else if (status == DownloadTask.STATUS_PREPARED) {
                    LogUtil.i(TAG, data.appName + " state : STATUS_PREPARED")
                } else if (status == DownloadTask.STATUS_STOPPED) {
                    vb!!.btnDownload.text = context.getString(R.string.continue_d)
                    LogUtil.i(TAG, data.appName + " state : STOP")
                } else if (status == DownloadTask.STATUS_DOWNLOADING) {
//                    vb.btnDownload.setText(context.getString(R.string.stop));
                    vb!!.pb.visibility = View.VISIBLE
                    LogUtil.i(TAG, data.appName + " state : DOWNLOADING")
                } else if (status == DownloadTask.STATUS_DOWNLOADED) {
                    vb!!.btnDownload.text = context.getString(R.string.launch)
                    vb!!.pb.progress = 0
                    vb!!.pb.visibility = View.INVISIBLE
                    LogUtil.i(TAG, data.appName + " state : DOWNLOADED")
                } else if (status == DownloadTask.STATUS_CANCELED) {
                    vb!!.btnDownload.text = context.getString(R.string.download)
                    vb!!.pb.progress = 0
                    vb!!.pb.visibility = View.INVISIBLE
                    LogUtil.i(TAG, data.appName + " state : ERROR")
                } else if (status == DownloadTask.STATUS_ERROR) {
                    vb!!.btnDownload.text = context.getString(R.string.retry)
                    LogUtil.i(TAG, data.appName + " state : ERROR")
                }
            }
        }


    }

    override fun setSourceDataList(dataList: MutableList<AppInfo?>?) {
        super.setSourceDataList(dataList)
        if (recyclerView != null) {
            if (this.dataList.size == 0) {
                recyclerView.visibility = View.INVISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    fun update(pkg: AppInfoPkg?) {
//        LogUtil.i(TAG,"update");
        updateSourceUI(pkg!!.appInfo, pkg.index)
    }

    fun insertData(fileBean: AppInfo?) {
        insertSourceData(fileBean)
        if (recyclerView != null) {
            if (dataList.size == 1) {
                recyclerView.visibility = View.VISIBLE
            }
        }
    } //
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