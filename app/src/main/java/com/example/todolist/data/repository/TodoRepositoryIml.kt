package com.example.todolist.data.repository

import com.example.todolist.data.local.TodoDao
import com.example.todolist.data.local.preferences.PreferencesManager
import com.example.todolist.data.model.TodoEntity
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val todoDao: TodoDao,
    private val preferencesManager: PreferencesManager
) : TodoRepository {

    override fun getTodos(): Flow<List<TodoItem>> {
        return todoDao.getAllTodos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getTodoById(id: Int): TodoItem? {
        return todoDao.getTodoById(id)?.toDomainModel()
    }

    override suspend fun addTodo(todo: TodoItem) {
        todoDao.insertTodo(todo.toEntity())
    }

    override suspend fun updateTodo(todo: TodoItem) {
        todoDao.updateTodo(todo.toEntity())
    }

    override suspend fun deleteTodo(id: Int) {
        todoDao.deleteTodo(id)
    }

    override suspend fun toggleTodo(id: Int) {
        val todo = todoDao.getTodoById(id)
        todo?.let {
            val updatedTodo = it.copy(isCompleted = !it.isCompleted)
            todoDao.updateTodo(updatedTodo)
        }
    }

    override suspend fun importTodosFromJson(todos: List<TodoItem>) {
        todoDao.deleteAllTodos()
        todoDao.insertAllTodos(todos.map { it.toEntity() })
    }

    override fun isCompletedColorEnabled(): Flow<Boolean> {
        return preferencesManager.isCompletedColorEnabled
    }

    override suspend fun setCompletedColorEnabled(enabled: Boolean) {
        preferencesManager.setCompletedColorEnabled(enabled)
    }
}

fun TodoItem.toEntity(): TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}

fun TodoEntity.toDomainModel(): TodoItem {
    return TodoItem(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}