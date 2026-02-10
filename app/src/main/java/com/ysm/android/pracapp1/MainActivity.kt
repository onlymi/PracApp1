package com.ysm.android.pracapp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ysm.android.pracapp1.data.local.AppDatabase
import com.ysm.android.pracapp1.data.repository.TodoRepository
import com.ysm.android.pracapp1.ui.screens.MainScreen
import com.ysm.android.pracapp1.ui.theme.PracAppTheme
import com.ysm.android.pracapp1.viewmodel.CounterViewModel
import com.ysm.android.pracapp1.viewmodel.ListViewModel
import com.ysm.android.pracapp1.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { TodoRepository(database.todoDao()) }
    private val counterViewModel: CounterViewModel by viewModels()
//    private val listViewModel: ListViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels {
        TodoViewModelFactory(repository)
    }

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
                MainScreen(counterViewModel, listViewModel)
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