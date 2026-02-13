package com.ysm.android.pracapp1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val createdDate: Long,
    val modifiedDate: Long? = null,
    val isDone: Boolean = false,
    val imagePath: String? = null
)

fun TodoItem.toDto(): TodoDto = TodoDto(
    title = title,
    content = content,
    createdDate = createdDate,
    modifiedDate = modifiedDate,
    isDone = isDone,
    imagePath = imagePath
)