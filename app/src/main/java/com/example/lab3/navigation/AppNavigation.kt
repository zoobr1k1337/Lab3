package com.example.lab3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab3.ui.screens.AddEditTaskScreen
import com.example.lab3.ui.screens.TaskDetailsScreen
import com.example.lab3.ui.screens.TasksListScreen
import com.example.lab3.ui.viewmodel.TasksViewModel

object AppRoutes {
    const val TASKS_LIST = "tasks_list"
    const val TASK_ADD = "task_add"
    const val TASK_EDIT = "task_edit/{taskId}"
    const val TASK_DETAILS = "task_details/{taskId}"

    fun taskEdit(taskId: Int) = "task_edit/$taskId"
    fun taskDetails(taskId: Int) = "task_details/$taskId"
}

@Composable
fun AppNavigation(
    viewModel: TasksViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = AppRoutes.TASKS_LIST) {
        composable(AppRoutes.TASKS_LIST) {
            val uiState by viewModel.tasksListUiState.collectAsState()
            TasksListScreen(
                uiState = uiState,
                onAddClick = {
                    viewModel.prepareNewTask()
                    navController.navigate(AppRoutes.TASK_ADD)
                },
                onTaskClick = { taskId ->
                    navController.navigate(AppRoutes.taskDetails(taskId))
                }
            )
        }

        composable(AppRoutes.TASK_ADD) {
            val uiState by viewModel.addEditTaskUiState.collectAsState()
            AddEditTaskScreen(
                uiState = uiState,
                onTitleChange = viewModel::updateTitle,
                onDescriptionChange = viewModel::updateDescription,
                onPriorityChange = viewModel::updatePriority,
                onSaveClick = {
                    viewModel.saveTask()
                    navController.popBackStack()
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.TASK_EDIT,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            LaunchedEffect(taskId) {
                if (taskId != null) viewModel.prepareEditTask(taskId)
            }
            val uiState by viewModel.addEditTaskUiState.collectAsState()

            AddEditTaskScreen(
                uiState = uiState,
                onTitleChange = viewModel::updateTitle,
                onDescriptionChange = viewModel::updateDescription,
                onPriorityChange = viewModel::updatePriority,
                onSaveClick = {
                    viewModel.saveTask()
                    navController.popBackStack()
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.TASK_DETAILS,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            LaunchedEffect(taskId) {
                if (taskId != null) viewModel.prepareDetails(taskId)
            }
            val uiState by viewModel.taskDetailsUiState.collectAsState()

            TaskDetailsScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onEditClick = { editId -> navController.navigate(AppRoutes.taskEdit(editId)) },
                onDeleteClick = { delId ->
                    viewModel.deleteTask(delId)
                    navController.popBackStack()
                },
                onCompletedChange = viewModel::updateCompleted
            )
        }
    }
}