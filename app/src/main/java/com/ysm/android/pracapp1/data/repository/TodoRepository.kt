package com.ysm.android.pracapp1.data.repository

import com.ysm.android.pracapp1.data.local.TodoDao
import com.ysm.android.pracapp1.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    suspend fun insert(todoItem: TodoItem) = todoDao.insertTodo(todoItem)
    suspend fun update(todoItem: TodoItem) = todoDao.updateTodo(todoItem)
    suspend fun delete(todoItem: TodoItem) = todoDao.deleteTodo(todoItem)
}