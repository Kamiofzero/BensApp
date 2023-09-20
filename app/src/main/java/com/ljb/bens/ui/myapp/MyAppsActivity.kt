package com.ljb.bens.ui.myapp

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ljb.base.activity.BaseActivity
import com.ljb.bens.R
import com.ljb.bens.databinding.ActivityMyAppsBinding

class MyAppsActivity : BaseActivity<ActivityMyAppsBinding, MyAppViewModel>() {

//    private lateinit var binding: ActivityMyAppsBinding
//
//    lateinit var viewModel: MyAppViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMyAppsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel = ViewModelProvider(this).get(MyAppViewModel::class.java)
//
//
//    }

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

        vb.btnCancel.setOnClickListener(this)
    }


    override fun initModel() {
        vm.appInfoData.observe(
            this
        ) { adapter.update(it) }

        vm.appListData.observe(this) {
            adapter.setSourceDataList(it)
        }
    }

    override fun initData() {
        vm.getData()


    }


}