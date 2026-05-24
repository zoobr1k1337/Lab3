package com.example.lab3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab3.domain.model.getBadgeBackgroundColor
import com.example.lab3.domain.model.toDisplayName
import com.example.lab3.domain.model.getBadgeTextColor
import com.example.lab3.ui.viewmodel.TaskDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    uiState: TaskDetailsUiState,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onCompletedChange: (Int, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Деталі завдання", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3))
            )
        }
    ) { paddingValues ->
        val task = uiState.task

        if (task != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(task.dueDate, color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(task.priority.getBadgeBackgroundColor())
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                task.priority.toDisplayName().uppercase(),
                                color = task.priority.getBadgeTextColor(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Опис", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(task.description, fontWeight = FontWeight.Medium)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = { onCompletedChange(task.id, it) }
                        )
                        Text("Позначити як виконане")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { onEditClick(task.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Редагувати")
                }

                OutlinedButton(
                    onClick = { onDeleteClick(task.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Видалити")
                }

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Назад")
                }
            }
        }
    }
}