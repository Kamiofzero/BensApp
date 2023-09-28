package com.ljb.bens.ui.myapp

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.ljb.base.activity.BaseActivity
import com.ljb.bens.databinding.ActivityAppDetailBinding
import kotlin.math.abs


class AppDetailActivity : BaseActivity<ActivityAppDetailBinding, AppDetailViewModel>() {

    override fun onClick(p0: View?) {
    }

    private var state: CollapsingToolbarLayoutState? = null

    private enum class CollapsingToolbarLayoutState {
        EXPANDED, COLLAPSED, INTERNEDIATE
    }

    override fun initView() {

        setSupportActionBar(vb.toolbar)

        var index = intent.getIntExtra("index", 0)
        var appInfo = MyAppRepository.instance.getApp(index)

//        vb.toolbarLayout.isTitleEnabled = false
//        supportActionBar?.title = appInfo.appName

        vb.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
//        vb.toolbarLayout.title = ""

        //CollapsingToolbarLayout 的title设置需要在layout完成后才生效
        vb.toolbarLayout.viewTreeObserver.addOnGlobalLayoutListener {
            vb.toolbarLayout.title = ""
            vb.toolbarLayout.viewTreeObserver.removeOnGlobalLayoutListener { this }
        }
        vb.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {//最大展开时
                if (state != CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED
                    vb.toolbarLayout.title = ""
                }
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {//折叠时
                if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                    vb.toolbarLayout.title = appInfo.appName
                    state = CollapsingToolbarLayoutState.COLLAPSED;
                }
            } else {//中间过程
                if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                    }
                    vb.toolbarLayout.title = ""
                    state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                }
            }
        }


    }

    override fun initData() {
    }

    override fun initModel() {
    }
}