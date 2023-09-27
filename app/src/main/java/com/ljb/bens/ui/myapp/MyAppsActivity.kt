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

    private lateinit var adapter: DownloadingAdapter

    override fun initView() {
        vb.recyclerView.layoutManager = LinearLayoutManager(context)
        vb.recyclerView.itemAnimator = DefaultItemAnimator()
        vb.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
        )
        adapter = DownloadingAdapter(context)
        vb.recyclerView.adapter = adapter
        adapter.bindRecyclerView(vb.recyclerView)
        adapter.bindViewModel(vm)
        adapter.setOnSelectChangedListener {
            AnimationMan.myAppOperationBarAnim(vb.lOperation, it)
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
        ) { adapter.update(it) }
        vm.appListData.observe(this) {
            adapter.setSourceDataList(it.toMutableList())
        }
    }

    override fun initData() {
        vm.getData()
    }

    override fun onBackPressed() {
        if (adapter.isSelect) adapter.cancelSelect()
        else super.onBackPressed()
    }

}