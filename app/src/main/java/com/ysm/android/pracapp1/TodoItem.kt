package com.ysm.android.pracapp1

data class TodoItem(
    val id: Int,
    val task: String,
    val isDone: Boolean = false
)
