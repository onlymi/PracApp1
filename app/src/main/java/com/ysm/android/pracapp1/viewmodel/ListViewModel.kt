package com.ysm.android.pracapp1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ysm.android.pracapp1.data.model.TodoDraft
import com.ysm.android.pracapp1.data.model.TodoItem
import com.ysm.android.pracapp1.data.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(private val repository: TodoRepository): ViewModel() {

//    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
//    val todoList: StateFlow<List<TodoItem>> = _todoList.asStateFlow()

    val todoList = repository.allTodos.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
)

    fun addTodo(draft: TodoDraft) {
//        val newId = (_todoList.value.lastOrNull()?.id ?: 0) + 1
        val newItem = TodoItem(
            title = draft.title,
            content = draft.content,
            date = draft.date,
            isDone = draft.isDone,
            imagePath = draft.imagePath
        )
//        _todoList.value += newItem
        if (newItem.title.isBlank() || newItem.content.isBlank()) return
        viewModelScope.launch {
            repository.insert(newItem)
        }
    }

    fun toggleTodo(item: TodoItem) {
//        val index = _todoList.indexOf(item)
//        if (index != -1) {
//            // Compose가 변경을 감지하게 하려면 객체를 새로 만들어 교체해야 함
//            _todoList[index] = item.copy(isDone = !item.isDone)
//        }
//        _todoList.value = _todoList.value.map {
//            if (it.id == item.id) it.copy(isDone = !it.isDone) else it
//        }
        viewModelScope.launch {
            val updatedItem = item.copy(isDone = !item.isDone)
            repository.update(updatedItem)
        }
    }

    fun removeTodo(item: TodoItem) {
//        _todoList.remove(item)
//        _todoList.value = _todoList.value.filter { it.id != item.id }
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}