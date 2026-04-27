package com.sqlmimo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── Entity ────────────────────────────────────────────────────
@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey val lessonId: String,
    val completedAt: Long = System.currentTimeMillis()
)

// ── DAO ───────────────────────────────────────────────────────
@Dao
@JvmSuppressWildcards
interface ProgressDao {
    @Query("SELECT * FROM progress")
    fun getAllFlow(): Flow<List<ProgressEntity>>

    @Query("SELECT lessonId FROM progress")
    suspend fun getCompletedIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markComplete(progress: ProgressEntity): Long

    @Query("DELETE FROM progress")
    suspend fun clearAll() : Int

    @Query("SELECT COUNT(*) FROM progress")
    suspend fun totalCompleted(): Int
}

// ── Database ──────────────────────────────────────────────────
@Database(entities = [ProgressEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sqlmimo_db"
                ).build().also { INSTANCE = it }
            }
    }
}
