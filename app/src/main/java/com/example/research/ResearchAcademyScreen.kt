package com.example.research

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@Composable
fun ResearchAcademyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var picoPopulation by remember { mutableStateOf("Adult patients with chronic HFrEF (LVEF ≤ 40%)") }
    var picoIntervention by remember { mutableStateOf("SGLT2 Inhibitor (Empagliflozin 10 mg daily)") }
    var picoComparison by remember { mutableStateOf("Standard GDMT without SGLT2i (Placebo)") }
    var picoOutcome by remember { mutableStateOf("Composite of cardiovascular death or hospitalization for heart failure") }
    var isPicoEvaluated by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_research")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Research Forge & EvidenceLens",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    "Evidence-Based Medicine & PICO Formulation",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            Text("🔬", fontSize = 24.sp)
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // EBM Science Notice
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🤖", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Atlas the Golem: Evidence Appraisal Guide",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = RewardGold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Rigorous clinical practice relies on formulating structured PICO questions (Population, Intervention, Comparison, Outcome) and critically appraising relative risk reductions and number needed to treat (NNT).",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmCream,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // PICO Builder
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Construct Your Clinical Research Question", style = MaterialTheme.typography.titleSmall, color = MedicalTeal, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = picoPopulation,
                            onValueChange = { picoPopulation = it },
                            label = { Text("P - Population / Patient Problem") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTeal,
                                unfocusedBorderColor = NavyElevated,
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream
                            )
                        )

                        OutlinedTextField(
                            value = picoIntervention,
                            onValueChange = { picoIntervention = it },
                            label = { Text("I - Intervention / Prognostic Factor") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTeal,
                                unfocusedBorderColor = NavyElevated,
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream
                            )
                        )

                        OutlinedTextField(
                            value = picoComparison,
                            onValueChange = { picoComparison = it },
                            label = { Text("C - Comparison / Control") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTeal,
                                unfocusedBorderColor = NavyElevated,
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream
                            )
                        )

                        OutlinedTextField(
                            value = picoOutcome,
                            onValueChange = { picoOutcome = it },
                            label = { Text("O - Outcome of Clinical Interest") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTeal,
                                unfocusedBorderColor = NavyElevated,
                                focusedTextColor = WarmCream,
                                unfocusedTextColor = WarmCream
                            )
                        )

                        Button(
                            onClick = { isPicoEvaluated = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                            modifier = Modifier.fillMaxWidth().testTag("appraise_pico_button")
                        ) {
                            Text("Appraise Trial Evidence & Forest Plot", color = MidnightNavy, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Real Evidence Analysis (EMPEROR-Reduced Trial Analysis)
            if (isPicoEvaluated) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyElevated),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RewardGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Empirical Evidence Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = RewardGold)
                            Text("Benchmark: EMPEROR-Reduced Trial (Packer et al., N Engl J Med 2020; 383:1413-1424)", fontSize = 11.sp, color = Color.LightGray)

                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(color = DeepNavySurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Key Statistical Metrics:", fontWeight = FontWeight.Bold, color = MedicalTeal, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("• Hazard Ratio (HR): 0.75 (95% CI: 0.65 to 0.86; P < 0.001)", color = WarmCream, fontSize = 13.sp)
                                    Text("• Primary Outcome: 25% relative risk reduction in CV death or HF hospitalization", color = WarmCream, fontSize = 13.sp)
                                    Text("• Number Needed to Treat (NNT): ~19 patients treated over 16 months to prevent 1 primary event", color = WarmCream, fontSize = 13.sp)
                                    Text("• Benefit observed regardless of baseline type 2 diabetes presence or absence", color = WarmCream, fontSize = 13.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Evidence Quality Grade: High (Double-blind, randomized, placebo-controlled trial with multi-center enrollment and low loss to follow-up).",
                                style = MaterialTheme.typography.bodySmall,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }
        }
    }
}
