package com.ysm.android.pracapp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ysm.android.pracapp1.viewmodel.CounterViewModel

@Composable
fun CounterScreen(counterViewModel: CounterViewModel, onNavigateToList: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "현재 카운트: ${counterViewModel.count}", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { counterViewModel.incrementCount() }) {
            Text("숫자 올리기")
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = onNavigateToList) {
            Text("리스트 화면으로 이동 →")
        }
    }
}