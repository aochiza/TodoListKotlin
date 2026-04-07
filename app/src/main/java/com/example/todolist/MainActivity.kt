package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.todolist.data.local.TodoDatabase
import com.example.todolist.data.local.preferences.PreferencesManager
import com.example.todolist.data.model.TodoItemDto
import com.example.todolist.data.repository.TodoRepositoryImpl
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.usecase.*
import com.example.todolist.navigation.NavGraph
import com.example.todolist.presentation.viewmodel.TodoViewModel
import com.example.todolist.ui.theme.TodolistTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate started")  // Добавьте

        setContent {
            Log.d("MainActivity", "Setting content")  // Добавьте
            TodoListApp()
        }
    }
}

@Composable
fun TodoListApp() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Initialize database and preferences
    val database = remember { TodoDatabase.getDatabase(context) }
    val preferencesManager = remember { PreferencesManager(context) }
    val repository = remember { TodoRepositoryImpl(database.todoDao(), preferencesManager) }

    // Initialize use cases
    val getTodosUseCase = remember { GetTodosUseCase(repository) }
    val getTodoByIdUseCase = remember { GetTodoByIdUseCase(repository) }
    val addTodoUseCase = remember { AddTodoUseCase(repository) }
    val updateTodoUseCase = remember { UpdateTodoUseCase(repository) }
    val deleteTodoUseCase = remember { DeleteTodoUseCase(repository) }
    val toggleTodoUseCase = remember { ToggleTodoUseCase(repository) }
    val importTodosUseCase = remember { ImportTodosUseCase(repository) }
    val getCompletedColorEnabledUseCase = remember { GetCompletedColorEnabledUseCase(repository) }
    val setCompletedColorEnabledUseCase = remember { SetCompletedColorEnabledUseCase(repository) }

    val viewModel = remember {
        TodoViewModel(
            getTodosUseCase,
            getTodoByIdUseCase,
            addTodoUseCase,
            updateTodoUseCase,
            deleteTodoUseCase,
            toggleTodoUseCase,
            importTodosUseCase,
            getCompletedColorEnabledUseCase,
            setCompletedColorEnabledUseCase
        )
    }

    // Import initial data from JSON only once
    LaunchedEffect(Unit) {
        val todos = repository.getTodos().first()
        if (todos.isEmpty()) {
            val jsonString = context.assets.open("todos.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<List<TodoItemDto>>() {}.type
            val todoDtos: List<TodoItemDto> = gson.fromJson(jsonString, type)
            val todoItems = todoDtos.map { dto ->
                TodoItem(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    isCompleted = dto.isCompleted
                )
            }
            viewModel.importTodos(todoItems)
        }
    }

    TodolistTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(viewModel = viewModel)
        }
    }
}