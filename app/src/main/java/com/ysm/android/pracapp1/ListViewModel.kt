package com.ysm.android.pracapp1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel: ViewModel() {

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList.asStateFlow()

    fun addTodo(item: String) {
        val newId = (_todoList.value.lastOrNull()?.id ?: 0) + 1
        val newItem = TodoItem(id = newId, task = item)

        _todoList.value += newItem
    }

    fun toggleTodo(item: TodoItem) {
//        val index = _todoList.indexOf(item)
//        if (index != -1) {
//            // Compose가 변경을 감지하게 하려면 객체를 새로 만들어 교체해야 함
//            _todoList[index] = item.copy(isDone = !item.isDone)
//        }
        _todoList.value = _todoList.value.map {
            if (it.id == item.id) it.copy(isDone = !it.isDone) else it
        }
    }

    fun removeTodo(item: TodoItem) {
//        _todoList.remove(item)
        _todoList.value = _todoList.value.filter { it.id != item.id }
    }
}