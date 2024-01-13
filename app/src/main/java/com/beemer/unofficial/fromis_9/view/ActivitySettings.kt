package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding

class ActivitySettings : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.switchDarkMode.isChecked = true
    }
}