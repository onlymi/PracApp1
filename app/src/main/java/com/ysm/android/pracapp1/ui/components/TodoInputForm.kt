package com.ysm.android.pracapp1.ui.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ysm.android.pracapp1.data.model.TodoDto
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.utils.toFormattedDateString
import com.ysm.android.pracapp1.utils.verticalScrollbar

@Composable
fun TodoInputForm(
    initialTodoDto: TodoDto? = null,
    onSave: (TodoDto) -> Unit,
    onDismiss: () -> Unit
) {
    val originalTitle: String = initialTodoDto?.title ?: ""
    val originalContent: String = initialTodoDto?.content ?: ""
    val originalImagePath: String? = initialTodoDto?.imagePath

    val isEditMode = initialTodoDto != null

    var title: String by remember { mutableStateOf(originalTitle) }
    var content: String by remember { mutableStateOf(originalContent) }
    val createDate: Long = remember { initialTodoDto?.createdDate ?: System.currentTimeMillis() }
    val modifiedDate: Long? = remember(isEditMode) { if (isEditMode) System.currentTimeMillis() else null }
    var taskIsDone: Boolean by remember { mutableStateOf(initialTodoDto?.isDone ?: false) }
    var selectedImagePath: String? by remember { mutableStateOf(originalImagePath) }

    val context = LocalContext.current
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)

            selectedImagePath = uri.toString()
        }
    }

    val isNotBlank: Boolean = title.isNotBlank() && content.isNotBlank()
    val isModified: Boolean =
        title.trim() != originalTitle || content.trim() != originalContent
                || selectedImagePath?.trim() != originalImagePath

    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .verticalScrollbar(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start),
                text = if (isEditMode) "기록 수정하기" else "새로운 추억 기록",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (selectedImagePath != null) {
                SubcomposeAsyncImage(
                    model = selectedImagePath,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                strokeWidth = 6.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF0F0F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                )
            }

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
                onClick = {
                    imageLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
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
                    enabled = isNotBlank && (if (isEditMode) isModified else true)
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