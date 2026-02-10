package com.ysm.android.pracapp1.data.model

data class TodoDraft(
    val title: String,
    val content: String,
    val date: Long,
    val isDone: Boolean = false,
    val imagePath: String? = null
)
