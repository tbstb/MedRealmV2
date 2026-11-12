package com.example.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LectureDocument
import com.example.ui.theme.*

@Composable
fun LectureLibraryScreen(
    documents: List<LectureDocument>,
    onAddSampleDocument: (String, String, Int, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showImportDialog by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_lectures")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Lecture & Document Library",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    "Smart Lecture-to-Exam AI Engine",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            IconButton(onClick = { showImportDialog = true }) {
                Icon(Icons.Default.CloudUpload, contentDescription = "Upload Document", tint = RewardGold)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("📄", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Import Syllabi & Lecture Slides", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = RewardGold)
                            Text("Extract key concepts, generate active recall flashcards, and create board-style questions automatically.", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                        }
                    }
                }
            }

            items(documents) { doc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyElevated),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                doc.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = WarmCream,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = MedicalTealContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    "${doc.pageCount} Pages",
                                    color = MedicalTeal,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = doc.extractedSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCAD4DF),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            var generatedNotice by remember { mutableStateOf<String?>(null) }
                            
                            FilledTonalButton(
                                onClick = {
                                    onAddSampleDocument(
                                        "${doc.subject}: Auto-Extracted Active Recall Set",
                                        doc.subject,
                                        5,
                                        "Extracted high-yield retrieval points for ${doc.title}"
                                    )
                                    generatedNotice = "Generated 5 flashcards added to study deck!"
                                },
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = DeepNavySurface)
                            ) {
                                Text("Generate 5 Cards", fontSize = 11.sp, color = MedicalTeal)
                            }
                            FilledTonalButton(
                                onClick = {
                                    generatedNotice = "Quiz generated & added to Clinical MCQ bank!"
                                },
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = DeepNavySurface)
                            ) {
                                Text("Generate Quiz", fontSize = 11.sp, color = RewardGold)
                            }
                            
                            if (generatedNotice != null) {
                                Text(
                                    generatedNotice!!,
                                    color = SuccessGreen,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { showImportDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("import_lecture_button")
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = MidnightNavy)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Import New Lecture Document", color = MidnightNavy, fontWeight = FontWeight.Bold)
        }
    }

    if (showImportDialog) {
        var docTitle by remember { mutableStateOf("") }
        var docSubject by remember { mutableStateOf("Cardiology") }
        var docPages by remember { mutableStateOf("25") }
        var docSummary by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Medical Document", color = WarmCream) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = docTitle,
                        onValueChange = { docTitle = it },
                        label = { Text("Document / Lecture Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = docSubject,
                        onValueChange = { docSubject = it },
                        label = { Text("Medical Specialty") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = docSummary,
                        onValueChange = { docSummary = it },
                        label = { Text("Key Topics / Summary") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (docTitle.isNotBlank()) {
                            onAddSampleDocument(
                                docTitle,
                                docSubject,
                                docPages.toIntOrNull() ?: 20,
                                if (docSummary.isNotBlank()) docSummary else "Extracted syllabus covering clinical mechanisms, diagnostics, and management principles."
                            )
                            showImportDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal)
                ) {
                    Text("Import", color = MidnightNavy)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = DeepNavySurface
        )
    }
}
