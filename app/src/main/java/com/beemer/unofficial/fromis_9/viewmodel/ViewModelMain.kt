package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactoryMain() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMain::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelMain() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelMain : ViewModel() {
    private val _currentFragmentTag = MutableLiveData("HOME")
    val currentFragmentTag: LiveData<String> = _currentFragmentTag

    fun setCurrentFragmentTag(tag: String) {
        _currentFragmentTag.value = tag
    }
}