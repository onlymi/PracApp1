package com.ysm.android.pracapp1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ysm.android.pracapp1.data.model.TodoDto
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.utils.toFormattedDateString

@Composable
fun TodoInputForm(
    initialTodoDto: TodoDto? = null,
    onSave: (TodoDto) -> Unit,
    onDismiss: () -> Unit
) {
    var title: String by remember { mutableStateOf(initialTodoDto?.title ?: "") }
    var content: String by remember { mutableStateOf(initialTodoDto?.content ?: "") }
    var taskIsDone: Boolean by remember { mutableStateOf(initialTodoDto?.isDone ?: false) }
    var selectedImagePath: String? by remember { mutableStateOf(initialTodoDto?.imagePath) }

    val isEditMode = initialTodoDto != null
    println(isEditMode)

    val createDate: Long = remember { initialTodoDto?.createdDate ?: System.currentTimeMillis() }
    val modifiedDate: Long? = remember(isEditMode) { if (isEditMode) System.currentTimeMillis() else null }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "오늘의 기록",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Column {
                    Text(
                        text = "기록 시점",
                        style = MaterialTheme.typography.labelSmall
                    )

                    Text(
                        text = createDate.toFormattedDateString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Column {
                        Text(
                            text = "수정 시점",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            text = modifiedDate!!.toFormattedDateString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("이 장소의 한 문장은 무엇인가요?") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(alpha = 0.1f)
                )
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("어떤 것이 생각이 나시나요?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedButton(
                onClick = { /* 이미지 피커 실행 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (selectedImagePath == null) "사진 추가" else "사진 변경")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("취소")
                }
                Button(
                    onClick = {
                        val draft = TodoDto(
                            title = title,
                            content = content,
                            createdDate = createDate,
                            isDone = taskIsDone,
                            imagePath = selectedImagePath
                        )
                        onSave(draft)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank() && content.isNotBlank()
                ) {
                    Text(if (isEditMode) "수정하기" else "저장하기")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoInputFormPreview() {
    PracAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            TodoInputForm(
                onSave = { draft -> println("Saved: $draft") },
                onDismiss = { }
            )
        }
    }
}