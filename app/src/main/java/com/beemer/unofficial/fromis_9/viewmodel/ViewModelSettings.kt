package com.beemer.unofficial.fromis_9.viewmodel

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataChangelog
import com.beemer.unofficial.fromis_9.datastore.Preferences
import com.beemer.unofficial.fromis_9.repository.RepositoryAppInfo
import com.beemer.unofficial.fromis_9.utils.Event
import com.beemer.unofficial.fromis_9.utils.MyApplication
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

class ViewModelSettings(private val repository: RepositoryAppInfo, private val cacheDir: File, private val externalCacheDir: File?) : ViewModel() {
    private val context = MyApplication.instance

    private val _cacheSize = MutableLiveData<String>()
    val cacheSize: LiveData<String> = _cacheSize

    private val _version = MutableLiveData<String?>()
    val version: LiveData<String?> = _version

    private val _canUpdate = MutableLiveData<Boolean>(false)
    val canUpdate: LiveData<Boolean> = _canUpdate

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    private val _changeLog = MutableLiveData<List<DataChangelog>>()
    val changeLog: LiveData<List<DataChangelog>> = _changeLog

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    init {
        loadPreferences()
        updateCacheSize()
        getAppVersion()
        getAppInfo()
    }

    fun updateIsDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            Preferences.setIsDarkMode(context, isDarkMode)
            _isDarkMode.value = isDarkMode
        }
    }

    fun updateCacheSize() {
        val cacheSizeValue = getCacheSize()
        _cacheSize.postValue(cacheSizeValue)
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _isDarkMode.value = Preferences.getIsDarkMode(context)
        }
    }

    private fun getCacheSize(): String {
        val internalCacheSize = getDirectorySize(cacheDir)
        val externalCacheSize = externalCacheDir?.let { getDirectorySize(it) } ?: 0
        val totalCacheSize = internalCacheSize + externalCacheSize
        val totalCacheSizeInMB = BigDecimal(totalCacheSize / (1024.0 * 1024.0)).setScale(2, RoundingMode.HALF_UP).toDouble()
        return NumberFormat.getNumberInstance().format(totalCacheSizeInMB)
    }

    private fun getDirectorySize(directory: File): Long {
        var size: Long = 0
        directory.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getDirectorySize(file) else file.length()
        }
        return size
    }

    private fun getAppVersion() {
        try {
            val packageInfo = context.packageManager.getPackageInfo(MyApplication.instance.packageName, 0)
            val versionName = packageInfo.versionName

           _version.value = versionName
        } catch (_: PackageManager.NameNotFoundException) {
            _version.value = null
        }
    }

    private fun getAppInfo() {
        viewModelScope.launch {
            try {
                val response = repository.getAppInfo()
                val latestVersion = response.first().version
                val currentVersion = version.value
                _changeLog.value = response.map {
                    DataChangelog(
                        version = it.version,
                        release = it.release,
                        changelog = it.changelog
                    )
                }
                _canUpdate.value = latestVersion != currentVersion
            } catch (_: Exception) {
                _errorMessage.value = Event("앱 정보를 불러오지 못했습니다.")
            }
        }
    }
}