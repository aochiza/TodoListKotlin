package com.example.todolist.domain.usecase

import com.example.todolist.domain.repository.TodoRepository

class SetCompletedColorEnabledUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setCompletedColorEnabled(enabled)
}