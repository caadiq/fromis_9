package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelMain
import com.google.android.material.snackbar.Snackbar

class ActivityMain : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelMain by lazy { ViewModelProvider(this)[ViewModelMain::class.java] }

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Snackbar.make(binding.layoutParent, getString(R.string.str_activity_main_press_back), 2000).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        viewModel.currentFragmentTag.observe(this) { tag ->
            supportFragmentManager.fragments.forEach { fragment ->
                supportFragmentManager.beginTransaction().apply {
                    if (fragment.tag == tag)
                        show(fragment)
                    else
                        hide(fragment)
                }.commit()
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            viewModel.setCurrentFragmentTag(
                when (item.itemId) {
                    R.id.home -> "HOME"
                    R.id.video -> "VIDEO"
                    R.id.schedule -> "SCHEDULE"
                    else -> return@setOnItemSelectedListener false
                }
            )
            true
        }

        if (savedInstanceState == null) {
            // 프래그먼트 추가 및 숨기기
            supportFragmentManager.beginTransaction().apply {
                add(R.id.layoutFragment, FragmentMainHome(), "HOME")
                add(R.id.layoutFragment, FragmentMainVideo(), "VIDEO").hide(FragmentMainVideo())
                add(R.id.layoutFragment, FragmentMainSchedule(), "SCHEDULE").hide(FragmentMainSchedule())
                commit()
            }
        }
    }
}