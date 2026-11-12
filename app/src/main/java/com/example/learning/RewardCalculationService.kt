package com.example.learning

import com.example.data.model.UserProfile
import kotlin.math.roundToInt

/**
 * Educational task category to apply domain-specific multipliers and motivation bonuses.
 */
enum class EducationalTaskType {
    FLASHCARD_RETRIEVAL,    // Spaced repetition review (Again, Hard, Good, Easy)
    MCQ_SESSION,            // Multiple choice vignette with confidence calibration
    PHYSIOCHAIN_PUZZLE,     // Pathophysiological mechanism causal cascade
    CLINICAL_CASE_ARENA,    // Complex multi-tab patient diagnostic synthesis
    SPEAKING_OSCE_ROUND,    // Structured SBAR ward presentation with examiner rubric
    RESEARCH_TRIAL_APPRAISAL // PICO question & EBM statistical appraisal
}

/**
 * Detailed breakdown of calculated rewards.
 */
data class RewardCalculationResult(
    val baseCandidateXp: Int,
    val baseCandidateCoins: Int,
    val performanceBonusXp: Int,
    val performanceBonusCoins: Int,
    val finalXpAwarded: Int,
    val finalCoinsAwarded: Int,
    val isLevelUp: Boolean,
    val previousLevel: Int,
    val newLevel: Int,
    val previousXp: Int,
    val newCurrentXp: Int,
    val newRequiredXp: Int,
    val previousCoins: Int,
    val newCoins: Int,
    val celebrationMessage: String
)

/**
 * Robust Reward Calculation Service that calculates XP and Coins based on
 * task complexity, accuracy, confidence calibration, and academic level scaling.
 */
object RewardCalculationService {

    /**
     * Compute rewards for a completed educational activity and return updated user profile.
     */
    fun calculateRewards(
        currentProfile: UserProfile,
        taskType: EducationalTaskType,
        accuracyScore: Float = 1.0f,     // 0.0f to 1.0f (e.g. 3/3 = 1.0f, SM-2 Good = 0.8f)
        confidenceRating: Float = 80f,   // 0 to 100%
        difficultyMultiplier: Float = 1.0f
    ): Pair<UserProfile, RewardCalculationResult> {
        // 1. Establish base values by educational task category
        val (baseXp, baseCoins) = when (taskType) {
            EducationalTaskType.FLASHCARD_RETRIEVAL -> Pair(20, 10)
            EducationalTaskType.MCQ_SESSION -> Pair(50, 30)
            EducationalTaskType.PHYSIOCHAIN_PUZZLE -> Pair(65, 35)
            EducationalTaskType.CLINICAL_CASE_ARENA -> Pair(120, 60)
            EducationalTaskType.SPEAKING_OSCE_ROUND -> Pair(80, 45)
            EducationalTaskType.RESEARCH_TRIAL_APPRAISAL -> Pair(75, 40)
        }

        // 2. Metacognitive Calibration Bonus:
        // Reward students whose confidence matches performance (avoids overconfidence trap)
        val calibrationBonusMultiplier: Float = if (accuracyScore >= 0.8f && confidenceRating >= 70f) {
            1.20f // Well-calibrated high confidence & high accuracy
        } else if (accuracyScore < 0.5f && confidenceRating < 50f) {
            1.15f // Metacognitive awareness of uncertainty (rewarded in medical education)
        } else if (accuracyScore < 0.5f && confidenceRating >= 85f) {
            0.85f // Overconfidence bias penalty on bonus
        } else {
            1.00f
        }

        val accuracyMultiplier = accuracyScore.coerceIn(0.2f, 1.25f)
        val totalMultiplier = (accuracyMultiplier * calibrationBonusMultiplier * difficultyMultiplier).coerceAtLeast(0.25f)

        val totalXpAwarded = (baseXp * totalMultiplier).roundToInt().coerceAtLeast(5)
        val totalCoinsAwarded = (baseCoins * totalMultiplier).roundToInt().coerceAtLeast(2)

        val performanceBonusXp = (totalXpAwarded - baseXp).coerceAtLeast(0)
        val performanceBonusCoins = (totalCoinsAwarded - baseCoins).coerceAtLeast(0)

        // 3. Level Progression arithmetic: required XP scales at 1.35x compounding
        var runningXp = currentProfile.currentXp + totalXpAwarded
        var runningLevel = currentProfile.level
        var runningReqXp = currentProfile.requiredXp.coerceAtLeast(100)
        var didLevelUp = false

        while (runningXp >= runningReqXp) {
            runningXp -= runningReqXp
            runningLevel += 1
            runningReqXp = (runningReqXp * 1.35f).roundToInt()
            didLevelUp = true
        }

        val newCoins = currentProfile.coins + totalCoinsAwarded

        val celebrationMsg = when {
            didLevelUp -> "🌟 LEVEL UP! You reached Scholar Level $runningLevel! Academy privileges expanded."
            accuracyScore >= 0.95f -> "✨ Flawless Mastery! Exceptional clinical reasoning demonstrated."
            else -> "🎯 Cognitive progress saved! XP and study coins awarded."
        }

        val updatedProfile = currentProfile.copy(
            level = runningLevel,
            currentXp = runningXp,
            requiredXp = runningReqXp,
            coins = newCoins
        )

        val result = RewardCalculationResult(
            baseCandidateXp = baseXp,
            baseCandidateCoins = baseCoins,
            performanceBonusXp = performanceBonusXp,
            performanceBonusCoins = performanceBonusCoins,
            finalXpAwarded = totalXpAwarded,
            finalCoinsAwarded = totalCoinsAwarded,
            isLevelUp = didLevelUp,
            previousLevel = currentProfile.level,
            newLevel = runningLevel,
            previousXp = currentProfile.currentXp,
            newCurrentXp = runningXp,
            newRequiredXp = runningReqXp,
            previousCoins = currentProfile.coins,
            newCoins = newCoins,
            celebrationMessage = celebrationMsg
        )

        return Pair(updatedProfile, result)
    }
}
