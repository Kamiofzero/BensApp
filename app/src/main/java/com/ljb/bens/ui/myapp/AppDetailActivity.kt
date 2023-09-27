package com.ljb.bens.ui.myapp

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.ljb.base.activity.BaseActivity
import com.ljb.bens.R
import com.ljb.bens.databinding.ActivityAppDetailBinding

class AppDetailActivity : BaseActivity<ActivityAppDetailBinding, AppDetailViewModel>() {

    override fun onClick(p0: View?) {
    }

    override fun initView() {

        setSupportActionBar(findViewById(R.id.toolbar))
        vb.toolbarLayout.title = title
        vb.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun initData() {
    }

    override fun initModel() {
    }
}