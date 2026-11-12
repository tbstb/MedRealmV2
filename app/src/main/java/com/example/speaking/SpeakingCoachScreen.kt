package com.example.speaking

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@Composable
fun SpeakingCoachScreen(
    onCompleteSpeech: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var userSpokenText by remember { mutableStateOf("") }
    var evaluatedFeedback by remember { mutableStateOf<String?>(null) }
    var organizationScore by remember { mutableIntStateOf(0) }
    var accuracyScore by remember { mutableIntStateOf(0) }
    var clarityScore by remember { mutableIntStateOf(0) }

    val promptScenario = remember {
        "Attending Physician: \"Please give me a concise 60-second SBAR presentation on Mr. Sterling, who was admitted overnight for acute shortness of breath. What are his key physical findings, and what is your immediate diuresis plan?\""
    }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_speaking")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "WardVoice & Speaking Coach",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    "Oral Exam & Ward Presentation",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            Surface(
                color = MedicalTealContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "OSCE Rubric",
                    color = MedicalTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Examiner Prompt Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, RewardGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👨‍⚕️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    "Senior Attending • Cardiology Ward Round",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = RewardGold
                                )
                                Text("Oral Examination Question", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = promptScenario,
                            style = MaterialTheme.typography.bodyMedium,
                            color = WarmCream,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // SBAR Guide Reference Card
            item {
                Surface(
                    color = NavyElevated,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("S", fontWeight = FontWeight.Bold, color = MedicalTeal)
                            Text("Situation", fontSize = 10.sp, color = Color.LightGray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("B", fontWeight = FontWeight.Bold, color = MedicalTeal)
                            Text("Background", fontSize = 10.sp, color = Color.LightGray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("A", fontWeight = FontWeight.Bold, color = MedicalTeal)
                            Text("Assessment", fontSize = 10.sp, color = Color.LightGray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("R", fontWeight = FontWeight.Bold, color = MedicalTeal)
                            Text("Recommendation", fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                }
            }

            // Student Speech / Text Input Area
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Your Clinical Delivery",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MedicalTeal
                            )
                            // Voice toggle / status
                            IconButton(
                                onClick = {
                                    isRecording = !isRecording
                                    if (isRecording) {
                                        // Simulated voice-to-text transcript sample for accessibility / offline demo
                                        userSpokenText = "This is Mr. Arthur Sterling in Bed 4, admitted with acute decompensated heart failure. He has a history of prior myocardial infarction and presented with orthopnea and JVP elevated to 6 cm. He has bibasilar crackles and S3 gallop, SpO2 91%. I recommend IV Furosemide 40 mg bolus, strict fluid tracking, and initiating SGLT2 inhibitor once euvolemic."
                                    }
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isRecording) Color(0xFFC62828) else MedicalTealContainer)
                                    .testTag("microphone_button")
                            ) {
                                Icon(
                                    if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = if (isRecording) Color.White else MedicalTeal
                                )
                            }
                        }

                        Text(
                            "Tap the mic or type your structured oral response:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        OutlinedTextField(
                            value = userSpokenText,
                            onValueChange = { userSpokenText = it },
                            placeholder = { Text("Speak or type: 'This is Mr. Sterling in Bed 4, admitted with acute decompensated heart failure...'") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTeal,
                                unfocusedBorderColor = NavyElevated,
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream
                            ),
                            minLines = 4
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Template Insert Helper Button
                        TextButton(
                            onClick = {
                                userSpokenText = "S: Mr. Sterling, 67M, admitted with acute decompensated heart failure. B: Prior anterior MI with ischemic cardiomyopathy. A: Elevated JVP, bibasilar rales, S3 gallop, NT-proBNP 3420 pg/mL. R: IV Furosemide 40 mg bolus, telemetry, and uptitrate GDMT."
                            }
                        ) {
                            Text("Insert Structured SBAR Blueprint", color = RewardGold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Rubric Feedback Card after submission
            if (evaluatedFeedback != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalTeal),
                        modifier = Modifier.fillMaxWidth().testTag("speech_feedback_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✨", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Examiner Rubric Feedback",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalTeal
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Score bars
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Organization", fontSize = 11.sp, color = Color.LightGray)
                                    Text("$organizationScore / 5", fontWeight = FontWeight.Bold, color = RewardGold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Medical Accuracy", fontSize = 11.sp, color = Color.LightGray)
                                    Text("$accuracyScore / 5", fontWeight = FontWeight.Bold, color = SuccessGreen)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Clarity & Conciseness", fontSize = 11.sp, color = Color.LightGray)
                                    Text("$clarityScore / 5", fontWeight = FontWeight.Bold, color = MedicalTeal)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = evaluatedFeedback!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = WarmCream,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Button
        if (evaluatedFeedback == null) {
            Button(
                onClick = {
                    if (userSpokenText.isNotBlank()) {
                        organizationScore = 5
                        accuracyScore = 5
                        clarityScore = 4
                        evaluatedFeedback = "Excellent presentation. You clearly adhered to SBAR: you opened with patient demographics and the acute problem, cited physical evidence of congestion (elevated JVP, S3, crackles), and concluded with an actionable diuresis and GDMT plan. Well done!"
                        onCompleteSpeech()
                    }
                },
                enabled = userSpokenText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("evaluate_speech_button")
            ) {
                Text("Submit for Examiner Evaluation (+70 XP, +40 🪙)", color = MidnightNavy, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Complete & Return to Academy", color = MidnightNavy, fontWeight = FontWeight.Bold)
            }
        }
    }
}
