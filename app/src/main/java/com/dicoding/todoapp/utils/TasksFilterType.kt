package com.dicoding.todoapp.utils

enum class TasksFilterType {
    /**
     * Not filtering any tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}
