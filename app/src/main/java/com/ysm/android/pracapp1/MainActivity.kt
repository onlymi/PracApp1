package com.ysm.android.pracapp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ysm.android.pracapp1.ui.theme.PracAppTheme

class MainActivity : ComponentActivity() {
    private val counterViewModel: CounterViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracAppTheme {
                // 2. 전체 앱의 뼈대 설정
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 3. 내비게이션 호스트 실행 (모든 화면 전환의 중심)
                    // ViewModel을 전달하여 화면들이 데이터를 공유하게 합니다.
                    AppNavHost(
                        counterViewModel = this.counterViewModel,
                        listViewModel = this.listViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(counterViewModel: CounterViewModel, listViewModel: ListViewModel,
               modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "counter",
        modifier = modifier
    ) {
        composable("counter") {
            CounterScreen(counterViewModel) {
                if (navController.currentDestination?.route == "counter") {
                    navController.navigate("list") {
                        launchSingleTop = true
                    }
                }
            }
        }
        composable("list") {
            ListScreen(listViewModel) {
                // 뒤로 가기는 항상 popBackStack이 가장 안전합니다.
                if (navController.currentDestination?.route == "list") {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CounterScreen(viewModel: CounterViewModel, onNavigateToList: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "현재 카운트: ${viewModel.count}", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { viewModel.incrementCount() }) {
            Text("숫자 올리기")
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = onNavigateToList) {
            Text("리스트 화면으로 이동 →")
        }
    }
}

@Composable
fun ListScreen(viewModel: ListViewModel, onNavigateBack: () -> Unit) {
    var textInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onNavigateBack) {
            Text("← 뒤로")
        }

        // 입력창
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("할 일 입력") }
            )
            Button(onClick = {
                viewModel.addTodo(textInput)
                textInput = ""
            }) { Text("추가") }
        }

        // 리스트 (LazyColumn은 대량의 데이터를 효율적으로 보여줌)
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            items(viewModel.todoList) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(text = item, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracAppTheme {
        Greeting("Android")
    }
}