package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.presentation.ui.screen.TodoDetailScreen
import com.example.todolist.presentation.ui.screen.TodoListScreen
import com.example.todolist.presentation.viewmodel.TodoViewModel

@Composable
fun NavGraph(viewModel: TodoViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todo_list"
    ) {
        composable("todo_list") {
            TodoListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            "detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
            TodoDetailScreen(
                todoId = todoId,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}