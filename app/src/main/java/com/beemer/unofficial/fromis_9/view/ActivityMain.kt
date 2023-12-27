package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class ActivityMain : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val toolbar by lazy { binding.toolbar }
    private val bottombar by lazy { binding.bottombar }
    private val layoutParent by lazy { binding.layoutParent }
    private val layoutFragment by lazy { binding.layoutFragment }

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

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.appInfo -> {

                    }
                }
                return true
            }
        })

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        changeFragment(FragmentHome())
        bottombar.onItemSelected = ::onBottomBarItemSelected
    }

    private fun onBottomBarItemSelected(position: Int) {
        changeFragment(
            when (position) {
                0 -> FragmentHome()
                1 -> FragmentVideo()
                else -> FragmentSchedule()
            }
        )
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(layoutFragment.id, fragment).commit()
    }
}