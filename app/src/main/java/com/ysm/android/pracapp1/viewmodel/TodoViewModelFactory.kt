package com.ysm.android.pracapp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ysm.android.pracapp1.data.repository.TodoRepository

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 1. 요청받은 뷰모델이 ListViewModel 타입인지 확인합니다.
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            // 2. 맞다면 준비된 repository를 넣어서 생성합니다.
            return ListViewModel(repository) as T
        }

        // 3. 만약 다른 뷰모델(예: CounterViewModel)을 요청했다면?
        // 현재는 ListViewModel 전용이므로 예외를 발생시킵니다.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}