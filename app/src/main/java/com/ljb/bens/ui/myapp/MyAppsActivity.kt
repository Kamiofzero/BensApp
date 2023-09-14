package com.ljb.bens.ui.myapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ljb.bens.R
import com.ljb.bens.databinding.ActivityMyAppsBinding

class MyAppsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMyAppsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }
}