package com.ysm.android.pracapp1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val task: String,
    val isDone: Boolean = false
)