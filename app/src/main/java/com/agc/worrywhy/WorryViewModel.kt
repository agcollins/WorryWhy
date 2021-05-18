package com.agc.worrywhy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WorryViewModel(application: Application) : AndroidViewModel(application) {
    private val database by lazy {
        Room.databaseBuilder<AppDatabase>(
            application.applicationContext,
            AppDatabase::class.java,
            "app"
        ).build()
    }

    private val dao by lazy {
        database.worryDao()
    }

    val worries by lazy {
        dao.getAll().flowOn(Dispatchers.Default).asLiveData()
    }

    fun addWorry(text: String) {
        viewModelScope.launch {
            dao.addWorry(Worry(text))
        }
    }

    fun removeAllWorries() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}

