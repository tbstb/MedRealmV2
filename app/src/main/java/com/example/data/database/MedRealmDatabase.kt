package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.MedRealmDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        StudyTask::class,
        LectureDocument::class,
        Flashcard::class,
        MedicalQuestion::class,
        ClinicalCase::class,
        CosmeticItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MedRealmDatabase : RoomDatabase() {
    abstract fun dao(): MedRealmDao

    companion object {
        @Volatile
        private var INSTANCE: MedRealmDatabase? = null

        fun getDatabase(context: Context): MedRealmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedRealmDatabase::class.java,
                    "medrealm_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.dao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: MedRealmDao) {
                // Initial Profile
                dao.insertOrUpdateProfile(
                    UserProfile(
                        id = 1,
                        characterName = "Dr. Valerius",
                        academicYear = "Clinical Year 3",
                        mainSubject = "Cardiology & Internal Medicine",
                        learningGoals = "Ace OSCE ward presentations & master physiology mechanisms",
                        preferredLanguage = "English",
                        dailyTargetMinutes = 20,
                        upcomingExam = "Internal Medicine OSCE Boards",
                        level = 1,
                        currentXp = 120,
                        requiredXp = 300,
                        coins = 240,
                        selectedCompanionId = "orion",
                        hairStyle = "Academic Short",
                        hairColor = 0xFF4A3728,
                        skinTone = 0xFFF5D0A9,
                        outfit = "Apprentice White Coat",
                        accessory = "Classic Stethoscope",
                        headwear = "None",
                        isOnboarded = true
                    )
                )

                // Initial Tasks
                dao.insertTask(
                    StudyTask(
                        title = "Heart Failure Pathophysiology & Signs",
                        category = "Cardiology",
                        systemType = "STUDY_ENGINE",
                        estimatedMinutes = 5,
                        xpReward = 50,
                        coinReward = 30,
                        isCompleted = false,
                        dueDate = "Today"
                    )
                )
                dao.insertTask(
                    StudyTask(
                        title = "Cardiac Physiology Chain: Afterload & Preload",
                        category = "Physiology",
                        systemType = "KNOWLEDGE_LAB",
                        estimatedMinutes = 8,
                        xpReward = 60,
                        coinReward = 35,
                        isCompleted = false,
                        dueDate = "Today"
                    )
                )
                dao.insertTask(
                    StudyTask(
                        title = "Clinical Case: Mr. Sterling's Acute Dyspnea",
                        category = "Internal Medicine",
                        systemType = "CLINICAL_ARENA",
                        estimatedMinutes = 10,
                        xpReward = 80,
                        coinReward = 50,
                        isCompleted = false,
                        dueDate = "Today"
                    )
                )
                dao.insertTask(
                    StudyTask(
                        title = "Ward Presentation: 60-Second SBAR for Heart Failure",
                        category = "OSCE & Ward",
                        systemType = "SPEAKING_STUDIO",
                        estimatedMinutes = 6,
                        xpReward = 70,
                        coinReward = 40,
                        isCompleted = false,
                        dueDate = "Tomorrow"
                    )
                )
                dao.insertTask(
                    StudyTask(
                        title = "Appraise SGLT2i in Heart Failure (EMPEROR-Reduced)",
                        category = "Research",
                        systemType = "RESEARCH_ACADEMY",
                        estimatedMinutes = 10,
                        xpReward = 75,
                        coinReward = 45,
                        isCompleted = false,
                        dueDate = "Upcoming"
                    )
                )

                // Initial Flashcards
                dao.insertFlashcard(
                    Flashcard(
                        front = "What is the primary hemodynamic mechanism of S3 heart sound?",
                        back = "Rapid ventricular filling into a compliant, volume-overloaded ventricle during early diastole. Commonly heard in dilated cardiomyopathy and systolic heart failure.",
                        topic = "Cardiology",
                        repetitionLevel = 1,
                        easeFactor = 2.5f,
                        intervalDays = 1,
                        nextReviewEpoch = System.currentTimeMillis() - 1000
                    )
                )
                dao.insertFlashcard(
                    Flashcard(
                        front = "Why are Beta-Blockers introduced gradually in compensated HFrEF?",
                        back = "To avoid transient negative inotropy/decompensation, while allowing long-term reverse ventricular remodeling and downregulation of neurohormonal toxicity.",
                        topic = "Pharmacology",
                        repetitionLevel = 2,
                        easeFactor = 2.6f,
                        intervalDays = 3,
                        nextReviewEpoch = System.currentTimeMillis() - 500
                    )
                )
                dao.insertFlashcard(
                    Flashcard(
                        front = "Identify Beck's Triad for Cardiac Tamponade.",
                        back = "1. Hypotension with narrowed pulse pressure\n2. Jugular venous distension (JVD)\n3. Muffled / distant heart sounds.",
                        topic = "Emergency Medicine",
                        repetitionLevel = 1,
                        easeFactor = 2.4f,
                        intervalDays = 2,
                        nextReviewEpoch = System.currentTimeMillis() + 86400000L
                    )
                )
                dao.insertFlashcard(
                    Flashcard(
                        front = "What physiological event corresponds to the 'a' wave in JVP?",
                        back = "Right atrial contraction against an open tricuspid valve during end-diastole. Absent in atrial fibrillation, accentuated (cannon) in AV dissociation.",
                        topic = "Clinical Skills",
                        repetitionLevel = 0,
                        easeFactor = 2.5f,
                        intervalDays = 1,
                        nextReviewEpoch = System.currentTimeMillis() - 2000
                    )
                )

                // Medical Questions (MCQ)
                dao.insertQuestion(
                    MedicalQuestion(
                        questionText = "A 64-year-old male presents with worsening dyspnea on exertion, bilateral ankle edema, and orthopnea. Echocardiogram reveals an ejection fraction of 32%. Which four-pillar medical therapy class has been shown to reduce mortality and also promote glycosuria?",
                        topic = "Cardiology",
                        optionsJson = "Loop Diuretics (e.g. Furosemide)|||SGLT2 Inhibitors (e.g. Empagliflozin)|||Calcium Channel Blockers (e.g. Amlodipine)|||Digoxin",
                        correctIndex = 1,
                        explanation = "SGLT2 inhibitors (Empagliflozin, Dapagliflozin) are established as one of the fundamental four pillars of guideline-directed medical therapy (GDMT) for HFrEF, demonstrating proven mortality reduction and renal protection independent of diabetes status.",
                        clinicalPearls = "The 4 GDMT pillars for HFrEF: ARNI/ACEi/ARB, Beta-Blocker (Carvedilol, Bisoprolol, Metoprolol Succinate), MRA (Spironolactone, Eplerenone), and SGLT2i.",
                        difficulty = "High Yield"
                    )
                )
                dao.insertQuestion(
                    MedicalQuestion(
                        questionText = "During auscultation of a 22-year-old athlete, a systolic murmur is heard along the left sternal border that intensifies with the Valsalva maneuver. What is the most likely underlying condition?",
                        topic = "Cardiology",
                        optionsJson = "Aortic Stenosis|||Hypertrophic Cardiomyopathy (HCM)|||Mitral Regurgitation|||Ventricular Septal Defect",
                        correctIndex = 1,
                        explanation = "The murmur of HCM increases with maneuvers that decrease LV cavity size (Valsalva strain, standing), because lower LV filling allows the anterior mitral leaflet to approximate the hypertrophied septum more closely (dynamic outflow obstruction). Most other murmurs soften with Valsalva.",
                        clinicalPearls = "Valsalva decreases preload. HCM and Mitral Valve Prolapse murmur durations INCREASE with decreased preload; almost all other systolic murmurs DECREASE.",
                        difficulty = "Clinical Reasoning"
                    )
                )
                dao.insertQuestion(
                    MedicalQuestion(
                        questionText = "A patient with acute decompensated heart failure exhibits warm extremities and elevated JVP with bibasilar crackles. According to the Forrester hemodynamic classification, which subset does this patient fall into?",
                        topic = "Hemodynamics",
                        optionsJson = "Subset I (Warm & Dry)|||Subset II (Warm & Wet)|||Subset III (Cold & Dry)|||Subset IV (Cold & Wet)",
                        correctIndex = 1,
                        explanation = "Warm extremities indicate adequate cardiac output / peripheral perfusion (>2.2 L/min/m²). Elevated JVP and pulmonary crackles indicate elevated pulmonary capillary wedge pressure (>18 mmHg). Hence: Warm and Wet (Subset II).",
                        clinicalPearls = "Subset II (Warm & Wet) is primarily treated with IV loop diuretics and vasodilators if blood pressure permits.",
                        difficulty = "Board Exam"
                    )
                )

                // Clinical Case
                dao.insertCase(
                    ClinicalCase(
                        title = "The Case of the Shortness of Breath",
                        patientName = "Arthur Sterling",
                        patientAgeGender = "67-year-old male, retired architect",
                        chiefComplaint = "\"I wake up coughing and feeling like I can't catch my breath for the past three nights.\"",
                        historyText = "Mr. Sterling reports progressive shortness of breath over two weeks. He now sleeps on 3 pillows (orthopnea) and wakes up gasping 2 hours after falling asleep (paroxysmal nocturnal dyspnea). Past medical history: Hypertension x 15 years, myocardial infarction 3 years ago.",
                        examFindings = "Vitals: BP 152/92 mmHg, HR 96 bpm regular, RR 24/min, SpO2 91% on room air. JVP elevated at 6 cm above sternal angle with positive hepatojugular reflux. Auscultation: Bibasilar inspiratory fine crackles halfway up lung fields. S3 gallop audible at apex. Bilateral 2+ pitting pretibial edema.",
                        labData = "NT-proBNP: 3,420 pg/mL (elevated). Serum creatinine: 1.2 mg/dL. ECG: Sinus rhythm with old anterior Q waves. Chest X-ray: Cardiomegaly with pulmonary vascular congestion, Kerley B lines.",
                        primaryDiagnosis = "Acute Decompensated Heart Failure (HFrEF secondary to Ischemic Cardiomyopathy)",
                        differentialDiagnosesJson = "Acute Exacerbation of COPD|||Community-Acquired Pneumonia|||Pulmonary Embolism|||Acute Decompensated Heart Failure",
                        managementSummary = "1. Supplemental O2 to maintain SpO2 >92%.\n2. Intravenous Loop Diuretic (Furosemide 40 mg IV bolus).\n3. Monitor urine output and daily weights.\n4. Optimize GDMT once stable (ARNI, BB, MRA, SGLT2i).\n5. Cardiology consultation & repeat transthoracic echocardiogram.",
                        isResolved = false
                    )
                )

                // Lecture Documents
                dao.insertDocument(
                    LectureDocument(
                        title = "Cardiology Module 04: Heart Failure Principles",
                        subject = "Cardiology",
                        pageCount = 38,
                        extractedSummary = "Core pathophysiology: Myocardial injury leads to decreased stroke volume and neurohormonal activation (RAAS and Sympathetic Nervous System). Long-term compensatory remodeling causes fibrosis, chamber dilation, and worsening systolic dysfunction. Guideline-directed medical therapy (GDMT) targets these neurohormonal pathways to attenuate remodeling."
                    )
                )
                dao.insertDocument(
                    LectureDocument(
                        title = "Clinical Skills: Cardiovascular Examination & SBAR",
                        subject = "Clinical Medicine",
                        pageCount = 22,
                        extractedSummary = "Standardized SBAR delivery for ward handovers: Situation (Patient name, bed, urgent concern), Background (Admitting diagnosis, baseline EF, past cardiac history), Assessment (Current vitals, auscultation findings, fluid balance), Recommendation (IV diuresis adjustment, lab checks, senior review)."
                    )
                )

                // Cosmetics
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "coat_white",
                        name = "Apprentice White Coat",
                        type = "OUTFIT",
                        cost = 0,
                        isPurchased = true,
                        isEquipped = true,
                        description = "Crisp, standard-issue coat for newly arrived medical scholars.",
                        iconEmoji = "🥼"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "robe_violet",
                        name = "Royal Violet Robe",
                        type = "OUTFIT",
                        cost = 100,
                        isPurchased = false,
                        isEquipped = false,
                        description = "Woven with silver thread for masters of physiology and pharmacology.",
                        iconEmoji = "🥋"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "vest_medic",
                        name = "Field Medic Vest",
                        type = "OUTFIT",
                        cost = 150,
                        isPurchased = false,
                        isEquipped = false,
                        description = "Utility pockets for rapid clinical triage and emergency diagnostic tools.",
                        iconEmoji = "🦺"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "steth_classic",
                        name = "Classic Stethoscope",
                        type = "ACCESSORY",
                        cost = 0,
                        isPurchased = true,
                        isEquipped = true,
                        description = "A reliable acoustic companion tuned for murmurs and gallops.",
                        iconEmoji = "🩺"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "glasses_sage",
                        name = "Sage Reading Spectacles",
                        type = "ACCESSORY",
                        cost = 80,
                        isPurchased = false,
                        isEquipped = false,
                        description = "Enhances focus when analyzing complex statistical trials and forest plots.",
                        iconEmoji = "👓"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "mortarboard",
                        name = "Academy Mortarboard",
                        type = "HEADWEAR",
                        cost = 120,
                        isPurchased = false,
                        isEquipped = false,
                        description = "Symbolizes scholarly dedication and rigorous retrieval practice.",
                        iconEmoji = "🎓"
                    )
                )
                dao.insertCosmeticItem(
                    CosmeticItem(
                        id = "laurels_gold",
                        name = "Golden Healer Laurels",
                        type = "HEADWEAR",
                        cost = 200,
                        isPurchased = false,
                        isEquipped = false,
                        description = "Bestowed upon students who master all five academy learning disciplines.",
                        iconEmoji = "👑"
                    )
                )
            }
        }
    }
}
