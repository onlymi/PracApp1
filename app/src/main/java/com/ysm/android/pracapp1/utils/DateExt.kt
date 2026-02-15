package com.ysm.android.pracapp1.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateString(): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
    return formatter.format(Date(this))
}