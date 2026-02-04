package com.ysm.android.pracapp1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ListViewModel: ViewModel() {

    private val _todoList = mutableStateListOf<String>()
    val todoList: List<String> = _todoList

    fun addTodo(item: String) {
        if (item.isNotBlank()) _todoList.add(item)
    }

    fun removeTodo(item: String) {
        _todoList.remove(item)
    }
}