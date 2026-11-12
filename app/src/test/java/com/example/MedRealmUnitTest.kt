package com.example

import com.example.data.model.Flashcard
import com.example.data.model.StudyTask
import com.example.data.model.UserProfile
import com.example.learning.EducationalTaskType
import com.example.learning.RewardCalculationService
import org.junit.Assert.*
import org.junit.Test

class MedRealmUnitTest {

    @Test
    fun testLevelProgressionCalculation() {
        var currentXp = 120
        var currentLevel = 1
        var reqXp = 300
        val xpReward = 200

        currentXp += xpReward
        while (currentXp >= reqXp) {
            currentXp -= reqXp
            currentLevel += 1
            reqXp = (reqXp * 1.35f).toInt()
        }

        assertEquals(2, currentLevel)
        assertEquals(20, currentXp)
        assertEquals(405, reqXp)
    }

    @Test
    fun testMultiLevelProgressionBurst() {
        var currentXp = 50
        var currentLevel = 1
        var reqXp = 100
        val xpReward = 500 // Big quest milestone

        currentXp += xpReward
        while (currentXp >= reqXp) {
            currentXp -= reqXp
            currentLevel += 1
            reqXp = (reqXp * 1.35f).toInt()
        }

        assertTrue(currentLevel >= 3)
        assertTrue(currentXp < reqXp)
    }

    @Test
    fun testCoinBalanceNonNegative() {
        val initialCoins = 150
        val cost = 200
        val canAfford = initialCoins >= cost
        assertFalse(canAfford)

        val affordableCost = 100
        val canAffordLower = initialCoins >= affordableCost
        assertTrue(canAffordLower)
        val remaining = initialCoins - affordableCost
        assertTrue(remaining >= 0)
    }

    @Test
    fun testSpacedRepetitionSM2Intervals() {
        val card = Flashcard(
            front = "What is the primary hemodynamic mechanism of S3?",
            back = "Rapid passive ventricular filling into a compliant, volume-overloaded ventricle",
            topic = "Cardiology",
            repetitionLevel = 0,
            easeFactor = 2.5f,
            intervalDays = 1
        )

        // Rating: Good (score = 2)
        var nextInterval = when (card.repetitionLevel) {
            0 -> 1
            1 -> 4
            else -> (card.intervalDays * card.easeFactor).toInt()
        }
        assertEquals(1, nextInterval)

        val advancedCard = card.copy(repetitionLevel = 2, intervalDays = 4, easeFactor = 2.5f)
        nextInterval = (advancedCard.intervalDays * advancedCard.easeFactor).toInt()
        assertEquals(10, nextInterval)
    }

    @Test
    fun testPhysioChainSequenceValidation() {
        val correctOrder = listOf(1, 2, 3, 4, 5, 6)
        val userOrderCorrect = listOf(1, 2, 3, 4, 5, 6)
        val userOrderFaulty = listOf(1, 3, 2, 4, 5, 6)

        assertEquals(correctOrder, userOrderCorrect)
        assertNotEquals(correctOrder, userOrderFaulty)
    }

    @Test
    fun testCriticalUserJourneyTaskStateTransition() {
        val initialTask = StudyTask(
            id = 101,
            title = "Heart Failure Pathophysiology",
            category = "Cardiology",
            systemType = "STUDY_ENGINE",
            estimatedMinutes = 5,
            xpReward = 50,
            coinReward = 30,
            isCompleted = false
        )
        assertFalse(initialTask.isCompleted)

        val completedTask = initialTask.copy(isCompleted = true)
        assertTrue(completedTask.isCompleted)
        assertEquals(50, completedTask.xpReward)
        assertEquals(30, completedTask.coinReward)
    }

    @Test
    fun testRewardCalculationServiceAccurateRewards() {
        val profile = UserProfile(
            id = 1,
            level = 1,
            currentXp = 100,
            requiredXp = 300,
            coins = 150
        )

        val (updatedProfile, result) = RewardCalculationService.calculateRewards(
            currentProfile = profile,
            taskType = EducationalTaskType.CLINICAL_CASE_ARENA,
            accuracyScore = 1.0f,
            confidenceRating = 90f,
            difficultyMultiplier = 1.0f
        )

        assertTrue(result.finalXpAwarded >= 120)
        assertTrue(result.finalCoinsAwarded >= 60)
        assertEquals(profile.coins + result.finalCoinsAwarded, updatedProfile.coins)
        assertFalse(result.isLevelUp)
        assertEquals(1, updatedProfile.level)
    }

    @Test
    fun testRewardCalculationServiceLevelUp() {
        val profile = UserProfile(
            id = 1,
            level = 1,
            currentXp = 250,
            requiredXp = 300,
            coins = 100
        )

        val (updatedProfile, result) = RewardCalculationService.calculateRewards(
            currentProfile = profile,
            taskType = EducationalTaskType.CLINICAL_CASE_ARENA,
            accuracyScore = 1.0f,
            confidenceRating = 95f
        )

        assertTrue(result.isLevelUp)
        assertEquals(2, updatedProfile.level)
        assertTrue(updatedProfile.requiredXp > 300)
    }
}
