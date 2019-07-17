package io.github.lettucech.example.android.viewanimations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.lettucech.example.android.viewanimations.model.CustomLog

/**
 * Created by Brian Ho on 2019-07-17.
 */
class LogConsoleViewModel : ViewModel() {
    private val logPanelOpened = MutableLiveData<Boolean>().apply { value = false }
    private val logList = MutableLiveData<ArrayList<CustomLog>>().apply {
        value = ArrayList()
    }

    fun getLogPanelOpened() = logPanelOpened as LiveData<Boolean>

    fun setLogPanelOpened(opened: Boolean) {
        logPanelOpened.value = opened
    }

    fun getLogList() = logList as LiveData<ArrayList<CustomLog>>

    fun addLog(message: String) {
        val log = CustomLog(message)
        logList.value?.let {
            it.add(log)
            logList.value = it
        }
    }
}