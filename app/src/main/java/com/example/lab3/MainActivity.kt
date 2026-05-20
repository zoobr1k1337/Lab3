package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab3.navigation.AppNavigation
import com.example.lab3.ui.viewmodel.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val tasksViewModel: TasksViewModel = viewModel()
            AppNavigation(viewModel = tasksViewModel)
        }
    }
}
