package com.example.lab3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab3.domain.model.TaskItem
import com.example.lab3.domain.model.getIndicatorColor
import com.example.lab3.domain.model.getBadgeBackgroundColor
import com.example.lab3.domain.model.getBadgeTextColor
import com.example.lab3.domain.model.toDisplayName
import com.example.lab3.ui.viewmodel.TasksListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListScreen(
    uiState: TasksListUiState,
    onAddClick: () -> Unit,
    onTaskClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої завдання", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.tasks) { task ->
                TaskListItem(task = task, onClick = { onTaskClick(task.id) })
            }
        }
    }
}

@Composable
fun TaskListItem(task: TaskItem, onClick: () -> Unit) {
    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val cardAlpha = if (task.isCompleted) 0.6f else 1f

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).alpha(cardAlpha),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(task.priority.getIndicatorColor())
            )

            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = textDecoration
                )
                if (task.dueDate.isNotEmpty()) {
                    Text(
                        text = task.dueDate,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(task.priority.getBadgeBackgroundColor())
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = task.priority.toDisplayName().uppercase(),
                        color = task.priority.getBadgeTextColor(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}