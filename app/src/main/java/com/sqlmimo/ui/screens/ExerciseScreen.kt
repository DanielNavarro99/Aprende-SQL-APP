package com.sqlmimo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sqlmimo.model.*
import com.sqlmimo.ui.components.*
import com.sqlmimo.ui.theme.Brand
import com.sqlmimo.viewmodel.AppViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.foundation.background


// ── Pantalla de módulo (lista de lecciones) ───────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreen(
    vm: AppViewModel,
    moduleIndex: Int,
    onLessonClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val module = vm.modules[moduleIndex]
    val completedIds by vm.completedIds.collectAsState()
    val isDark by vm.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(module.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(module.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = { // <--- EL BOTÓN ESTÁ AQUÍ
                    IconButton(onClick = { vm.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(module.lessons.size) { i ->
                val lesson = module.lessons[i]
                val done = completedIds.contains(lesson.id)
                val unlocked = i == 0 || completedIds.contains(module.lessons[i - 1].id) || done

                Card(
                    modifier = Modifier.fillMaxWidth().clickable(enabled = unlocked) { onLessonClick(i) },
                    // CAMBIO VITAL: Usamos los colores del tema, no colores fijos
                    colors = CardDefaults.cardColors(
                        containerColor = if (unlocked) MaterialTheme.colorScheme.surface
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        0.5.dp,
                        if (done) Color(0xFFC0DD97) else MaterialTheme.colorScheme.outlineVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(50),
                            color = if (done) Color(0xFFEAF3DE) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    if (done) "✓" else "${i + 1}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (done) Color(0xFF3B6D11) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Column(Modifier.weight(1f)) {
                            Text(lesson.title, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                "${if (lesson.type == LessonType.THEORY) "📖 Teoría" else "⌨️ Ejercicio"} · ${lesson.xp} XP",
                                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Pantalla de ejercicio / teoría ────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    vm: AppViewModel,
    moduleIndex: Int,
    lessonIndex: Int,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val module = vm.modules[moduleIndex]
    val lesson = module.lessons[lessonIndex]
    val completedIds by vm.completedIds.collectAsState()
    val alreadyDone = completedIds.contains(lesson.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Column {
                    Text(lesson.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(module.title, fontSize = 12.sp, color = Color(0xFF6B7280))
                }},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when (lesson.type) {
            LessonType.THEORY -> TheoryContent(
                lesson = lesson,
                alreadyDone = alreadyDone,
                onComplete = {
                    vm.completeLesson(lesson.id, lesson.xp, false)
                    onNext()
                },
                modifier = Modifier.padding(padding)
            )
            LessonType.EXERCISE -> ExerciseContent(
                lesson = lesson,
                module = module,
                lessonIndex = lessonIndex,
                alreadyDone = alreadyDone,
                vm = vm,
                onNext = onNext,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun TheoryContent(
    lesson: Lesson,
    alreadyDone: Boolean,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = lesson.theory ?: return
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(lesson.title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Spacer(Modifier.height(10.dp))
                    Text(t.text, fontSize = 14.sp, color = Color(0xFF374151), lineHeight = 22.sp)
                    Spacer(Modifier.height(12.dp))
                    CodeBlock(t.code)
                    if (t.note.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            color = Color(0xFFEEF2FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Text("💡 ", fontSize = 13.sp)
                                Text(t.note, fontSize = 13.sp, color = Color(0xFF3730A3), lineHeight = 20.sp)
                            }
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Brand),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entendido — continuar →", fontWeight = FontWeight.Medium)
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun ExerciseContent(
    lesson: Lesson,
    module: com.sqlmimo.model.Module,
    lessonIndex: Int,
    alreadyDone: Boolean,
    vm: AppViewModel,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ex = lesson.exercise ?: return
    var sqlInput by remember { mutableStateOf("") }
    var showHint by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var motivation by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tarea
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "EJERCICIO · ${lesson.xp} XP",
                        fontSize = 11.sp, fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 7.dp)
                    )
                    Text(ex.task, fontSize = 15.sp, lineHeight = 24.sp)
                    if (showHint) {
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            color = Color(0xFFF5F3FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "💡 ${ex.hint}",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp, color = Color(0xFF5B21B6), lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Tablas
        ex.tables.forEach { table ->
            item { DataTable(table) }
        }

        // Editor
        item {
            SqlEditor(
                value = sqlInput,
                onValueChange = { sqlInput = it },
                onRun = {
                    if (sqlInput.isBlank()) return@SqlEditor
                    vm.incrementAttempts()
                    val correct = ex.checkFn(sqlInput)
                    if (correct) {
                        motivation = vm.randomMotivation()
                        vm.completeLesson(lesson.id, lesson.xp, true)
                        feedback = Pair(true, "¡Ganaste +${lesson.xp} XP!\n\nTu consulta:\n${sqlInput.trim()}")
                    } else {
                        feedback = Pair(false, analyzeError(sqlInput, ex))
                    }
                },
                onHint = { showHint = !showHint }
            )
        }

        // Feedback
        feedback?.let { (isOk, msg) ->
            item {
                FeedbackCard(
                    isCorrect = isOk,
                    title = if (isOk) "¡Correcto! 🎉" else "Revisa tu consulta ❌",
                    body = msg,
                    motivation = if (isOk) motivation else "",
                    onNext = if (isOk) onNext else null
                )
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

fun analyzeError(input: String, ex: ExerciseContent): String {
    val u = input.lowercase().replace(Regex("\\s+"), " ").replace(";", "").trim()
    val sb = StringBuilder("Errores encontrados:\n")
    if (!u.contains("select") && !u.contains("insert") && !u.contains("update") && !u.contains("delete") && !u.contains("create")) {
        sb.append("• Falta la palabra clave SELECT (o INSERT/UPDATE/DELETE).\n")
    } else {
        val expected = ex.answer
        if (expected.contains("from") && !u.contains("from")) sb.append("• Falta FROM.\n")
        if (expected.contains("where") && !u.contains("where")) sb.append("• Falta la cláusula WHERE.\n")
        if (expected.contains("order by") && !u.contains("order by")) sb.append("• Falta ORDER BY.\n")
        if (expected.contains("limit") && !u.contains("limit")) sb.append("• Falta LIMIT.\n")
        if (expected.contains("group by") && !u.contains("group by")) sb.append("• Falta GROUP BY.\n")
        if (expected.contains("having") && !u.contains("having")) sb.append("• Falta HAVING.\n")
        if (expected.contains("join") && !u.contains("join")) sb.append("• Falta el JOIN.\n")
        if (expected.contains("distinct") && !u.contains("distinct")) sb.append("• Falta DISTINCT.\n")
        if (expected.contains("between") && !u.contains("between")) sb.append("• Falta BETWEEN.\n")
        if (expected.contains("desc") && !u.contains("desc")) sb.append("• Falta DESC.\n")
        if (sb.toString() == "Errores encontrados:\n") sb.append("• Verifica los nombres de columnas y tablas.\n")
    }
    sb.append("\n💡 Pista: ${ex.hint}")
    return sb.toString()
}
