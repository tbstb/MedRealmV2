package com.example.learning

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.Flashcard
import com.example.ui.theme.*

@Composable
fun FlashcardReviewScreen(
    flashcards: List<Flashcard>,
    onReviewComplete: (Flashcard, Int) -> Unit,
    onAddNewCard: (String, String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val currentCard = flashcards.getOrNull(currentIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_flashcards")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Spaced Retrieval (SM-2)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    if (flashcards.isNotEmpty()) "Card ${currentIndex + 1} of ${flashcards.size}" else "No cards",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Card", tint = RewardGold)
            }
        }

        if (currentCard == null) {
            // Empty / Completed State
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "All Reviews Complete!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = RewardGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "You've consolidated today's memory nodes. Spaced repetition protects against neural decay over time.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal)
                    ) {
                        Text("Return to Academy", color = MidnightNavy, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Flashcard Interactive Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (isFlipped) RewardGold else MedicalTeal),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { isFlipped = !isFlipped }
                    .testTag("flashcard_tap_target")
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Tag & Flip prompt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MedicalTealContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                currentCard.topic,
                                color = MedicalTeal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            if (isFlipped) "Showing Answer" else "Tap anywhere to flip",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    // Content
                    AnimatedContent(
                        targetState = isFlipped,
                        label = "card_flip"
                    ) { flipped ->
                        if (!flipped) {
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                                Text(
                                    "QUESTION",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RewardGold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = currentCard.front,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WarmCream,
                                    lineHeight = 30.sp
                                )
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                                Text(
                                    "MECHANISM & EXPLANATION",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalTeal,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = currentCard.back,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = WarmCream,
                                    lineHeight = 26.sp
                                )
                            }
                        }
                    }

                    // Interval info
                    Text(
                        "Current repetition: Level ${currentCard.repetitionLevel} • Interval: ${currentCard.intervalDays}d",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rating Buttons (SM-2: Again, Hard, Good, Easy)
            if (isFlipped) {
                Text(
                    "Rate your recall ease:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val ratings = listOf(
                        Triple("Again", 0, Color(0xFFEF5350)),
                        Triple("Hard", 1, Color(0xFFFFA726)),
                        Triple("Good", 2, Color(0xFF66BB6A)),
                        Triple("Easy", 3, MedicalTeal)
                    )
                    ratings.forEach { (label, score, color) ->
                        Button(
                            onClick = {
                                onReviewComplete(currentCard, score)
                                isFlipped = false
                                if (currentIndex < flashcards.size - 1) {
                                    currentIndex += 1
                                } else {
                                    currentIndex = flashcards.size
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = color.copy(alpha = 0.25f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, color),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("flashcard_rate_$label")
                        ) {
                            Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                Button(
                    onClick = { isFlipped = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("flashcard_reveal_button")
                ) {
                    Text("Show Answer", color = MidnightNavy, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Add Card Dialog
    if (showAddDialog) {
        var frontText by remember { mutableStateOf("") }
        var backText by remember { mutableStateOf("") }
        var topicText by remember { mutableStateOf("Cardiology") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create Flashcard", color = WarmCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = topicText,
                        onValueChange = { topicText = it },
                        label = { Text("Topic/Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = frontText,
                        onValueChange = { frontText = it },
                        label = { Text("Question / Prompt") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = backText,
                        onValueChange = { backText = it },
                        label = { Text("Answer / Mechanism") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (frontText.isNotBlank() && backText.isNotBlank()) {
                            onAddNewCard(frontText, backText, topicText)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal)
                ) {
                    Text("Add Card", color = MidnightNavy)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = DeepNavySurface
        )
    }
}
