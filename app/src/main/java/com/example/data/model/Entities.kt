package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val characterName: String = "Apprentice Healer",
    val academicYear: String = "Year 3 (Clinical)",
    val mainSubject: String = "Cardiology & Internal Medicine",
    val learningGoals: String = "Master clinical reasoning & ace OSCE rounds",
    val preferredLanguage: String = "English",
    val dailyTargetMinutes: Int = 20,
    val upcomingExam: String = "Internal Medicine OSCE & Written Boards",
    val level: Int = 1,
    val currentXp: Int = 120,
    val requiredXp: Int = 300,
    val coins: Int = 150,
    val selectedCompanionId: String = "orion", // orion, ember, nova, atlas
    // Avatar customization
    val hairStyle: String = "Academic Short", // Academic Short, Wizard Bob, Pixie Mage, Knight Waves
    val hairColor: Long = 0xFF4A3728, // brown, black, blonde, silver, teal
    val skinTone: Long = 0xFFF5D0A9,
    val outfit: String = "Apprentice White Coat", // Apprentice White Coat, Royal Violet Robe, Field Medic Vest, Gold Scholar Tunic
    val accessory: String = "Classic Stethoscope", // Classic Stethoscope, Sage Glasses, Dragon Brooch, Research Magnifier
    val headwear: String = "None", // None, Scholar Mortarboard, Laurels, Mystic Hood
    val isOnboarded: Boolean = false
)

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Cardiology, Pulmonology, Pharmacology, OSCE, Research
    val systemType: String, // STUDY_ENGINE, KNOWLEDGE_LAB, CLINICAL_ARENA, SPEAKING_STUDIO, RESEARCH_ACADEMY
    val estimatedMinutes: Int,
    val xpReward: Int,
    val coinReward: Int,
    val isCompleted: Boolean = false,
    val dueDate: String = "Today"
)

@Entity(tableName = "lecture_documents")
data class LectureDocument(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val pageCount: Int,
    val extractedSummary: String,
    val dateAdded: Long = System.currentTimeMillis()
)

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val front: String,
    val back: String,
    val topic: String,
    val repetitionLevel: Int = 0, // 0 to 5
    val easeFactor: Float = 2.5f,
    val intervalDays: Int = 1,
    val nextReviewEpoch: Long = System.currentTimeMillis(),
    val reviewCount: Int = 0
)

@Entity(tableName = "question_bank")
data class MedicalQuestion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionText: String,
    val topic: String,
    val optionsJson: String, // Delimited options
    val correctIndex: Int,
    val explanation: String,
    val clinicalPearls: String,
    val difficulty: String = "Intermediate"
)

@Entity(tableName = "clinical_cases")
data class ClinicalCase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val patientName: String,
    val patientAgeGender: String,
    val chiefComplaint: String,
    val historyText: String,
    val examFindings: String,
    val labData: String,
    val primaryDiagnosis: String,
    val differentialDiagnosesJson: String,
    val managementSummary: String,
    val isResolved: Boolean = false
)

@Entity(tableName = "user_inventory")
data class CosmeticItem(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // OUTFIT, ACCESSORY, HEADWEAR, FURNITURE
    val cost: Int,
    val isPurchased: Boolean = false,
    val isEquipped: Boolean = false,
    val description: String,
    val iconEmoji: String
)
