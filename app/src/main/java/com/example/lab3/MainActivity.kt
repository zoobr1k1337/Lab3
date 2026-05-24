package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.lab3.navigation.AppNavigation
import com.example.lab3.ui.viewmodel.TasksViewModel
import com.example.lab3.ui.viewmodel.TasksViewModelFactory
import com.example.lab3.data.local.database.AppDatabase
import com.example.lab3.data.repository.TaskRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val repository = TaskRepository(database.taskDao())
            val tasksViewModel: TasksViewModel = viewModel(
                factory = TasksViewModelFactory(repository)
            )
            AppNavigation(viewModel = tasksViewModel)
        }
    }
}