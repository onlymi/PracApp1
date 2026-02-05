package com.ysm.android.pracapp1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ListViewModel: ViewModel() {

    private val _todoList = mutableStateListOf<TodoItem>()
    val todoList: List<TodoItem> = _todoList

    fun addTodo(item: String) {
        val newId = (_todoList.lastOrNull()?.id ?: 0) + 1
        _todoList.add(TodoItem(id = newId, task = item))
    }

    fun toggleTodo(item: TodoItem) {
        val index = _todoList.indexOf(item)
        if (index != -1) {
            // Compose가 변경을 감지하게 하려면 객체를 새로 만들어 교체해야 함
            _todoList[index] = item.copy(isDone = !item.isDone)
        }
    }

    fun removeTodo(item: TodoItem) {
        _todoList.remove(item)
    }
}