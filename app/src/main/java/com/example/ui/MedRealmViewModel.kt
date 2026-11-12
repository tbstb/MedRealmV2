package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MedRealmRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Tuple5<A, B, C, D, E>(
    val profile: A,
    val tasks: B,
    val flashcards: C,
    val questions: D,
    val cases: E
)

data class MainUiState(
    val userProfile: UserProfile? = null,
    val tasks: List<StudyTask> = emptyList(),
    val flashcards: List<Flashcard> = emptyList(),
    val questions: List<MedicalQuestion> = emptyList(),
    val cases: List<ClinicalCase> = emptyList(),
    val documents: List<LectureDocument> = emptyList(),
    val inventory: List<CosmeticItem> = emptyList(),
    val activeSubScreen: String? = null // null for main bottom tabs, or "FLASHCARDS", "MCQ", "CLINICAL_CASE", "PHYSIOCHAIN", "SPEAKING", "RESEARCH", "LECTURE_LIBRARY", "SCIENCE"
)

class MedRealmViewModel(private val repository: MedRealmRepository) : ViewModel() {

    private val _activeSubScreen = MutableStateFlow<String?>(null)

    private val _dataFlow = combine(
        repository.userProfile,
        repository.allTasks,
        repository.allFlashcards,
        repository.allQuestions,
        repository.allCases
    ) { profile, tasks, flashcards, questions, cases ->
        Tuple5(profile, tasks, flashcards, questions, cases)
    }

    private val _auxFlow = combine(
        repository.allDocuments,
        repository.inventory,
        _activeSubScreen
    ) { docs, inv, subScreen ->
        Triple(docs, inv, subScreen)
    }

    val uiState: StateFlow<MainUiState> = combine(
        _dataFlow,
        _auxFlow
    ) { data, aux ->
        MainUiState(
            userProfile = data.profile,
            tasks = data.tasks,
            flashcards = data.flashcards,
            questions = data.questions,
            cases = data.cases,
            documents = aux.first,
            inventory = aux.second,
            activeSubScreen = aux.third
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun navigateToSubScreen(screenKey: String?) {
        _activeSubScreen.value = screenKey
    }

    fun completeTask(task: StudyTask) {
        viewModelScope.launch {
            repository.completeTask(task)
        }
    }

    fun reviewFlashcard(card: Flashcard, qualityScore: Int) {
        viewModelScope.launch {
            repository.reviewFlashcard(card, qualityScore)
        }
    }

    fun addFlashcard(front: String, back: String, topic: String) {
        viewModelScope.launch {
            repository.addFlashcard(
                Flashcard(
                    front = front,
                    back = back,
                    topic = topic
                )
            )
        }
    }

    fun completeMcqSession(correctCount: Int, total: Int, confidenceRating: Float = 80f) {
        viewModelScope.launch {
            val accuracy = if (total > 0) correctCount.toFloat() / total.toFloat() else 1.0f
            repository.awardEducationalRewards(
                taskType = com.example.learning.EducationalTaskType.MCQ_SESSION,
                accuracyScore = accuracy,
                confidenceRating = confidenceRating
            )
            _activeSubScreen.value = null
        }
    }

    fun completeClinicalCase(clinicalCase: ClinicalCase) {
        viewModelScope.launch {
            repository.resolveCase(clinicalCase)
        }
    }

    fun completePhysioChain() {
        viewModelScope.launch {
            repository.awardEducationalRewards(
                taskType = com.example.learning.EducationalTaskType.PHYSIOCHAIN_PUZZLE,
                accuracyScore = 1.0f,
                confidenceRating = 90f
            )
        }
    }

    fun completeSpeech() {
        viewModelScope.launch {
            repository.awardEducationalRewards(
                taskType = com.example.learning.EducationalTaskType.SPEAKING_OSCE_ROUND,
                accuracyScore = 1.0f,
                confidenceRating = 85f
            )
        }
    }

    fun addDocument(title: String, subject: String, pages: Int, summary: String) {
        viewModelScope.launch {
            repository.addDocument(
                LectureDocument(
                    title = title,
                    subject = subject,
                    pageCount = pages,
                    extractedSummary = summary
                )
            )
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun selectCompanion(companionId: String) {
        viewModelScope.launch {
            val current = repository.getUserProfileOnce() ?: return@launch
            repository.saveUserProfile(current.copy(selectedCompanionId = companionId))
        }
    }

    fun purchaseCosmetic(item: CosmeticItem) {
        viewModelScope.launch {
            repository.purchaseCosmetic(item)
        }
    }

    fun equipCosmetic(item: CosmeticItem) {
        viewModelScope.launch {
            repository.equipCosmetic(item)
        }
    }
}
