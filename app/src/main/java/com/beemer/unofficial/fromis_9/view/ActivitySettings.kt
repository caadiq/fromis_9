package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSettings
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSettingsFactory
import com.mikepenz.aboutlibraries.LibsBuilder
import java.io.File

class ActivitySettings : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelSettings by lazy { ViewModelProvider(this, ViewModelSettingsFactory(cacheDir, externalCacheDir))[ViewModelSettings::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.apply {
            updateCacheSize()
            cacheSize.observe(this@ActivitySettings) { binding.txtCurrentCache.text = getString(R.string.str_activity_settings_cache_mb, it) }
            version.observe(this@ActivitySettings) { binding.txtCurrentVersion.text = it }
            canUpdate.observe(this@ActivitySettings) { binding.btnUpdate.isEnabled = it }
            isDarkMode.observe(this@ActivitySettings) { binding.switchDarkMode.isChecked = it }
        }

        binding.btnRemoveCache.setOnClickListener {
            clearCache()
        }

        binding.txtLicenseDetails.setOnClickListener {
            LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withAboutIconShown(true)
                .start(this)
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateIsDarkMode(isChecked)
        }
    }

    private fun clearCache() {
        clearDirectory(cacheDir)
        externalCacheDir?.let { clearDirectory(it) }
        viewModel.updateCacheSize()
    }

    private fun clearDirectory(directory: File) {
        directory.listFiles()?.forEach { file ->
            file.deleteRecursively()
        }
    }
}