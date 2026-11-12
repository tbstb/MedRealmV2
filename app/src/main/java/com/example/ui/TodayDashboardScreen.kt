package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.StudyTask
import com.example.data.model.UserProfile
import com.example.game.GameData
import com.example.ui.theme.*

@Composable
fun TodayDashboardScreen(
    userProfile: UserProfile?,
    studyTasks: List<StudyTask>,
    onStartTask: (StudyTask) -> Unit,
    onQuickStartAction: (String) -> Unit,
    onOpenScienceGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeCompanion = remember(userProfile?.selectedCompanionId) {
        GameData.companions.find { it.id == userProfile?.selectedCompanionId } ?: GameData.companions[0]
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Character & Level Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalTeal),
                modifier = Modifier.fillMaxWidth().testTag("hero_character_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(NavyElevated)
                                    .border(2.dp, RewardGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🩺", fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    userProfile?.characterName ?: "Apprentice Healer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmCream
                                )
                                Text(
                                    "Level ${userProfile?.level ?: 1} Scholar • ${userProfile?.academicYear ?: "Year 3"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MedicalTeal
                                )
                            }
                        }

                        // Currency Badge
                        Surface(
                            color = RewardGoldContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🪙", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "${userProfile?.coins ?: 0}",
                                    color = RewardGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // XP Progress Bar
                    val currentXp = userProfile?.currentXp ?: 0
                    val reqXp = userProfile?.requiredXp ?: 300
                    val progressFraction = (currentXp.toFloat() / reqXp.toFloat()).coerceIn(0f, 1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "XP Progress to Level ${(userProfile?.level ?: 1) + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray
                        )
                        Text(
                            "$currentXp / $reqXp XP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = RewardGold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = RewardGold,
                        trackColor = NavyElevated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        }

        // AI Mentor Daily Recommendation Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = RoyalPurpleSubtle),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalPurpleLight),
                modifier = Modifier.fillMaxWidth().testTag("ai_mentor_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RoyalPurpleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(activeCompanion.emoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "Mentor Recommendation: ${activeCompanion.name}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RewardGold
                            )
                            Text("Adaptive Metacognitive Coach", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "\"You consolidated heart failure symptoms earlier. Today's optimal retention step: complete a 5-minute PhysioChain on preload vs afterload, followed by a simulated clinical case with Mr. Sterling.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmCream,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onQuickStartAction("PHYSIOCHAIN") },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("start_recommended_activity")
                    ) {
                        Text("Start Recommended Activity (5 min)", color = MidnightNavy, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Launch Floating Action Grid
        item {
            Text(
                "Immediate Learning Portals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WarmCream
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onQuickStartAction("FLASHCARDS") }
                        .testTag("portal_flashcards")
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🃏", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Flashcards", fontWeight = FontWeight.Bold, color = WarmCream, fontSize = 13.sp)
                        Text("Spaced Review", fontSize = 11.sp, color = MedicalTeal)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onQuickStartAction("MCQ") }
                        .testTag("portal_mcq")
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📝", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("MCQ Arena", fontWeight = FontWeight.Bold, color = WarmCream, fontSize = 13.sp)
                        Text("High-Yield Qs", fontSize = 11.sp, color = RewardGold)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onQuickStartAction("CLINICAL_CASE") }
                        .testTag("portal_case")
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏥", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Patient Case", fontWeight = FontWeight.Bold, color = WarmCream, fontSize = 13.sp)
                        Text("Simulation", fontSize = 11.sp, color = MedicalTeal)
                    }
                }
            }
        }

        // Today's Study Plan Tasks
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Today's Study Plan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    "Exam: ${userProfile?.upcomingExam ?: "Boards"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = RewardGold
                )
            }
        }

        items(studyTasks) { task ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (task.isCompleted) Color(0xFF13231B) else DeepNavySurface
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (task.isCompleted) SuccessGreen.copy(alpha = 0.5f) else Color(0x33FFFFFF)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartTask(task) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (task.isCompleted) SuccessGreen else MedicalTeal,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (task.isCompleted) Color.Gray else WarmCream
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${task.category} • ~${task.estimatedMinutes} min",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }
                    }
                    Surface(
                        color = RewardGoldContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "+${task.xpReward} XP",
                            color = RewardGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Learning Science Button
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenScienceGuide() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Evidence-Based Learning Science", fontWeight = FontWeight.Bold, color = WarmCream, fontSize = 14.sp)
                        Text("Read Dunlosky et al., Roediger & Karpicke retrieval research references", fontSize = 11.sp, color = Color.LightGray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MedicalTeal)
                }
            }
        }
    }
}
