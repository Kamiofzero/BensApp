package com.ljb.bens.ui.myapp

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.ljb.base.activity.BaseActivity
import com.ljb.base.utils.LogUtil
import com.ljb.bens.databinding.ActivityAppDetailBinding


class AppDetailActivity : BaseActivity<ActivityAppDetailBinding, AppDetailViewModel>() {

    override fun onClick(p0: View?) {
    }

    override fun initView() {

        setSupportActionBar(vb.toolbar)

        var index = intent.getIntExtra("index", 0)
        var appInfo = MyAppRepository.instance.getApp(index)

//        vb.toolbarLayout.title = appInfo.appName
        vb.toolbarLayout.isTitleEnabled = false
        supportActionBar?.title = appInfo.appName

        vb.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        vb.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            LogUtil.i("verticalOffset: $verticalOffset")
            supportActionBar?.title =
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    // Collapsed
                    appInfo.appName

                } else {
                    // Expanded
                    ""
                }
        };
    }

    override fun initData() {
    }

    override fun initModel() {
    }
}