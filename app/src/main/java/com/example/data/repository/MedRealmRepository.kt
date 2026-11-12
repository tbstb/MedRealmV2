package com.example.data.repository

import com.example.data.dao.MedRealmDao
import com.example.data.model.*
import com.example.learning.EducationalTaskType
import com.example.learning.RewardCalculationResult
import com.example.learning.RewardCalculationService
import kotlinx.coroutines.flow.Flow

class MedRealmRepository(private val dao: MedRealmDao) {
    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allTasks: Flow<List<StudyTask>> = dao.getAllTasks()
    val allFlashcards: Flow<List<Flashcard>> = dao.getAllFlashcards()
    val allQuestions: Flow<List<MedicalQuestion>> = dao.getAllQuestions()
    val allCases: Flow<List<ClinicalCase>> = dao.getAllCases()
    val allDocuments: Flow<List<LectureDocument>> = dao.getAllDocuments()
    val inventory: Flow<List<CosmeticItem>> = dao.getAllInventory()

    suspend fun getUserProfileOnce(): UserProfile? = dao.getUserProfileOnce()

    suspend fun saveUserProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    /**
     * Award educational rewards using the robust RewardCalculationService.
     * Persists changes to Room database immediately, emitting new profile state to Flow listeners.
     */
    suspend fun awardEducationalRewards(
        taskType: EducationalTaskType,
        accuracyScore: Float = 1.0f,
        confidenceRating: Float = 80f,
        difficultyMultiplier: Float = 1.0f
    ): RewardCalculationResult {
        val currentProfile = dao.getUserProfileOnce() ?: UserProfile()
        val (updatedProfile, result) = RewardCalculationService.calculateRewards(
            currentProfile = currentProfile,
            taskType = taskType,
            accuracyScore = accuracyScore,
            confidenceRating = confidenceRating,
            difficultyMultiplier = difficultyMultiplier
        )
        dao.insertOrUpdateProfile(updatedProfile)
        return result
    }

    suspend fun awardRewards(xpGained: Int, coinsGained: Int) {
        val current = dao.getUserProfileOnce() ?: UserProfile()
        var newXp = current.currentXp + xpGained
        var newLevel = current.level
        var reqXp = current.requiredXp

        while (newXp >= reqXp) {
            newXp -= reqXp
            newLevel += 1
            reqXp = (reqXp * 1.35f).toInt()
        }

        val updated = current.copy(
            level = newLevel,
            currentXp = newXp,
            requiredXp = reqXp,
            coins = current.coins + coinsGained
        )
        dao.insertOrUpdateProfile(updated)
    }

    suspend fun spendCoins(amount: Int): Boolean {
        val current = dao.getUserProfileOnce() ?: return false
        if (current.coins < amount) return false
        val updated = current.copy(coins = current.coins - amount)
        dao.insertOrUpdateProfile(updated)
        return true
    }

    suspend fun completeTask(task: StudyTask) {
        if (!task.isCompleted) {
            dao.updateTask(task.copy(isCompleted = true))
            val taskType = when (task.systemType) {
                "STUDY_ENGINE" -> EducationalTaskType.FLASHCARD_RETRIEVAL
                "KNOWLEDGE_LAB" -> EducationalTaskType.PHYSIOCHAIN_PUZZLE
                "CLINICAL_ARENA" -> EducationalTaskType.CLINICAL_CASE_ARENA
                "SPEAKING_STUDIO" -> EducationalTaskType.SPEAKING_OSCE_ROUND
                "RESEARCH_ACADEMY" -> EducationalTaskType.RESEARCH_TRIAL_APPRAISAL
                else -> EducationalTaskType.MCQ_SESSION
            }
            awardEducationalRewards(taskType = taskType, accuracyScore = 1.0f, confidenceRating = 85f)
        }
    }

    suspend fun addStudyTask(task: StudyTask) {
        dao.insertTask(task)
    }

    // Spaced Repetition (SuperMemo SM-2 modified)
    suspend fun reviewFlashcard(flashcard: Flashcard, qualityScore: Int) {
        // qualityScore: 0: Again, 1: Hard, 2: Good, 3: Easy
        var nextInterval: Int
        var newEase = flashcard.easeFactor
        var newRep = flashcard.repetitionLevel

        if (qualityScore < 1) { // Again
            nextInterval = 1
            newRep = 0
            newEase = maxOf(1.3f, newEase - 0.2f)
        } else {
            when (newRep) {
                0 -> nextInterval = 1
                1 -> nextInterval = if (qualityScore == 1) 2 else 4
                else -> {
                    val multiplier = when (qualityScore) {
                        1 -> 1.2f
                        2 -> newEase
                        else -> newEase * 1.3f
                    }
                    nextInterval = (flashcard.intervalDays * multiplier).toInt().coerceAtLeast(1)
                }
            }
            newRep += 1
            val delta = 0.1f - (3 - qualityScore) * (0.08f + (3 - qualityScore) * 0.02f)
            newEase = (newEase + delta).coerceIn(1.3f, 3.0f)
        }

        val nextEpoch = System.currentTimeMillis() + (nextInterval * 86400000L)
        val updated = flashcard.copy(
            repetitionLevel = newRep,
            easeFactor = newEase,
            intervalDays = nextInterval,
            nextReviewEpoch = nextEpoch,
            reviewCount = flashcard.reviewCount + 1
        )
        dao.updateFlashcard(updated)
        val accuracy = when (qualityScore) {
            0 -> 0.3f
            1 -> 0.6f
            2 -> 0.85f
            else -> 1.0f
        }
        awardEducationalRewards(
            taskType = EducationalTaskType.FLASHCARD_RETRIEVAL,
            accuracyScore = accuracy,
            confidenceRating = (accuracy * 100f)
        )
    }

    suspend fun addFlashcard(card: Flashcard) {
        dao.insertFlashcard(card)
    }

    suspend fun addDocument(doc: LectureDocument) {
        dao.insertDocument(doc)
    }

    suspend fun resolveCase(clinicalCase: ClinicalCase) {
        if (!clinicalCase.isResolved) {
            dao.updateCase(clinicalCase.copy(isResolved = true))
            awardEducationalRewards(
                taskType = EducationalTaskType.CLINICAL_CASE_ARENA,
                accuracyScore = 1.0f,
                confidenceRating = 90f,
                difficultyMultiplier = 1.2f
            )
        }
    }

    suspend fun purchaseCosmetic(item: CosmeticItem): Boolean {
        if (spendCoins(item.cost)) {
            dao.updateCosmeticItem(item.copy(isPurchased = true))
            return true
        }
        return false
    }

    suspend fun equipCosmetic(item: CosmeticItem) {
        dao.updateCosmeticItem(item.copy(isEquipped = true))
        val profile = dao.getUserProfileOnce() ?: return
        val updatedProfile = when (item.type) {
            "OUTFIT" -> profile.copy(outfit = item.name)
            "ACCESSORY" -> profile.copy(accessory = item.name)
            "HEADWEAR" -> profile.copy(headwear = item.name)
            else -> profile
        }
        dao.insertOrUpdateProfile(updatedProfile)
    }
}
