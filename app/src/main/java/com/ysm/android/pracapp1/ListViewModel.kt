package com.ysm.android.pracapp1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun addTodo(item: String) {
//        val newId = (_todoList.value.lastOrNull()?.id ?: 0) + 1
//        val newItem = TodoItem(id = newId, task = item)
//        _todoList.value += newItem
        viewModelScope.launch {
            repository.insert(TodoItem(task = item))
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
            repository.update(item.copy(isDone = !item.isDone))
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