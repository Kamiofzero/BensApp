package com.ljb.bens.ui.myapp

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ljb.base.activity.BaseActivity
import com.ljb.base.utils.LogUtil
import com.ljb.bens.R
import com.ljb.bens.beans.AppInfo
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
                vm.test()
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

    var appList = arrayListOf<AppInfo>()

    override fun initData() {
        appList.add(
            AppInfo(
                "bilibili",
                "视频弹幕网站",
                "https://downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.8.88.7830_537117916_HB.64.apk"
            )
        )
        appList.add(
            AppInfo(
                "QQ",
                "聊天软件",
                "https://dl.hdslb.com/mobile/latest/android64/iBiliPlayer-bili.apk?t=1652253615000&amp;spm_id_from=333.47.b_646f776e6c6f61642d6c696e6b.1"
            )
        )
        appList.add(AppInfo("via", "简洁浏览器", "https://res.viayoo.com/v1/via-release-cn.apk"))

        adapter.setSourceDataList(appList)

        LogUtil.i("initData")
    }
}