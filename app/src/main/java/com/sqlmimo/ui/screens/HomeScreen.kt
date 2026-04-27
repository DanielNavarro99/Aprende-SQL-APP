package com.sqlmimo.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sqlmimo.model.Difficulty
import com.sqlmimo.model.Module
import com.sqlmimo.ui.components.XpProgressBar
import com.sqlmimo.ui.theme.Brand
import com.sqlmimo.ui.theme.SuccessGreenBg
import com.sqlmimo.viewmodel.AppViewModel

@Composable
fun HomeScreen(vm: AppViewModel, onModuleClick: (Int) -> Unit) {
    val completedIds by vm.completedIds.collectAsState()
    val xp by vm.xp.collectAsState()
    val level by vm.level.collectAsState()
    val streak by vm.streak.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Aprende SQL", fontSize = 22.sp, fontWeight = FontWeight.Medium)
            Text(
                "Tu camino desde cero hasta experto",
                fontSize = 13.sp, color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Racha
        if (streak > 0) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = BorderStroke(0.5.dp, Color(0xFFFED7AA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("🔥", fontSize = 28.sp)
                    Column(Modifier.weight(1f)) {
                        Text("¡Racha activa!", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color(0xFF9A3412))
                        Text("$streak día${if (streak > 1) "s" else ""} consecutivo${if (streak > 1) "s" else ""}", fontSize = 12.sp, color = Color(0xFFC2410C))
                    }
                    Text("$streak", fontSize = 32.sp, fontWeight = FontWeight.Medium, color = Color(0xFFC2410C))
                }
            }
        }

        // XP Bar
        item {
            XpProgressBar(
                level = level,
                xpProgress = vm.xpProgress(),
                xp = xp,
                xpNeeded = vm.xpForCurrentLevel()
            )
        }

        // Módulos
        itemsIndexed(vm.modules) { index, module ->
            val locked = vm.isModuleLocked(index)
            val progress = vm.moduleProgress(module)
            val totalLessons = module.lessons.size
            val doneLessons = (progress * totalLessons).toInt()
            val complete = doneLessons == totalLessons
            val totalXp = module.lessons.sumOf { it.xp }

            ModuleCard(
                module = module,
                locked = locked,
                complete = complete,
                progress = progress,
                doneLessons = doneLessons,
                totalLessons = totalLessons,
                totalXp = totalXp,
                onClick = { if (!locked) onModuleClick(index) }
            )
        }

        // Tarjeta del examen final
        item {
            val allDone = vm.allModulesCompleted()
            val examDone by vm.examDone.collectAsState()
            val examScore by vm.examScore.collectAsState()
            ExamCard(unlocked = allDone, done = examDone, score = examScore, onClick = {
                if (allDone) onModuleClick(-1)
            })
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun ModuleCard(
    module: Module,
    locked: Boolean,
    complete: Boolean,
    progress: Float,
    doneLessons: Int,
    totalLessons: Int,
    totalXp: Int,
    onClick: () -> Unit
) {
    val diffColor = when (module.difficulty) {
        Difficulty.BEGINNER -> Pair(Color(0xFFEAF3DE), Color(0xFF3B6D11))
        Difficulty.INTERMEDIATE -> Pair(Color(0xFFFFF7ED), Color(0xFF9A3412))
        Difficulty.ADVANCED -> Pair(Color(0xFFFCEBEB), Color(0xFFA32D2D))
    }
    val diffLabel = when (module.difficulty) {
        Difficulty.BEGINNER -> "Principiante"
        Difficulty.INTERMEDIATE -> "Intermedio"
        Difficulty.ADVANCED -> "Avanzado"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!locked) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(containerColor = if (locked) Color(0xFFF9FAFB) else Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (complete) SuccessGreenBg else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (complete) "✅" else module.icon, fontSize = 22.sp)
            }

            Column(Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        module.title,
                        fontWeight = FontWeight.Medium, fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    // Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when {
                                    complete -> SuccessGreenBg
                                    locked -> Color(0xFFF3F4F6)
                                    else -> Color(0xFFEEF2FF)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            when {
                                complete -> "✓"
                                locked -> "🔒"
                                else -> "$doneLessons/$totalLessons"
                            },
                            fontSize = 11.sp,
                            color = when {
                                complete -> Color(0xFF3B6D11)
                                locked -> Color(0xFF9CA3AF)
                                else -> Color(0xFF3730A3)
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(module.description, fontSize = 12.sp, color = Color(0xFF6B7280))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(diffColor.first)
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(diffLabel, fontSize = 10.sp, color = diffColor.second, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(Modifier.height(7.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(4.dp)),
                    color = Brand,
                    trackColor = Color(0xFFE5E7EB)
                )
                Text(
                    "$totalLessons lecciones · ${module.lessons.count { it.type == com.sqlmimo.model.LessonType.EXERCISE }} ejercicios · $totalXp XP",
                    fontSize = 11.sp, color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ExamCard(unlocked: Boolean, done: Boolean, score: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (unlocked) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(containerColor = if (unlocked) Color.White else Color(0xFFF9FAFB)),
        border = BorderStroke(0.5.dp, if (done) Color(0xFF7C3AED) else Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (unlocked) Color(0xFFF5F3FF) else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎓", fontSize = 22.sp)
            }
            Column(Modifier.weight(1f)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Examen Final", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF5F3FF))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            if (done) "✓ $score%" else if (unlocked) "🎓 Listo" else "🔒",
                            fontSize = 11.sp, color = Color(0xFF7C3AED), fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text("Pon a prueba todo lo aprendido", fontSize = 12.sp, color = Color(0xFF6B7280))
                if (done) {
                    Spacer(Modifier.height(7.dp))
                    LinearProgressIndicator(
                        progress = { score / 100f },
                        modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF7C3AED),
                        trackColor = Color(0xFFE5E7EB)
                    )
                }
                Text(
                    "15 preguntas · Calificación automática",
                    fontSize = 11.sp, color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
