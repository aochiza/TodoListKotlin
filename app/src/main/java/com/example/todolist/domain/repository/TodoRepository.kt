package com.example.todolist.domain.repository

import com.example.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<TodoItem>>
    suspend fun getTodoById(id: Int): TodoItem?
    suspend fun addTodo(todo: TodoItem)
    suspend fun updateTodo(todo: TodoItem)
    suspend fun deleteTodo(id: Int)
    suspend fun toggleTodo(id: Int)
    suspend fun importTodosFromJson(todos: List<TodoItem>)
    fun isCompletedColorEnabled(): Flow<Boolean>
    suspend fun setCompletedColorEnabled(enabled: Boolean)
}