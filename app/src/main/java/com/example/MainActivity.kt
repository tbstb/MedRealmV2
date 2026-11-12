package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.configureAppCheck
import com.example.clinical.ClinicalCaseScreen
import com.example.data.database.MedRealmDatabase
import com.example.data.model.StudyTask
import com.example.data.repository.MedRealmRepository
import com.example.learning.*
import com.example.research.ResearchAcademyScreen
import com.example.speaking.SpeakingCoachScreen
import com.example.ui.*
import com.example.ui.theme.DeepNavySurface
import com.example.ui.theme.MedRealmTheme
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.RewardGold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure App Check before setContent, ViewModels, or Firebase operations
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MedRealmTheme {
                val context = LocalContext.current
                val database = remember { MedRealmDatabase.getDatabase(context) }
                val repository = remember { MedRealmRepository(database.dao()) }
                val viewModel: MedRealmViewModel = viewModel {
                    MedRealmViewModel(repository)
                }

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // Check onboarding status
                if (uiState.userProfile != null && !uiState.userProfile!!.isOnboarded) {
                    OnboardingScreen(
                        onCompleteOnboarding = { newProfile ->
                            viewModel.updateProfile(newProfile)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    )
                } else {
                    MedRealmAppRoot(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }
    }
}

enum class MainTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector) {
    TODAY("Today", Icons.Outlined.Dashboard, Icons.Filled.Dashboard),
    LEARN("Learn", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
    PRACTICE("Practice", Icons.Outlined.LocalHospital, Icons.Filled.LocalHospital),
    WORLD("World", Icons.Outlined.Public, Icons.Filled.Public)
}

@Composable
fun MedRealmAppRoot(
    viewModel: MedRealmViewModel,
    uiState: MainUiState
) {
    var selectedTab by remember { mutableStateOf(MainTab.TODAY) }

    // Intercept hardware/gesture Back key when in sub-screens
    BackHandler(enabled = uiState.activeSubScreen != null) {
        viewModel.navigateToSubScreen(null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (uiState.activeSubScreen == null) {
                NavigationBar(
                    containerColor = DeepNavySurface,
                    contentColor = MedicalTeal,
                    modifier = Modifier.navigationBarsPadding().testTag("bottom_navigation_bar")
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) MedicalTeal else Color.LightGray
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    color = if (isSelected) MedicalTeal else Color.LightGray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = DeepNavySurface
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MidnightNavy)
        ) {
            // Check if user is navigating an active learning subscreen
            when (uiState.activeSubScreen) {
                "FLASHCARDS" -> {
                    FlashcardReviewScreen(
                        flashcards = uiState.flashcards,
                        onReviewComplete = { card, score ->
                            viewModel.reviewFlashcard(card, score)
                        },
                        onAddNewCard = { front, back, topic ->
                            viewModel.addFlashcard(front, back, topic)
                        },
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "MCQ" -> {
                    McqLearningScreen(
                        questions = uiState.questions,
                        onCompleteSession = { correct, total ->
                            viewModel.completeMcqSession(correct, total)
                        },
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "CLINICAL_CASE" -> {
                    val firstCase = uiState.cases.firstOrNull()
                    if (firstCase != null) {
                        ClinicalCaseScreen(
                            clinicalCase = firstCase,
                            onCaseCompleted = {
                                viewModel.completeClinicalCase(firstCase)
                            },
                            onBack = { viewModel.navigateToSubScreen(null) }
                        )
                    } else {
                        viewModel.navigateToSubScreen(null)
                    }
                }
                "PHYSIOCHAIN" -> {
                    PhysioChainScreen(
                        onCompleteChain = {
                            viewModel.completePhysioChain()
                        },
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "SPEAKING" -> {
                    SpeakingCoachScreen(
                        onCompleteSpeech = {
                            viewModel.completeSpeech()
                        },
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "RESEARCH" -> {
                    ResearchAcademyScreen(
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "LECTURE_LIBRARY" -> {
                    LectureLibraryScreen(
                        documents = uiState.documents,
                        onAddSampleDocument = { title, subject, pages, summary ->
                            viewModel.addDocument(title, subject, pages, summary)
                        },
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                "SCIENCE" -> {
                    LearningScienceScreen(
                        onBack = { viewModel.navigateToSubScreen(null) }
                    )
                }
                else -> {
                    // Main 4 Bottom Tabs
                    when (selectedTab) {
                        MainTab.TODAY -> {
                            TodayDashboardScreen(
                                userProfile = uiState.userProfile,
                                studyTasks = uiState.tasks,
                                onStartTask = { task ->
                                    handleTaskNavigation(task, viewModel)
                                },
                                onQuickStartAction = { action ->
                                    viewModel.navigateToSubScreen(action)
                                },
                                onOpenScienceGuide = {
                                    viewModel.navigateToSubScreen("SCIENCE")
                                }
                            )
                        }
                        MainTab.LEARN -> {
                            LearnHubScreen(
                                onNavigate = { destination ->
                                    viewModel.navigateToSubScreen(destination)
                                }
                            )
                        }
                        MainTab.PRACTICE -> {
                            PracticeHubScreen(
                                onNavigate = { destination ->
                                    viewModel.navigateToSubScreen(destination)
                                }
                            )
                        }
                        MainTab.WORLD -> {
                            WorldHubScreen(
                                userProfile = uiState.userProfile,
                                inventory = uiState.inventory,
                                onEnterBuilding = { buildingId ->
                                    when (buildingId) {
                                        "LIBRARY" -> viewModel.navigateToSubScreen("LECTURE_LIBRARY")
                                        "HOSPITAL" -> viewModel.navigateToSubScreen("CLINICAL_CASE")
                                        "SPEAKING_HALL" -> viewModel.navigateToSubScreen("SPEAKING")
                                        "RESEARCH_TOWER" -> viewModel.navigateToSubScreen("RESEARCH")
                                    }
                                },
                                onProfileUpdated = { updated ->
                                    viewModel.updateProfile(updated)
                                },
                                onSelectCompanion = { companionId ->
                                    viewModel.selectCompanion(companionId)
                                },
                                onPurchaseCosmetic = { item ->
                                    viewModel.purchaseCosmetic(item)
                                },
                                onEquipCosmetic = { item ->
                                    viewModel.equipCosmetic(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun handleTaskNavigation(task: StudyTask, viewModel: MedRealmViewModel) {
    viewModel.completeTask(task)
    when (task.systemType) {
        "STUDY_ENGINE" -> viewModel.navigateToSubScreen("FLASHCARDS")
        "KNOWLEDGE_LAB" -> viewModel.navigateToSubScreen("PHYSIOCHAIN")
        "CLINICAL_ARENA" -> viewModel.navigateToSubScreen("CLINICAL_CASE")
        "SPEAKING_STUDIO" -> viewModel.navigateToSubScreen("SPEAKING")
        "RESEARCH_ACADEMY" -> viewModel.navigateToSubScreen("RESEARCH")
        else -> viewModel.navigateToSubScreen("MCQ")
    }
}
