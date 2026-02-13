package com.ysm.android.pracapp1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ysm.android.pracapp1.data.model.TodoItem
import com.ysm.android.pracapp1.ui.theme.PracAppTheme

@Composable
fun TodoCard(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = { onToggle() }
            )
            Text(
                text = item.title,
                style = TextStyle(
                    textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (item.isDone) Color.Gray else Color.Black
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onDelete) {
                Icon(
                    modifier = Modifier.alpha(0.5f),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoCardPreview() {
    PracAppTheme {
        val currentDateTime: Long = System.currentTimeMillis()

        val mockData = listOf(
            TodoItem(id = 1, title = "복습하기", content = "Kotlin 기본 문법 복습하기", createdDate = currentDateTime, isDone = false, imagePath = "/"),
            TodoItem(id = 2, title = "Room DB 연결", content = "Room DB 연결 완료", createdDate = currentDateTime, isDone = true, imagePath = "/"),
        )

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockData, key = { it.id }) { item ->
                TodoCard(
                    item = item,
                    modifier = Modifier,
                    onToggle = { },
                    onDelete = { }
                )
            }
        }
    }
}