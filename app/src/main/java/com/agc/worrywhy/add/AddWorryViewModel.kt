package com.agc.worrywhy.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.Worry
import com.agc.worrywhy.WorryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorryViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    fun addWorry(text: String) {
        viewModelScope.launch {
            worryDao.addWorry(Worry(text))
        }
    }
}