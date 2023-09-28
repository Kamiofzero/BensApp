package com.ljb.bens.ui.myapp

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ljb.base.activity.BaseActivity
import com.ljb.bens.R
import com.ljb.bens.animation.AnimationMan
import com.ljb.bens.databinding.ActivityMyAppsBinding

class MyAppsActivity : BaseActivity<ActivityMyAppsBinding, MyAppViewModel>() {


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cancel -> {
            }
        }
    }

    private lateinit var mAdapter: DownloadingAdapter

    override fun initView() {
        mAdapter = DownloadingAdapter(context).apply {
            bindRecyclerView(vb.recyclerView)
            bindViewModel(vm)
            setOnSelectChangedListener {
                AnimationMan.myAppOperationBarAnim(vb.lOperation, it)
            }
        }
        vb.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    context, LinearLayoutManager.VERTICAL
                )
            )
            adapter = mAdapter
        }

        vb.btnCancel.setOnClickListener(this)
        supportActionBar?.title = "FavorApps"

        vb.flContent.viewTreeObserver.addOnGlobalLayoutListener {
            vb.lOperation.y = vb.flContent.height.toFloat()
        }
    }


    override fun initModel() {
        vm.appInfoData.observe(
            this
        ) { mAdapter.update(it) }
        vm.appListData.observe(this) {
            mAdapter.setSourceDataList(it)
        }
    }

    override fun initData() {
        vm.getData()
    }

    override fun onBackPressed() {
        if (mAdapter.isSelect) mAdapter.cancelSelect()
        else super.onBackPressed()
    }

}