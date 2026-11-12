package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedRealmDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // Study Tasks
    @Query("SELECT * FROM study_tasks ORDER BY isCompleted ASC, id ASC")
    fun getAllTasks(): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask)

    @Update
    suspend fun updateTask(task: StudyTask)

    // Flashcards
    @Query("SELECT * FROM flashcards ORDER BY nextReviewEpoch ASC")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE nextReviewEpoch <= :nowEpoch")
    fun getDueFlashcards(nowEpoch: Long): Flow<List<Flashcard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    // Medical Questions
    @Query("SELECT * FROM question_bank")
    fun getAllQuestions(): Flow<List<MedicalQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: MedicalQuestion)

    // Clinical Cases
    @Query("SELECT * FROM clinical_cases")
    fun getAllCases(): Flow<List<ClinicalCase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(clinicalCase: ClinicalCase)

    @Update
    suspend fun updateCase(clinicalCase: ClinicalCase)

    // Lecture Documents
    @Query("SELECT * FROM lecture_documents ORDER BY dateAdded DESC")
    fun getAllDocuments(): Flow<List<LectureDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: LectureDocument)

    // Inventory
    @Query("SELECT * FROM user_inventory")
    fun getAllInventory(): Flow<List<CosmeticItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCosmeticItem(item: CosmeticItem)

    @Update
    suspend fun updateCosmeticItem(item: CosmeticItem)
}
