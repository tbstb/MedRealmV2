package com.example.ai

import android.util.Log
import com.example.data.model.Flashcard
import com.example.data.model.MedicalQuestion
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "MedRealmAIEngine"

sealed interface AiGenerationState {
    data object Idle : AiGenerationState
    data object Loading : AiGenerationState
    data class Streaming(val text: String) : AiGenerationState
    data class Success(val message: String) : AiGenerationState
    data class Error(val errorMessage: String) : AiGenerationState
}

/**
 * Service that connects to Gemini 3.8 Flash via Firebase AI Logic and App Check attestation.
 */
class MedRealmAIEngine {

    // General text & chat model (Gemini 3.8 Flash, lazy initialization)
    private val generalModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash"
        )
    }

    // Structured JSON model for generating flashcards from medical text
    private val flashcardGenModel by lazy {
        val flashcardsSchema = Schema.array(
            items = Schema.obj(
                properties = mapOf(
                    "front" to Schema.string("The active recall question or prompt"),
                    "back" to Schema.string("The mechanistic answer and pathophysiological explanation"),
                    "topic" to Schema.string("Medical subject or organ system (e.g. Cardiology, Renal)")
                )
            ),
            description = "List of structured active recall medical flashcards"
        )
        val config = generationConfig {
            responseMimeType = "application/json"
            responseSchema = flashcardsSchema
        }
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = config
        )
    }

    // Structured JSON model for generating board-style MCQs
    private val mcqGenModel by lazy {
        val mcqSchema = Schema.obj(
            properties = mapOf(
                "questionText" to Schema.string("Clinical vignette describing patient presentation"),
                "topic" to Schema.string("Medical subspecialty"),
                "options" to Schema.array(
                    items = Schema.string(),
                    description = "Exactly 4 multiple choice options"
                ),
                "correctIndex" to Schema.integer("Zero-indexed correct answer (0, 1, 2, or 3)"),
                "explanation" to Schema.string("Mechanistic explanation of why the correct option is right and distractors are wrong"),
                "clinicalPearls" to Schema.string("High-yield board examination pearl"),
                "difficulty" to Schema.string("Difficulty label: High Yield, Clinical Reasoning, or Board Exam")
            )
        )
        val config = generationConfig {
            responseMimeType = "application/json"
            responseSchema = mcqSchema
        }
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = config
        )
    }

    /**
     * Streams AI Mentor coaching advice to minimize time-to-first-token.
     */
    fun streamMentorAdvice(prompt: String): Flow<String> = flow {
        val stream = withTimeout(60_000L) {
            generalModel.generateContentStream(prompt)
        }
        val accumulated = java.lang.StringBuilder()
        stream.collect { chunk ->
            val piece = chunk.text.orEmpty()
            accumulated.append(piece)
            emit(accumulated.toString())
        }
    }

    /**
     * Evaluates student's oral or typed clinical presentation against OSCE examiner criteria.
     */
    suspend fun evaluateClinicalSpeech(
        scenarioPrompt: String,
        studentResponse: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fullPrompt = """
                You are a senior attending physician and medical board examiner evaluating a medical student's ward presentation.
                Scenario: $scenarioPrompt
                Student's Delivery: $studentResponse
                
                Evaluate the student's clinical delivery across three dimensions:
                1. Organization (Did they adhere to SBAR or a clear clinical structure?)
                2. Medical Accuracy & Acuity (Are diagnoses, signs, and doses correct?)
                3. Clarity & Conciseness (Is it focused, professional, and free of fluff?)
                
                Provide constructive, encouraging feedback and specific praise or areas for improvement.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                generalModel.generateContent(fullPrompt)
            }
            val text = response.text
            if (!text.isNullOrBlank()) {
                Result.success(text)
            } else {
                Result.failure(Exception("Model returned an empty evaluation."))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Speech evaluation failed", e)
            Result.failure(e)
        }
    }

    /**
     * Synthesizes uploaded syllabus or lecture text into structured active recall flashcards.
     */
    suspend fun extractFlashcardsFromLecture(
        documentTitle: String,
        lectureText: String
    ): Result<List<Flashcard>> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Extract 3 to 5 high-yield active recall flashcards from this medical lecture syllabus.
                Lecture Title: $documentTitle
                Content: $lectureText
                
                Ensure questions test mechanisms, pathophysiology, and diagnostic criteria rather than trivial trivia.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                flashcardGenModel.generateContent(prompt)
            }
            val jsonText = response.text ?: throw IllegalStateException("Model returned empty JSON.")
            val jsonArray = JSONArray(jsonText)
            val cards = mutableListOf<Flashcard>()
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                cards.add(
                    Flashcard(
                        front = item.optString("front", "Key clinical question"),
                        back = item.optString("back", "Detailed clinical mechanism"),
                        topic = item.optString("topic", "Cardiology")
                    )
                )
            }
            Result.success(cards)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Flashcard extraction failed", e)
            Result.failure(e)
        }
    }

    /**
     * Generates a board-style MCQ from lecture notes.
     */
    suspend fun generateQuizFromLecture(
        documentTitle: String,
        lectureText: String
    ): Result<MedicalQuestion> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Generate a board-style multiple choice clinical vignette based on this medical document:
                Title: $documentTitle
                Summary: $lectureText
            """.trimIndent()

            val response = withTimeout(60_000L) {
                mcqGenModel.generateContent(prompt)
            }
            val jsonText = response.text ?: throw IllegalStateException("Empty MCQ response from Gemini.")
            val obj = JSONObject(jsonText)
            val optionsArr = obj.getJSONArray("options")
            val optionsList = (0 until optionsArr.length()).map { optionsArr.getString(it) }

            val question = MedicalQuestion(
                questionText = obj.getString("questionText"),
                topic = obj.optString("topic", "Internal Medicine"),
                optionsJson = optionsList.joinToString("|||"),
                correctIndex = obj.getInt("correctIndex").coerceIn(0, 3),
                explanation = obj.getString("explanation"),
                clinicalPearls = obj.optString("clinicalPearls", "High-yield concept tested on boards."),
                difficulty = obj.optString("difficulty", "High Yield")
            )
            Result.success(question)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "MCQ quiz generation failed", e)
            Result.failure(e)
        }
    }
}
