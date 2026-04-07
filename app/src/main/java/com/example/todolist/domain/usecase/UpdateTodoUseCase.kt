// UpdateTodoUseCase.kt
package com.example.todolist.domain.usecase

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository

class UpdateTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: TodoItem) = repository.updateTodo(todo)
}