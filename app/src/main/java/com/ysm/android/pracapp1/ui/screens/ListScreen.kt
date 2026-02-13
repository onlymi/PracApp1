package com.ysm.android.pracapp1.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ysm.android.pracapp1.data.model.TodoDto
import com.ysm.android.pracapp1.data.model.TodoItem
import com.ysm.android.pracapp1.data.model.toDto
import com.ysm.android.pracapp1.ui.components.TodoCard
import com.ysm.android.pracapp1.ui.components.TodoDetailView
import com.ysm.android.pracapp1.ui.components.TodoInputForm
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.viewmodel.ListViewModel

sealed class TodoScreenState {
    object Idle : TodoScreenState()
    data class Detail(val item: TodoItem) : TodoScreenState()
    data class Edit(val item: TodoItem?) : TodoScreenState()
}

@Composable
fun ListScreen(
    listViewModel: ListViewModel,
    onDelete: (TodoItem) -> Unit
) {
    val todoItems by listViewModel.todoList.collectAsStateWithLifecycle()

    ListContent(
        todoItems = todoItems,
        onAddTodo = { todoDraft ->
            listViewModel.addTodo(todoDraft = todoDraft)
        },
        onToggleTodo = { item ->
            listViewModel.toggleTodo(item)
        },
        onEditTodo = { originItem, modifiedItem ->
            listViewModel.editTodo(
                originItem = originItem,
                updatedDto = modifiedItem
            )
        },
        onDeleteTodo = onDelete
    )
}

@Composable
fun ListContent(
    todoItems: List<TodoItem>,
    onAddTodo: (TodoDto) -> Unit,
    onToggleTodo: (TodoItem) -> Unit,
    onEditTodo: (TodoItem, TodoDto) -> Unit,
    onDeleteTodo: (TodoItem) -> Unit
) {
    var screenState by remember { mutableStateOf<TodoScreenState>(TodoScreenState.Idle) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    screenState = TodoScreenState.Edit(null)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가"
                )
            }
        }
    ) {
        paddingValues ->
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
                        modifier = Modifier.clickable {
                            screenState = TodoScreenState.Detail(item)
                        },
                        onToggle = { onToggleTodo(item) },
                        onDelete = { onDeleteTodo(item) }
                    )
                }
            }

            when (val state = screenState) {
                is TodoScreenState.Detail -> {
                    Dialog(
                        onDismissRequest = { screenState = TodoScreenState.Idle },
                        properties = DialogProperties(
                            usePlatformDefaultWidth = false
                        )

                    ) {
                        TodoDetailView(
                            todoDetail = state.item.toDto(),
                            onDismiss = { screenState = TodoScreenState.Idle },
                            onEditClick = { screenState = TodoScreenState.Edit(state.item) },
                            onDeleteClick = {
                                onDeleteTodo(state.item)
                                screenState = TodoScreenState.Idle
                            }
                        )
                    }
                }
                is TodoScreenState.Edit -> {
                    Dialog(
                        onDismissRequest = { screenState = TodoScreenState.Idle },
                        properties = DialogProperties(
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        TodoInputForm(
                            initialTodoDto = state.item?.toDto(),
                            onSave = { draft ->
                                if (state.item == null) onAddTodo(draft)
                                else onEditTodo(state.item, draft)
                                screenState = TodoScreenState.Idle
                            },
                            onDismiss = { screenState = TodoScreenState.Idle }
                        )
                    }
                }
                TodoScreenState.Idle -> {  }
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
            TodoItem(
                id = 1,
                title = "복습하기",
                content = "Kotlin 기본 문법 복습하기",
                createdDate = currentDateTime,
                isDone = false,
                imagePath = "/"
            ),
            TodoItem(
                id = 2,
                title = "Room DB 연결",
                content = "Room DB 연결 완료",
                createdDate = currentDateTime,
                isDone = true,
                imagePath = "/"
            ),
            TodoItem(
                id = 3,
                title = "운동 가기",
                content = "하체 운동 하기",
                createdDate = currentDateTime,
                isDone = false,
                imagePath = "/"
            )
        )

        ListContent(
            todoItems = mockData,
            onAddTodo = {},
            onToggleTodo = {},
            onEditTodo = { item, dto -> },
            onDeleteTodo = {}
        )
    }
}