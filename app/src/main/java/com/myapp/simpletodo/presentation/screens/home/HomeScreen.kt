package com.myapp.simpletodo.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapp.simpletodo.R
import com.myapp.simpletodo.domain.model.Task
import com.myapp.simpletodo.presentation.common.AppViewModelProvider
import com.myapp.simpletodo.presentation.common.TaskTopAppBar
import com.myapp.simpletodo.presentation.navigation.HomeDestination
import com.myapp.simpletodo.presentation.theme.SimpletodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddTask: () -> Unit,
    navigateToTaskDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TaskTopAppBar(
                title = HomeDestination.title,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddTask,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.content_desc_add_task)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            isLoading = homeUiState.isLoading,
            onTaskClick = navigateToTaskDetails,
            onTaskCheckedChange = { viewModel.toggleTaskCompleted(it) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>,
    isLoading: Boolean,
    onTaskClick: (Int) -> Unit,
    onTaskCheckedChange: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
        } else if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_state_no_tasks_message),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 32.dp)
            )
        } else {
            TaskList(
                taskList = taskList,
                onTaskClick = onTaskClick,
                onTaskCheckedChange = onTaskCheckedChange,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun TaskList(
    taskList: List<Task>,
    onTaskClick: (Int) -> Unit,
    onTaskCheckedChange: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onTaskClick = { onTaskClick(task.id) },
                onCheckedChange = { onTaskCheckedChange(task) }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onTaskClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    SimpletodoTheme {
        HomeBody(
            taskList = emptyList(),
            isLoading = false,
            onTaskClick = {},
            onTaskCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLoadingPreview() {
    SimpletodoTheme {
        HomeBody(
            taskList = emptyList(),
            isLoading = true,
            onTaskClick = {},
            onTaskCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenWithTasksPreview() {
    SimpletodoTheme {
        HomeBody(
            taskList = listOf(
                Task(1, "Buy groceries", "Milk, Eggs, Bread", false),
                Task(2, "Clean the house", "Living room and kitchen", true)
            ),
            isLoading = false,
            onTaskClick = {},
            onTaskCheckedChange = {}
        )
    }
}
