package com.ysm.android.pracapp1.data.model

data class TodoDto(
    val title: String,
    val content: String,
    val createdDate: Long,
    val modifiedDate: Long? = null,
    val isDone: Boolean = false,
    val imagePath: String? = null
)
