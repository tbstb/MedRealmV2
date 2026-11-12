package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class SciencePrinciple(
    val title: String,
    val authors: String,
    val citation: String,
    val whyUsed: String,
    val medRealmImplementation: String
)

@Composable
fun LearningScienceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val principles = listOf(
        SciencePrinciple(
            title = "1. Retrieval Practice & The Testing Effect",
            authors = "Roediger, H. L., & Karpicke, J. D.",
            citation = "Science, 2006; 311(5764): 1256–1258. doi:10.1126/science.1121808",
            whyUsed = "Actively generating an answer produces deeper long-term neural consolidation than passive rereading or highlighting.",
            medRealmImplementation = "MedRealm questions, flashcards, and PhysioChains require active generation rather than passive review."
        ),
        SciencePrinciple(
            title = "2. Distributed Practice & Spacing Effect",
            authors = "Cepeda, N. J., Vul, E., Rohrer, D., Wixted, J. T., & Pashler, H.",
            citation = "Journal of Experimental Psychology: Applied, 2008; 14(2): 109–118.",
            whyUsed = "Spacing study sessions across intervals significantly enhances retention over months and years compared to massed 'cramming'.",
            medRealmImplementation = "Modified SM-2 interval scheduler in the Spaced Retrieval Engine dynamically calculates card review intervals."
        ),
        SciencePrinciple(
            title = "3. Deliberate Practice & Simulation in Medical Education",
            authors = "Cook, D. A., Hatala, R., Brydges, R., et al.",
            citation = "JAMA, 2011; 306(9): 978–988. doi:10.1001/jama.2011.1232",
            whyUsed = "Structured simulation with informative feedback produces moderate-to-large effects across clinical knowledge, process skills, and patient outcomes.",
            medRealmImplementation = "Clinical Case Arena presents authentic multi-tab patient encounters with immediate debriefs and management summaries."
        ),
        SciencePrinciple(
            title = "4. Metacognitive Calibration & Self-Explanation",
            authors = "Dunlosky, J., Rawson, K. A., Marsh, E. J., Nathan, M. J., & Willingham, D. T.",
            citation = "Psychological Science in the Public Interest, 2013; 14(1): 4–58.",
            whyUsed = "Students who calibrate confidence before answering and generate causal self-explanations identify knowledge gaps far faster.",
            medRealmImplementation = "Confidence rating slider before MCQ reveals, and rationale synthesis boxes in Clinical Cases."
        ),
        SciencePrinciple(
            title = "5. Self-Determination Theory (Intrinsic Motivation)",
            authors = "Deci, E. L., & Ryan, R. M.",
            citation = "Contemporary Educational Psychology, 2000; 25(1): 54–67.",
            whyUsed = "Supporting learner autonomy, competence, and relatedness fosters resilient motivation without relying on shame-driven streaks.",
            medRealmImplementation = "Flexible study choices, cosmetic rewards rather than paywalled content, and gentle companion guidance without penalties for missed days."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_science")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "Learning Science Foundation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text("Peer-Reviewed Educational Psychology & Medical Evidence", style = MaterialTheme.typography.bodySmall, color = MedicalTeal)
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
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Evidence-Based Pedagogy Manifesto",
                            fontWeight = FontWeight.Bold,
                            color = RewardGold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "MedRealm is architected strictly upon empirical learning science principles. We believe gamification should serve cognitive consolidation—not addictive mechanics.",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmCream,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            items(principles) { p ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MedicalTeal.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(p.title, fontWeight = FontWeight.Bold, color = MedicalTeal, fontSize = 15.sp)
                        Text(p.authors, fontSize = 12.sp, color = RewardGold, fontWeight = FontWeight.Medium)
                        Text(p.citation, fontSize = 11.sp, color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Why it matters:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarmCream)
                        Text(p.whyUsed, fontSize = 12.sp, color = Color.LightGray, lineHeight = 18.sp)

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("MedRealm implementation:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTeal)
                        Text(p.medRealmImplementation, fontSize = 12.sp, color = WarmCream, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}
