package com.ysm.android.pracapp1.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ysm.android.pracapp1.data.model.TodoItem
import com.ysm.android.pracapp1.ui.components.TodoCard
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
        onAddTodo = {
            listViewModel.addTodo(it)
        },
        onToggleTodo = {
            listViewModel.toggleTodo(it)
        },
        onDeleteTodo = onDelete
    )
}

@Composable
fun ListContent(
    todoItems: List<TodoItem>,
    onAddTodo: (String) -> Unit,
    onToggleTodo: (TodoItem) -> Unit,
    onDeleteTodo: (TodoItem) -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                placeholder = { Text("할 일 입력") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, // 엔터 키 모양을 '완료'로 변경
                    keyboardType = KeyboardType.Text // 일반 텍스트 입력 모드
                )
            )
            Button(
                onClick = {
                if (textInput.isNotBlank()) {
                    onAddTodo(textInput)
                    textInput = ""
                }
            }) { Text("추가") }
        }

        // 리스트 (LazyColumn은 대량의 데이터를 효율적으로 보여줌)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            items(todoItems, key = { it.id }) { item ->
                TodoCard(
                    item = item,
                    onToggle = {
                        onToggleTodo(item)
                    },
                    onDelete = {
                        onDeleteTodo(item)
                    }
                )
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