package com.myapp.simpletodo.presentation.screens.task_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.simpletodo.R
import com.myapp.simpletodo.presentation.common.AppViewModelProvider
import com.myapp.simpletodo.presentation.common.TaskTopAppBar
import com.myapp.simpletodo.presentation.navigation.TaskDetailsDestination
import com.myapp.simpletodo.presentation.screens.common.TaskDetails
import com.myapp.simpletodo.presentation.theme.SimpletodoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navigateToEditTask: (Int) -> Unit,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TaskTopAppBar(
                title = TaskDetailsDestination.title,
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                actions = {
                    IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.content_desc_delete_task)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!uiState.isTaskLoading) {
                FloatingActionButton(
                    onClick = { navigateToEditTask(uiState.taskDetails.taskDetails.id) },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.content_desc_edit_task)
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isTaskLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            TaskDetailsBody(
                taskDetails = uiState.taskDetails.taskDetails,
                onToggleComplete = { viewModel.toggleTaskCompleted() },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }

        if (showDeleteConfirmationDialog) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    showDeleteConfirmationDialog = false
                    coroutineScope.launch {
                        viewModel.deleteTask()
                        navigateBack()
                    }
                },
                onDeleteCancel = { showDeleteConfirmationDialog = false }
            )
        }
    }
}

@Composable
private fun TaskDetailsBody(
    taskDetails: TaskDetails,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = taskDetails.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Checkbox(
                        checked = taskDetails.isCompleted,
                        onCheckedChange = { onToggleComplete() }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.task_details_label_description),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = taskDetails.description.ifEmpty { stringResource(R.string.label_no_description) },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.title_confirm_deletion)) },
        text = { Text(stringResource(R.string.dialog_delete_task_confirmation_message)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.button_cancel))
            }
        },
        confirmButton = {
            Button(
                onClick = onDeleteConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.button_delete))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TaskDetailsScreenPreview() {
    SimpletodoTheme {
        TaskDetailsBody(
            taskDetails = TaskDetails(
                id = 1,
                title = "Buy groceries for dinner party",
                description = "Milk, Eggs, Bread, Cheese, Wine, Fruits, Vegetables. Ensure all items are fresh.",
                isCompleted = false
            ),
            onToggleComplete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailsScreenCompletedPreview() {
    SimpletodoTheme {
        TaskDetailsBody(
            taskDetails = TaskDetails(
                id = 2,
                title = "Finish project report",
                description = "Complete the final sections and proofread.",
                isCompleted = true
            ),
            onToggleComplete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    SimpletodoTheme {
        DeleteConfirmationDialog(onDeleteConfirm = {}, onDeleteCancel = {})
    }
}
