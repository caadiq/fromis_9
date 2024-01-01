package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelMain : ViewModel() {
    private val _currentFragmentTag = MutableLiveData("HOME")
    val currentFragmentTag: LiveData<String> = _currentFragmentTag

    fun setCurrentFragmentTag(tag: String) {
        _currentFragmentTag.value = tag
    }
}