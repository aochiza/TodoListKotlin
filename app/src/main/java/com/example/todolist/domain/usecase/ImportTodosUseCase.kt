package com.example.todolist.domain.usecase

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository

class ImportTodosUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todos: List<TodoItem>) = repository.importTodosFromJson(todos)
}