package com.myapp.simpletodo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.myapp.simpletodo.presentation.screens.add_task.AddTaskScreen
import com.myapp.simpletodo.presentation.screens.edit_task.EditTaskScreen
import com.myapp.simpletodo.presentation.screens.home.HomeScreen
import com.myapp.simpletodo.presentation.screens.task_details.TaskDetailsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddTask = { navController.navigate(AddTaskDestination.route) },
                navigateToTaskDetails = { taskId ->
                    navController.navigate("${TaskDetailsDestination.route}/$taskId")
                }
            )
        }
        composable(route = AddTaskDestination.route) {
            AddTaskScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TaskDetailsDestination.routeWithArgs,
            arguments = TaskDetailsDestination.arguments
        ) { navBackStackEntry ->
            // val taskId = navBackStackEntry.arguments?.getInt(TaskDetailsDestination.TASK_ID_ARG)
            // taskId is automatically passed to ViewModel through SavedStateHandle
            TaskDetailsScreen(
                navigateToEditTask = { taskId ->
                    navController.navigate("${EditTaskDestination.route}/$taskId")
                },
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = EditTaskDestination.routeWithArgs,
            arguments = EditTaskDestination.arguments
        ) {
            EditTaskScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}