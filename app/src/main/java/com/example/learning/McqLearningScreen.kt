package com.example.learning

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MedicalQuestion
import com.example.ui.theme.*

@Composable
fun McqLearningScreen(
    questions: List<MedicalQuestion>,
    onCompleteSession: (correctCount: Int, totalCount: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var confidenceRating by remember { mutableFloatStateOf(80f) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    val currentQuestion = questions.getOrNull(currentIndex)
    val options = remember(currentQuestion) {
        currentQuestion?.optionsJson?.split("|||") ?: emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_mcq")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Clinical MCQ Mastery",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    if (questions.isNotEmpty()) "Question ${currentIndex + 1} of ${questions.size}" else "Complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            Surface(
                color = MedicalTealContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "$correctAnswersCount / ${questions.size}",
                    color = MedicalTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        if (currentQuestion == null) {
            // End of MCQ Quiz Summary
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🏆", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "MCQ Session Completed!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RewardGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Score: $correctAnswersCount out of ${questions.size} correct (${(correctAnswersCount * 100f / questions.size.coerceAtLeast(1)).toInt()}%)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = WarmCream
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "+${correctAnswersCount * 40} XP and +${correctAnswersCount * 25} Coins awarded to your character!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MedicalTeal
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onCompleteSession(correctAnswersCount, questions.size) },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                        modifier = Modifier.testTag("finish_mcq_button")
                    ) {
                        Text("Collect Rewards & Finish", color = MidnightNavy, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Question Vignette Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = MedicalTealContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        currentQuestion.topic,
                                        color = MedicalTeal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    color = RewardGoldContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        currentQuestion.difficulty,
                                        color = RewardGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQuestion.questionText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = WarmCream,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                // Options
                itemsIndexed(options) { idx, optionText ->
                    val isSelected = selectedOptionIndex == idx
                    val isCorrect = idx == currentQuestion.correctIndex

                    val borderColor = when {
                        !isAnswerSubmitted && isSelected -> MedicalTeal
                        isAnswerSubmitted && isCorrect -> SuccessGreen
                        isAnswerSubmitted && isSelected && !isCorrect -> ErrorRed
                        else -> Color(0x33FFFFFF)
                    }

                    val containerColor = when {
                        !isAnswerSubmitted && isSelected -> MedicalTealContainer
                        isAnswerSubmitted && isCorrect -> Color(0xFF1B5E20)
                        isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFB71C1C)
                        else -> DeepNavySurface
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isAnswerSubmitted) {
                                selectedOptionIndex = idx
                            }
                            .testTag("option_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + idx)}. ",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected || (isAnswerSubmitted && isCorrect)) WarmCream else Color.LightGray,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmCream,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Confidence Calibration Slider (Metacognitive monitoring)
                if (!isAnswerSubmitted) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Confidence Calibration",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = RewardGold
                                    )
                                    Text(
                                        "${confidenceRating.toInt()}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MedicalTeal
                                    )
                                }
                                Slider(
                                    value = confidenceRating,
                                    onValueChange = { confidenceRating = it },
                                    valueRange = 0f..100f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = MedicalTeal,
                                        activeTrackColor = MedicalTeal,
                                        inactiveTrackColor = NavyElevated
                                    )
                                )
                                Text(
                                    "Calibrating your confidence against actual correctness helps eliminate overconfidence traps in clinical exams.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Explanation & Clinical Pearl after submit
                if (isAnswerSubmitted) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NavyElevated),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MedicalTeal),
                            modifier = Modifier.fillMaxWidth().testTag("mcq_explanation_card")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💡", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Detailed Mechanism & Explanation",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MedicalTeal
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarmCream,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    color = DeepNavySurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            "⭐ Clinical Pearl",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = RewardGold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentQuestion.clinicalPearls,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button
            if (!isAnswerSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isAnswerSubmitted = true
                            if (selectedOptionIndex == currentQuestion.correctIndex) {
                                correctAnswersCount += 1
                            }
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("submit_mcq_answer_button")
                ) {
                    Text("Submit Answer", color = MidnightNavy, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {
                        isAnswerSubmitted = false
                        selectedOptionIndex = null
                        confidenceRating = 80f
                        currentIndex += 1
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("next_mcq_question_button")
                ) {
                    Text(
                        if (currentIndex < questions.size - 1) "Next Question" else "View Session Results",
                        color = MidnightNavy,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
