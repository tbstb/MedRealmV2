package com.example.clinical

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.ClinicalCase
import com.example.ui.theme.*

@Composable
fun ClinicalCaseScreen(
    clinicalCase: ClinicalCase,
    onCaseCompleted: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableIntStateOf(0) }
    var selectedDiagnosis by remember { mutableStateOf<String?>(null) }
    var userReasoningText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    val differentials = remember(clinicalCase) {
        clinicalCase.differentialDiagnosesJson.split("|||")
    }

    val tabs = listOf("History & Chief Complaint", "Physical Exam", "Labs & Imaging", "Diagnostic Synthesis")

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_case")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    clinicalCase.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    clinicalCase.patientName + " • " + clinicalCase.patientAgeGender,
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            Surface(
                color = if (clinicalCase.isResolved || isSubmitted) Color(0xFF1B5E20) else RewardGoldContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    if (clinicalCase.isResolved || isSubmitted) "Resolved" else "Active Case",
                    color = if (clinicalCase.isResolved || isSubmitted) Color(0xFFA5D6A7) else RewardGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            containerColor = DeepNavySurface,
            contentColor = MedicalTeal,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontSize = 12.sp, fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Tab Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (activeTab) {
                0 -> {
                    // Chief Complaint & History
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Chief Complaint", style = MaterialTheme.typography.titleSmall, color = RewardGold, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(clinicalCase.chiefComplaint, style = MaterialTheme.typography.bodyLarge, color = WarmCream, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                                Spacer(modifier = Modifier.height(14.dp))
                                Text("History of Present Illness", style = MaterialTheme.typography.titleSmall, color = MedicalTeal, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(clinicalCase.historyText, style = MaterialTheme.typography.bodyMedium, color = WarmCream, lineHeight = 22.sp)
                            }
                        }
                    }
                }
                1 -> {
                    // Physical Examination
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Physical Examination Findings", style = MaterialTheme.typography.titleSmall, color = MedicalTeal, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(clinicalCase.examFindings, style = MaterialTheme.typography.bodyMedium, color = WarmCream, lineHeight = 24.sp)
                            }
                        }
                    }
                }
                2 -> {
                    // Labs & Imaging
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Diagnostic Investigations & Results", style = MaterialTheme.typography.titleSmall, color = RewardGold, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(clinicalCase.labData, style = MaterialTheme.typography.bodyMedium, color = WarmCream, lineHeight = 24.sp)
                            }
                        }
                    }
                }
                3 -> {
                    // Diagnostic Synthesis & Differential
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Select Primary Working Diagnosis",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = RewardGold,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                differentials.forEach { diag ->
                                    val isSelected = selectedDiagnosis == diag
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MedicalTealContainer else NavyElevated
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) MedicalTeal else Color(0x33FFFFFF)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable(enabled = !isSubmitted) { selectedDiagnosis = diag }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { if (!isSubmitted) selectedDiagnosis = diag },
                                                colors = RadioButtonDefaults.colors(selectedColor = MedicalTeal)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(diag, color = WarmCream, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    "Clinical Reasoning Rationale",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MedicalTeal,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = userReasoningText,
                                    onValueChange = { userReasoningText = it },
                                    placeholder = { Text("Synthesize your pathophysiological rationale here (e.g. elevated JVP + S3 + NT-proBNP points to...)") },
                                    enabled = !isSubmitted,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MedicalTeal,
                                        unfocusedBorderColor = NavyElevated,
                                        focusedTextColor = WarmCream,
                                        unfocusedTextColor = WarmCream
                                    ),
                                    maxLines = 4
                                )
                            }
                        }
                    }

                    if (isSubmitted) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = NavyElevated),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Expert Case Debrief & Management Plan", style = MaterialTheme.typography.titleSmall, color = SuccessGreen, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Primary Diagnosis: ${clinicalCase.primaryDiagnosis}", fontWeight = FontWeight.Bold, color = WarmCream)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(clinicalCase.managementSummary, style = MaterialTheme.typography.bodyMedium, color = WarmCream, lineHeight = 22.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Button
        if (activeTab == 3) {
            if (!isSubmitted) {
                Button(
                    onClick = {
                        isSubmitted = true
                        onCaseCompleted()
                    },
                    enabled = selectedDiagnosis != null,
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("submit_case_button")
                ) {
                    Text("Submit Diagnostic Synthesis (+120 XP, +60 🪙)", color = MidnightNavy, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return to Arena", color = MidnightNavy, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Button(
                onClick = { activeTab = (activeTab + 1).coerceAtMost(3) },
                colors = ButtonDefaults.buttonColors(containerColor = NavyElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to Next Section", color = MedicalTeal, fontWeight = FontWeight.Bold)
            }
        }
    }
}
