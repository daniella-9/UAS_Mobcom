package com.dicoding.todoapp.ui.detail

import androidx.lifecycle.*
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class DetailTaskViewModel(private val taskRepository: TaskRepository): ViewModel() {

    private val _taskId = MutableLiveData<Int>()

    private val _task = _taskId.switchMap { id ->
        taskRepository.getTaskById(id)
    }
    val task: LiveData<Task> = _task

    fun setTaskId(taskId: Int) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
    }

    fun updateTitle(newTitle: String) {
        val currentTask = _task.value ?: return
        val updatedTask = currentTask.copy(title = newTitle)
        updateTask(updatedTask)
    }

    fun updateDescription(newDescription: String) {
        val currentTask = _task.value ?: return
        val updatedTask = currentTask.copy(description = newDescription)
        updateTask(updatedTask)
    }

    fun updateDueDate(newDueDateMillis: Long) {
        val currentTask = _task.value ?: return
        val updatedTask = currentTask.copy(dueDateMillis = newDueDateMillis)
        updateTask(updatedTask)
    }

    private fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(updatedTask)
        }
    }

    fun updateTaskDetails(title: String, description: String, dueDateMillis: Long) {
        val currentTask = _task.value ?: return
        val updatedTask = currentTask.copy(
            title = title,
            description = description,
            dueDateMillis = dueDateMillis
        )
        updateTask(updatedTask)
    }


    fun deleteTask() {
        viewModelScope.launch {
            _task.value?.let { taskRepository.deleteTask(it) }
        }
    }
}