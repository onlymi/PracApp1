package com.ysm.android.pracapp1

import android.annotation.SuppressLint
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val counterViewModel: CounterViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracAppTheme {
                // 2. 전체 앱의 뼈대 설정
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    // 3. 내비게이션 호스트 실행 (모든 화면 전환의 중심)
//                    // ViewModel을 전달하여 화면들이 데이터를 공유하게 합니다.
//                    AppNavHost(
//                        counterViewModel = this.counterViewModel,
//                        listViewModel = this.listViewModel,
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                MainApp(counterViewModel, listViewModel)
            }
        }
    }
}

//@Composable
//fun AppNavHost(counterViewModel: CounterViewModel, listViewModel: ListViewModel,
//               modifier: Modifier = Modifier) {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "counter",
//        modifier = modifier
//    ) {
//        composable("counter") {
//            CounterScreen(counterViewModel) {
//                if (navController.currentDestination?.route == "counter") {
//                    navController.navigate("list") {
//                        launchSingleTop = true
//                    }
//                }
//            }
//        }
//        composable("list") {
//            ListScreen(listViewModel) {
//                if (navController.currentDestination?.route == "list") {
//                    navController.popBackStack()
//                }
//            }
//        }
//    }
//}

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

@Composable
fun ListScreen(listViewModel: ListViewModel, onDelete: (TodoItem) -> Unit) {
    var textInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 입력창
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("할 일 입력") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, // 엔터 키 모양을 '완료'로 변경
                    keyboardType = KeyboardType.Text // 일반 텍스트 입력 모드
                )
            )
            Button(onClick = {
                    if (textInput.isNotBlank()) { // 빈 칸 입력 방지
                    listViewModel.addTodo(textInput)
                    textInput = ""
                }
            }) { Text("추가") }
        }

        // 리스트 (LazyColumn은 대량의 데이터를 효율적으로 보여줌)
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            items(listViewModel.todoList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                    Row(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Checkbox(
                            checked = item.isDone,
                            onCheckedChange = { listViewModel.toggleTodo(item) } // 클릭 시 toggleTodo 실행
                        )
                        Text(
                            text = item.task,
                            style = TextStyle(
                                // item.isDone이 true면 취소선을 긋고, 아니면 아무것도 안 함
                                textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None,
                                color = if (item.isDone) Color.Gray else Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(onClick = {
                            onDelete(item)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = androidx.compose.ui.graphics.Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(counterViewModel: CounterViewModel, listViewModel: ListViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 현재 경로 파악 (상단바 제목 및 아이콘 제어용)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (currentRoute == "counter") "카운터" else "할 일 목록")
                },
                navigationIcon = {
                    if (currentRoute == "list") {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "counter",
                    onClick = {
                        navController.navigate("counter") {
                            popUpTo("counter") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("카운터") }
                )
                NavigationBarItem(
                    selected = currentRoute == "list",
                    onClick = {
                        navController.navigate("list") { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("목록") }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "counter",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("counter") {
                CounterScreen(counterViewModel) {
                    navController.navigate("list") { launchSingleTop = true }
                }
            }
            composable("list") {
                ListScreen(
                    listViewModel = listViewModel,
                    onDelete = { item ->
                        listViewModel.removeTodo(item)
                        scope.launch {
                            snackbarHostState.showSnackbar("'${item}' 삭제 완료")
                        }
                    }
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracAppTheme {
//        MainApp(counterViewModel = CounterViewModel(), listViewModel = ListViewModel())
        val lvm: ListViewModel = ListViewModel()
        lvm.addTodo("hello")
        lvm.addTodo("hahaha")
        ListScreen(lvm) { }
    }
}