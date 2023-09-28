package com.ljb.bens.ui.myapp

import android.content.Context
import android.content.Intent
import android.view.View
import com.ljb.base.adapter.SelectorAdapter
import com.ljb.base.utils.LogUtil
import com.ljb.bens.R
import com.ljb.bens.beans.AppInfo
import com.ljb.bens.beans.AppInfoPkg
import com.ljb.bens.databinding.ItemFileBinding
import com.ljb.bens.ui.myapp.DownloadingAdapter.MyViewHolder
import com.ljb.downloadx.DownloadTask.STATUS_CANCELED
import com.ljb.downloadx.DownloadTask.STATUS_DOWNLOADED
import com.ljb.downloadx.DownloadTask.STATUS_DOWNLOADING
import com.ljb.downloadx.DownloadTask.STATUS_ERROR
import com.ljb.downloadx.DownloadTask.STATUS_IDLE
import com.ljb.downloadx.DownloadTask.STATUS_PREPARED
import com.ljb.downloadx.DownloadTask.STATUS_PREPARING
import com.ljb.downloadx.DownloadTask.STATUS_STOPPED
import com.ljb.downloadx.DownloadTask.STATUS_WAITING

class DownloadingAdapter(context: Context?) :
    SelectorAdapter<AppInfo?, ItemFileBinding?, MyAppViewModel?, MyViewHolder?>(context) {
    private val tag = "DownloadingAdapter"

    init {
        setItemClickWithoutSelectListener { _, position ->
            Intent(context, AppDetailActivity::class.java).apply {
                this.putExtra(
                    "index", position
                )
            }.let {
                context?.startActivity(it)
            }
        }
    }
    inner class MyViewHolder(vb: ItemFileBinding?, myAppViewModel: MyAppViewModel?) :
        SelectorHolder<AppInfo?, ItemFileBinding?, MyAppViewModel?>(
            vb, myAppViewModel
        ) {
        private var status = -1
        override fun onNotifyDataChanged(data: AppInfo?) {
            if (data != null) {
                vb!!.name.text = data.appName
                vb!!.description.text = data.appDescription
                vb!!.btnDownload.setOnClickListener {
                    when (status) {

                        STATUS_IDLE, STATUS_STOPPED, STATUS_ERROR -> {
                            LogUtil.i(data.appName + " download")
                            vm!!.download(data.url)
                        }

                        STATUS_DOWNLOADING -> {
                            LogUtil.i(data.appName + " stopDownload")
                            vm!!.stopDownload(data.url)
                        }

                        STATUS_DOWNLOADED -> {
                            LogUtil.i(data.appName + " launch")
                            vm!!.launch(data.appName)
                        }
                    }
                }
                bindChangeableUI(data)
            }
            if (isSelectMode) {
                vb!!.btnDownload.visibility = View.GONE
            } else {
                vb!!.btnDownload.visibility = View.VISIBLE
            }
        }

//        override fun bind(data: SelectorObject<AppInfo?>?) {
//
//            val bean = data?.obj ?: return
//            //            LogUtil.i(TAG, data.getObj().appName + " bind");
//            vb!!.name.text = bean.appName
//            vb!!.description.text = bean.appDescription
//            bindChangeableUI(bean)
//            vb!!.btnDownload.setOnClickListener { v: View? ->
//                if (status == DownloadTask.STATUS_IDLE || status == DownloadTask.STATUS_STOPPED || status == DownloadTask.STATUS_ERROR) {
//                    LogUtil.i(bean.appName + " download")
//                    vm!!.download(bean.url)
//                } else if (status == DownloadTask.STATUS_DOWNLOADING) {
//                    LogUtil.i(bean.appName + " stopDownload")
//                    vm!!.stopDownload(bean.url)
//                } else if (status == DownloadTask.STATUS_DOWNLOADED) {
//                    LogUtil.i(bean.appName + " launch")
//                    vm!!.launch(bean.appName)
//                }
//            }
//            vb!!.cbSelect.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
//                data.setSelected(
//                    isChecked
//                )
//            }
//            vb!!.cbSelect.isChecked = data.isSelected
//            if (isSelectMode) {
//                vb!!.btnDownload.visibility = View.GONE
//                vb!!.cbSelect.visibility = View.VISIBLE
//            } else {
//                vb!!.btnDownload.visibility = View.VISIBLE
//                vb!!.cbSelect.visibility = View.GONE
//            }
//        }


        override fun updateUI(data: SelectorObject<AppInfo?>?) {
            data!!.obj?.let { bindChangeableUI(it) }
        }

        private fun bindChangeableUI(data: AppInfo) {
//            LogUtil.i(TAG,"bindChangeableUI");
            val progress = data.downloadPercent
            if (vb!!.pb.progress != progress) {
                vb!!.pb.progress = progress
                vb!!.btnDownload.text = "$progress%"
                LogUtil.i(tag, data.appName + " progress: " + data.downloadPercent)
            }
            val tempStatus = data.status
            //            LogUtil.i(TAG,"data status: " + data.status);
            if (status != tempStatus) {
                status = tempStatus
                //                LogUtil.i(TAG,"status: " + data.status);

                when (status) {
                    STATUS_IDLE -> {
                        vb!!.btnDownload.text = context.getString(R.string.download)
                        LogUtil.i(tag, data.appName + " state : IDLE")
                    }

                    STATUS_WAITING -> {
                        vb!!.btnDownload.text = context.getString(R.string.waiting)
                        LogUtil.i(tag, data.appName + " state : WAITING")
                    }

                    STATUS_PREPARING -> {
                        vb!!.btnDownload.text = context.getString(R.string.preparing)
                        LogUtil.i(tag, data.appName + " state : PREPARE_DOWNLOADING")
                    }

                    STATUS_PREPARED -> {
                        LogUtil.i(tag, data.appName + " state : STATUS_PREPARED")
                    }

                    STATUS_STOPPED -> {
                        vb!!.btnDownload.text = context.getString(R.string.continue_d)
                        LogUtil.i(tag, data.appName + " state : STOP")
                    }

                    STATUS_DOWNLOADING -> {
                        vb!!.pb.visibility = View.VISIBLE
                        LogUtil.i(tag, data.appName + " state : DOWNLOADING")
                    }

                    STATUS_DOWNLOADED -> {
                        vb!!.btnDownload.text = context.getString(R.string.launch)
                        vb!!.pb.progress = 0
                        vb!!.pb.visibility = View.INVISIBLE
                        LogUtil.i(tag, data.appName + " state : DOWNLOADED")
                    }

                    STATUS_CANCELED -> {
                        vb!!.btnDownload.text = context.getString(R.string.download)
                        vb!!.pb.progress = 0
                        vb!!.pb.visibility = View.INVISIBLE
                        LogUtil.i(tag, data.appName + " state : CANCELED")
                    }

                    STATUS_ERROR -> {
                        vb!!.btnDownload.text = context.getString(R.string.retry)
                        LogUtil.i(tag, data.appName + " state : ERROR")
                    }
                }
            }
        }


    }

    fun update(pkg: AppInfoPkg?) {
        updateSourceUI(pkg!!.appInfo, pkg.index)
    }

    fun insertData(fileBean: AppInfo?) {
        insertSourceData(fileBean)
        if (recyclerView != null) {
            if (dataList.size == 1) {
                recyclerView.visibility = View.VISIBLE
            }
        }
    }
}