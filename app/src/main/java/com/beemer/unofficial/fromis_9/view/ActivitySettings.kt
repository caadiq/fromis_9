package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding
import com.mikepenz.aboutlibraries.LibsBuilder

class ActivitySettings : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.switchDarkMode.isChecked = true
        binding.txtLicenseDetails.setOnClickListener {
            LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withAboutIconShown(true)
                .start(this)
        }
    }
}