package com.ysm.android.pracapp1.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ysm.android.pracapp1.viewmodel.CounterViewModel
import com.ysm.android.pracapp1.viewmodel.ListViewModel
import kotlinx.coroutines.launch

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