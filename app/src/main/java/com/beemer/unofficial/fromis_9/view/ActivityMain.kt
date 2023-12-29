package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactoryMain
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelMain
import com.google.android.material.snackbar.Snackbar

class ActivityMain : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelMain by lazy { ViewModelProvider(this, ViewModelFactoryMain())[ViewModelMain::class.java] }

    private val toolbar by lazy { binding.toolbar }
    private val btnInfo by lazy { binding.btnInfo }
    private val bottomNav by lazy { binding.bottomNav }
    private val layoutParent by lazy { binding.layoutParent }

    private var backPressedTime: Long = 0
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Snackbar.make(layoutParent, getString(R.string.str_activity_main_press_back), 2000).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbar.title = ""
        setSupportActionBar(toolbar)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

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

        bottomNav.setOnItemSelectedListener { item ->
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

        btnInfo.setOnClickListener {
            Toast.makeText(this, "앱 정보", Toast.LENGTH_SHORT).show()
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