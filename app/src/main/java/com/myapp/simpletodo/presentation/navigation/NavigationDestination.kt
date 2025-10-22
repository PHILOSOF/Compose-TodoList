package com.myapp.simpletodo.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavigationDestination {

    val route: String
    val title: String // Or Int for string resource
}

// Define specific destinations

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Tasks"
}

object AddTaskDestination : NavigationDestination {
    override val route = "add_task"
    override val title = "Add Task"
}

object TaskDetailsDestination : NavigationDestination {
    override val route = "task_details"
    override val title = "Task Details"
    const val TASK_ID_ARG = "taskId"
    val routeWithArgs = "$route/{$TASK_ID_ARG}"
    val arguments = listOf(
        navArgument(TASK_ID_ARG) { type = NavType.IntType }
    )
}

object EditTaskDestination : NavigationDestination {
    override val route = "edit_task"
    override val title = "Edit Task"
    const val TASK_ID_ARG = "taskId"
    val routeWithArgs = "$route/{$TASK_ID_ARG}"
    val arguments = listOf(
        navArgument(TASK_ID_ARG) { type = NavType.IntType }
    )
}