package com.ysm.android.pracapp1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.ysm.android.pracapp1.data.model.TodoDto
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.utils.toFormattedDateString

@Composable
fun TodoDetailView(
    todoDetail: TodoDto,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clickable(enabled = false) { },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (!todoDetail.imagePath.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = todoDetail.imagePath,
                    contentDescription = "선택된 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(300.dp)
                        .aspectRatio(1f)
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

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = todoDetail.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            val dateText =
                if (todoDetail.modifiedDate != null) todoDetail.modifiedDate.toFormattedDateString()
                else todoDetail.createdDate.toFormattedDateString()

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = "마지막 수정일 : $dateText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )

            HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = todoDetail.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDeleteClick) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onEditClick) {
                    Text("수정")
                }
                Button(onClick = onDismiss, modifier = Modifier.padding(start = 8.dp)) {
                    Text("확인")
                }
            }
        }
    }
}

@Preview(
    name = "Pixel 9",
    device = Devices.PIXEL_9,
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun TodoDetailPreview() {
    PracAppTheme {
        val currentDateTime: Long = System.currentTimeMillis()
        val mockData = TodoDto(
            title = "복습하기",
            content = "Kotlin 기본 문법 복습하기",
            createdDate = currentDateTime,
            isDone = false,
            imagePath = "https://picsum.photos/1000/1000"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            TodoDetailView(
                todoDetail = mockData,
                onDismiss = {},
                onEditClick = {},
                onDeleteClick = {},
            )
        }
    }
}