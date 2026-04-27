package com.sqlmimo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sqlmimo.ui.theme.Brand
import com.sqlmimo.ui.theme.SuccessGreen
import com.sqlmimo.ui.theme.SuccessGreenBg
import com.sqlmimo.viewmodel.AppViewModel

@Composable
fun ProfileScreen(vm: AppViewModel) {
    val xp by vm.xp.collectAsState()
    val level by vm.level.collectAsState()
    val streak by vm.streak.collectAsState()
    val correctAnswers by vm.correctAnswers.collectAsState()
    val totalAttempts by vm.totalAttempts.collectAsState()
    val examDone by vm.examDone.collectAsState()
    val examScore by vm.examScore.collectAsState()
    val isDark by vm.isDarkTheme.collectAsState()

    val accuracy = if (totalAttempts > 0) (correctAnswers * 100 / totalAttempts) else 0
    val completedIds by vm.completedIds.collectAsState()
    val completedModules = vm.modules.count { m -> m.lessons.all { completedIds.contains(it.id) } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar + nombre
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧑‍💻", fontSize = 32.sp)
                }
                Spacer(Modifier.height(10.dp))
                Text("Mi Perfil", fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground)
                Text("Nivel $level · $xp XP acumulados", fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // ── Apariencia ────────────────────────────────────────
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Apariencia", fontWeight = FontWeight.Medium, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = Brand,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    if (isDark) "Tema oscuro" else "Tema claro",
                                    fontWeight = FontWeight.Medium, fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Toca para cambiar",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isDark,
                            onCheckedChange = { vm.toggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Brand
                            )
                        )
                    }
                }
            }
        }

        // ── Estadísticas ──────────────────────────────────────
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estadísticas", fontWeight = FontWeight.Medium, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatBox(modifier = Modifier.weight(1f), emoji = "🔥", value = "$streak", label = "Racha")
                        StatBox(modifier = Modifier.weight(1f), emoji = "⭐", value = "$xp", label = "XP Total")
                        StatBox(modifier = Modifier.weight(1f), emoji = "🎯", value = "$accuracy%", label = "Precisión")
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatBox(modifier = Modifier.weight(1f), emoji = "📚", value = "$completedModules", label = "Módulos")
                        StatBox(modifier = Modifier.weight(1f), emoji = "✅", value = "$correctAnswers", label = "Correctas")
                        StatBox(modifier = Modifier.weight(1f), emoji = "⌨️", value = "$totalAttempts", label = "Intentos")
                    }
                }
            }
        }

        // ── Examen final ──────────────────────────────────────
        if (examDone) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SuccessGreenBg),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFC0DD97)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🎓", fontSize = 28.sp)
                        Column(Modifier.weight(1f)) {
                            Text("Examen Final completado", fontWeight = FontWeight.Medium,
                                fontSize = 14.sp, color = SuccessGreen)
                            Text("Calificación: $examScore%", fontSize = 13.sp, color = SuccessGreen)
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun StatBox(modifier: Modifier = Modifier, emoji: String, value: String, label: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 18.sp)
            Text(value, fontWeight = FontWeight.Medium, fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}