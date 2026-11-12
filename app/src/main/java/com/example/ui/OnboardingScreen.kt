package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("Apprentice Scholar") }
    var academicYear by remember { mutableStateOf("Year 3 (Clinical Clerkships)") }
    var mainSubject by remember { mutableStateOf("Cardiology & Internal Medicine") }
    var targetMinutes by remember { mutableFloatStateOf(20f) }
    var upcomingExam by remember { mutableStateOf("Internal Medicine Boards & OSCE") }
    var selectedCompanion by remember { mutableStateOf("orion") }

    val years = listOf("Year 1 (Pre-Clinical Foundations)", "Year 2 (Pathology & Systems)", "Year 3 (Clinical Clerkships)", "Year 4 (Advanced Sub-I & Boards)", "Resident / Post-Grad")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text("🏰", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Welcome to MedRealm",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = RewardGold
                )
                Text(
                    "The Medical Learning Adventure",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MedicalTeal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Personalize your clinical syllabus, study routine, and fantasy academy companion.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("1. Academic Identity", fontWeight = FontWeight.Bold, color = MedicalTeal)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Character / Student Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MedicalTeal,
                            unfocusedBorderColor = NavyElevated,
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream
                        )
                    )

                    OutlinedTextField(
                        value = upcomingExam,
                        onValueChange = { upcomingExam = it },
                        label = { Text("Next Milestone / Upcoming Examination") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MedicalTeal,
                            unfocusedBorderColor = NavyElevated,
                            focusedTextColor = WarmCream,
                            unfocusedTextColor = WarmCream
                        )
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("2. Study Schedule & Pace", fontWeight = FontWeight.Bold, color = RewardGold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Daily Target Session:", fontSize = 13.sp, color = WarmCream)
                        Text("${targetMinutes.toInt()} minutes", fontWeight = FontWeight.Bold, color = RewardGold)
                    }

                    Slider(
                        value = targetMinutes,
                        onValueChange = { targetMinutes = it },
                        valueRange = 10f..60f,
                        steps = 9,
                        colors = SliderDefaults.colors(thumbColor = RewardGold, activeTrackColor = RewardGold)
                    )
                    Text(
                        "Sustainable 15-25 minute blocks with spaced intervals protect against cognitive burnout.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("3. Choose Starter Companion", fontWeight = FontWeight.Bold, color = MedicalTeal)
                    Spacer(modifier = Modifier.height(10.dp))

                    val companions = listOf(
                        Pair("orion", "🦉 Orion the Owl (Knowledge & Flashcards)"),
                        Pair("ember", "🦊 Ember the Fox (Clinical Reasoning)"),
                        Pair("nova", "🐲 Nova the Dragon (Speaking & OSCE)"),
                        Pair("atlas", "🤖 Atlas the Golem (Research & Evidence)")
                    )

                    companions.forEach { (id, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedCompanion == id,
                                onClick = { selectedCompanion = id },
                                colors = RadioButtonDefaults.colors(selectedColor = MedicalTeal)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label, color = WarmCream, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    val profile = UserProfile(
                        id = 1,
                        characterName = if (name.isNotBlank()) name else "Apprentice Scholar",
                        academicYear = academicYear,
                        mainSubject = mainSubject,
                        upcomingExam = upcomingExam,
                        dailyTargetMinutes = targetMinutes.toInt(),
                        selectedCompanionId = selectedCompanion,
                        isOnboarded = true
                    )
                    onCompleteOnboarding(profile)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("complete_onboarding_button")
            ) {
                Text("Enter the MedRealm Academy", color = MidnightNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
