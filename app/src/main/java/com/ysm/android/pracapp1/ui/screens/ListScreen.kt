package com.ysm.android.pracapp1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ysm.android.pracapp1.data.model.TodoDraft
import com.ysm.android.pracapp1.data.model.TodoItem
import com.ysm.android.pracapp1.ui.components.TodoCard
import com.ysm.android.pracapp1.ui.components.TodoInputForm
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.viewmodel.ListViewModel

@Composable
fun ListScreen(
    listViewModel: ListViewModel,
    onDelete: (TodoItem) -> Unit
) {
    val todoItems by listViewModel.todoList.collectAsStateWithLifecycle()

    ListContent(
        todoItems = todoItems,
        onAddTodo = { draft ->
            listViewModel.addTodo(draft)
        },
        onToggleTodo = { item ->
            listViewModel.toggleTodo(item)
        },
        onDeleteTodo = onDelete
    )
}

@Composable
fun ListContent(
    todoItems: List<TodoItem>,
    onAddTodo: (TodoDraft) -> Unit,
    onToggleTodo: (TodoItem) -> Unit,
    onDeleteTodo: (TodoItem) -> Unit
) {
    var showInputForm by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showInputForm = true }, // 버튼 클릭 시 폼 열기
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "추가")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(todoItems, key = { it.id }) { item ->
                    TodoCard(
                        item = item,
                        onToggle = { onToggleTodo(item) },
                        onDelete = { onDeleteTodo(item) }
                    )
                }
            }

            if (showInputForm) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
//                        .clickable { showInputForm = false }, // 배경 클릭 시 닫기
                    contentAlignment = Alignment.Center
                ) {
                    TodoInputForm(
                        onSave = { draft ->
                            onAddTodo(draft)
                            showInputForm = false // 저장 후 닫기
                        },
                        onDismiss = { showInputForm = false } // 취소 시 닫기
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    PracAppTheme {
        val currentDateTime: Long = System.currentTimeMillis()

        val mockData = listOf(
            TodoItem(id = 1, title = "복습하기", content = "Kotlin 기본 문법 복습하기", date = currentDateTime, isDone = false, imagePath = "/"),
            TodoItem(id = 2, title = "Room DB 연결", content = "Room DB 연결 완료", date = currentDateTime, isDone = true, imagePath = "/"),
            TodoItem(id = 3, title = "운동 가기", content = "하체 운동 하기", date = currentDateTime, isDone = false, imagePath = "/")
        )

        ListContent(
            todoItems = mockData,
            onAddTodo = {},
            onToggleTodo = {},
            onDeleteTodo = {}
        )
    }
}