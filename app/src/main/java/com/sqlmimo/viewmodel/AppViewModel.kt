package com.sqlmimo.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sqlmimo.data.AppDatabase
import com.sqlmimo.data.ProgressEntity
import com.sqlmimo.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


val android.content.Context.dataStore by preferencesDataStore(name = "user_prefs")

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).progressDao()
    private val ds = app.dataStore

    // Keys para DataStore
    private val XP_KEY = intPreferencesKey("xp")
    private val LEVEL_KEY = intPreferencesKey("level")
    private val STREAK_KEY = intPreferencesKey("streak")
    private val LAST_DATE_KEY = stringPreferencesKey("last_date")
    private val EXAM_DONE_KEY = booleanPreferencesKey("exam_done")
    private val EXAM_SCORE_KEY = intPreferencesKey("exam_score")
    private val CORRECT_KEY = intPreferencesKey("correct_answers")
    private val ATTEMPTS_KEY = intPreferencesKey("total_attempts")
    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

    // ── Estado de la UI ───────────────────────────────────────
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        // Persistir en DataStore
        viewModelScope.launch {
            ds.edit { it[DARK_THEME_KEY] = newValue }
        }
    }

    private val _completedIds = MutableStateFlow<Set<String>>(emptySet())
    val completedIds: StateFlow<Set<String>> = _completedIds

    private val _xp = MutableStateFlow(0)
    val xp: StateFlow<Int> = _xp

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak

    private val _examDone = MutableStateFlow(false)
    val examDone: StateFlow<Boolean> = _examDone

    private val _examScore = MutableStateFlow(0)
    val examScore: StateFlow<Int> = _examScore

    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers

    private val _totalAttempts = MutableStateFlow(0)
    val totalAttempts: StateFlow<Int> = _totalAttempts

    val modules = CourseData.modules
    val examQuestions = CourseData.examQuestions

    init {
        loadData()
        checkStreak()
    }

    private fun loadData() {
        viewModelScope.launch {
            dao.getAllFlow().collect { list ->
                _completedIds.value = list.map { it.lessonId }.toSet()
            }
        }
        viewModelScope.launch {
            ds.data.collect { prefs ->
                _xp.value = prefs[XP_KEY] ?: 0
                _level.value = prefs[LEVEL_KEY] ?: 1
                _streak.value = prefs[STREAK_KEY] ?: 0
                _examDone.value = prefs[EXAM_DONE_KEY] ?: false
                _examScore.value = prefs[EXAM_SCORE_KEY] ?: 0
                _correctAnswers.value = prefs[CORRECT_KEY] ?: 0
                _totalAttempts.value = prefs[ATTEMPTS_KEY] ?: 0
                _isDarkTheme.value = prefs[DARK_THEME_KEY] ?: false
            }
        }
    }

    private fun checkStreak() {
        viewModelScope.launch {
            val prefs = ds.data.first()
            val lastDate = prefs[LAST_DATE_KEY] ?: ""
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            if (lastDate == today) return@launch

            val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date(System.currentTimeMillis() - 86400000))

            val newStreak = when (lastDate) {
                yesterday -> (prefs[STREAK_KEY] ?: 0) + 1
                "" -> 1
                else -> 1
            }

            ds.edit { it[STREAK_KEY] = newStreak; it[LAST_DATE_KEY] = today }
        }
    }

    fun completeLesson(lessonId: String, xpGain: Int, isExercise: Boolean) {
        if (_completedIds.value.contains(lessonId)) return
        viewModelScope.launch {
            dao.markComplete(ProgressEntity(lessonId))
            val newXp = _xp.value + xpGain
            var newLevel = _level.value
            while (newXp >= newLevel * 100) newLevel++

            ds.edit { prefs ->
                prefs[XP_KEY] = newXp
                prefs[LEVEL_KEY] = newLevel
                if (isExercise) prefs[CORRECT_KEY] = (_correctAnswers.value + 1)
            }
        }
    }

    fun incrementAttempts() {
        viewModelScope.launch {
            ds.edit { it[ATTEMPTS_KEY] = _totalAttempts.value + 1 }
        }
    }

    fun saveExamResult(score: Int) {
        viewModelScope.launch {
            val xpBonus = if (score >= 70) 100 else 20
            ds.edit { prefs ->
                prefs[EXAM_DONE_KEY] = true
                prefs[EXAM_SCORE_KEY] = score
                prefs[XP_KEY] = _xp.value + xpBonus
            }
        }
    }

    fun isModuleLocked(moduleIndex: Int): Boolean {
        if (moduleIndex == 0) return false
        val prevModule = modules[moduleIndex - 1]
        return prevModule.lessons.any { !_completedIds.value.contains(it.id) }
    }

    fun moduleProgress(module: com.sqlmimo.model.Module): Float {
        val total = module.lessons.size
        val done = module.lessons.count { _completedIds.value.contains(it.id) }
        return if (total == 0) 0f else done.toFloat() / total
    }

    fun xpForCurrentLevel(): Int = _level.value * 100
    fun xpInCurrentLevel(): Int = _xp.value - (_level.value - 1) * 100
    fun xpProgress(): Float = xpInCurrentLevel().toFloat() / xpForCurrentLevel()

    fun randomMotivation(): String = CourseData.motivationalPhrases.random()

    fun allModulesCompleted(): Boolean =
        modules.all { m -> m.lessons.all { _completedIds.value.contains(it.id) } }
}