package com.ysm.android.pracapp1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CounterViewModel: ViewModel() {

    var count by mutableIntStateOf(0)
        private set // 외부에서는 수정 불가하게 캡슐화

    fun incrementCount() {
        count++
    }
}