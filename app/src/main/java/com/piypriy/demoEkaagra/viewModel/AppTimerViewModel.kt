package com.piypriy.demoEkaagra.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.piypriy.demoEkaagra.AppTimer
import com.piypriy.demoEkaagra.AppTimerList
import com.piypriy.demoEkaagra.datastore.appTimerDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppTimerViewModel(application: Application) : AndroidViewModel(application) {

    fun saveAppTimer(
        appName: String,
        mode: String, // "TIMER" or "RANGE"
        duration: Int = 0,
        startHour: Int = 0,
        startMinute: Int = 0,
        endHour: Int = 0,
        endMinute: Int = 0
    ) {
        val appTimer = AppTimer.newBuilder()
            .setAppName(appName)
            .setMode(mode)
            .setDurationMinutes(duration)
            .setStartHour(startHour)
            .setStartMinute(startMinute)
            .setEndHour(endHour)
            .setEndMinute(endMinute)
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            val current = getApplication<Application>().appTimerDataStore.data.first()

            val newList = current.timersList
                .filterNot { it.appName == appName } // remove previous
                .toMutableList()
                .apply { add(appTimer) }

            val updatedList = AppTimerList.newBuilder()
                .addAllTimers(newList)
                .build()


            getApplication<Application>().appTimerDataStore.updateData {
                updatedList
            }
        }
    }
}
