package com.myapp.simpletodo.presentation.screens.edit_task

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.simpletodo.presentation.common.AppViewModelProvider
import com.myapp.simpletodo.presentation.common.TaskTopAppBar
import com.myapp.simpletodo.presentation.navigation.EditTaskDestination
import com.myapp.simpletodo.presentation.screens.add_task.AddTaskBody // Reusing AddTaskBody for the form and save button
import com.myapp.simpletodo.presentation.screens.common.TaskDetails
import com.myapp.simpletodo.presentation.screens.common.TaskUiState
import com.myapp.simpletodo.presentation.theme.SimpletodoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = EditTaskDestination.title,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        AddTaskBody(
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTask()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskScreenPreview() {
    SimpletodoTheme {
        AddTaskBody(
            taskUiState = TaskUiState(
                TaskDetails(id = 1, title = "Existing Task", description = "Existing Description"),
                isEntryValid = true
            ),
            onTaskValueChange = {},
            onSaveClick = {}
        )
    }
}