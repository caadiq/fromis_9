package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.data.DataChangelog
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryAppInfo
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSettings
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSettingsFactory
import com.mikepenz.aboutlibraries.LibsBuilder
import java.io.File

class ActivitySettings : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelSettings by lazy { ViewModelProvider(this, ViewModelSettingsFactory(RepositoryAppInfo(RetrofitService.apiAppInfo), cacheDir, externalCacheDir))[ViewModelSettings::class.java] }

    private val changeLogList = mutableListOf<DataChangelog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.apply {
            updateCacheSize()
            cacheSize.observe(this@ActivitySettings) { binding.txtCurrentCache.text = getString(R.string.str_activity_settings_cache_mb, it) }
            version.observe(this@ActivitySettings) { binding.txtCurrentVersion.text = it }
            canUpdate.observe(this@ActivitySettings) {
                binding.btnUpdate.isEnabled = it
                if (it) binding.txtCurrentVersion.setTextColor(getColor(R.color.green))
            }
            changeLog.observe(this@ActivitySettings) { changelog ->
                changeLogList.clear()
                changelog?.let { changeLogList.addAll(it) }
            }
        }

        binding.btnRemoveCache.setOnClickListener {
            clearCache()
        }

        binding.txtLicense.setOnClickListener {
            LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withAboutIconShown(true)
                .start(this)
        }

        binding.txtChangelog.setOnClickListener {
            val dialog = DialogChangelog().apply {
                setChangelogList(changeLogList)
            }
            dialog.show(supportFragmentManager, "DialogChangelog")
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