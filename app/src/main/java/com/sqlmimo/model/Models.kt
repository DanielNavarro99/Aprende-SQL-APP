package com.sqlmimo.model

// ── Tipos de lección ──────────────────────────────────────────
enum class LessonType { THEORY, EXERCISE }
enum class Difficulty { BEGINNER, INTERMEDIATE, ADVANCED }

// ── Tabla para mostrar en ejercicios ─────────────────────────
data class TableData(
    val name: String,
    val columns: List<String>,
    val rows: List<List<String>>
)

// ── Contenido de lección de teoría ───────────────────────────
data class TheoryContent(
    val text: String,
    val code: String,
    val note: String = ""
)

// ── Contenido de lección de ejercicio ────────────────────────
data class ExerciseContent(
    val task: String,
    val hint: String,
    val tables: List<TableData>,
    val answer: String,          // respuesta esperada (normalizada)
    val checkFn: (String) -> Boolean = { input ->
        normalizeSQL(input).contains(normalizeSQL(answer))
    }
)

fun normalizeSQL(sql: String): String =
    sql.lowercase()
        .replace(Regex("\\s+"), " ")
        .replace(";", "")
        .trim()

// ── Lección ───────────────────────────────────────────────────
data class Lesson(
    val id: String,
    val title: String,
    val type: LessonType,
    val xp: Int,
    val theory: TheoryContent? = null,
    val exercise: ExerciseContent? = null
)

// ── Módulo ────────────────────────────────────────────────────
data class Module(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val lessons: List<Lesson>
)

// ── Pregunta de examen ────────────────────────────────────────
data class ExamQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)
