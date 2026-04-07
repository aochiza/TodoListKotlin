package com.example.todolist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val importTodosUseCase: ImportTodosUseCase,
    private val getCompletedColorEnabledUseCase: GetCompletedColorEnabledUseCase,
    private val setCompletedColorEnabledUseCase: SetCompletedColorEnabledUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _completedColorEnabled = MutableStateFlow(false)
    val completedColorEnabled: StateFlow<Boolean> = _completedColorEnabled.asStateFlow()

    init {
        loadTodos()
        loadColorPreference()
    }

    fun loadTodos() {
        Log.d("TodoViewModel", "loadTodos called")  // Добавьте
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем IO поток
            try {
                withContext(Dispatchers.Main) {
                    _isLoading.value = true
                }
                Log.d("TodoViewModel", "Loading todos from repository")

                getTodosUseCase().collect { todosList ->
                    Log.d("TodoViewModel", "Received ${todosList.size} todos")
                    withContext(Dispatchers.Main) {
                        _todos.value = todosList
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                Log.e("TodoViewModel", "Error loading todos", e)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun loadColorPreference() {
        viewModelScope.launch {
            getCompletedColorEnabledUseCase().collect { enabled ->
                _completedColorEnabled.value = enabled
            }
        }
    }

    fun toggleCompletedColor(enabled: Boolean) {
        viewModelScope.launch {
            setCompletedColorEnabledUseCase(enabled)
        }
    }

    fun getTodoById(id: Int): TodoItem? {
        return _todos.value.find { it.id == id }
    }

    fun addTodo(title: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val newId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
            val newTodo = TodoItem(
                id = newId,
                title = title,
                description = description,
                isCompleted = false
            )
            addTodoUseCase(newTodo)
            _isLoading.value = false
        }
    }

    fun updateTodo(todo: TodoItem) {
        viewModelScope.launch {
            _isLoading.value = true
            updateTodoUseCase(todo)
            _isLoading.value = false
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            deleteTodoUseCase(id)
            _isLoading.value = false
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
        }
    }

    fun importTodos(todos: List<TodoItem>) {
        viewModelScope.launch {
            _isLoading.value = true
            importTodosUseCase(todos)
            _isLoading.value = false
        }
    }
}