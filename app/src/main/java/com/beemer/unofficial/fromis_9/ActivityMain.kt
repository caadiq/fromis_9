package com.beemer.unofficial.fromis_9

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val toolbar by lazy { binding.toolbar }
    private val layoutFragment by lazy { binding.layoutFragment }
    private val bottombar by lazy { binding.bottombar }

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