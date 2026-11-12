package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

data class HubItem(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val badge: String,
    val actionKey: String
)

@Composable
fun LearnHubScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        HubItem(
            title = "Lecture-to-Exam Library",
            subtitle = "Import slides, extract core summaries, and generate active recall MCQs",
            iconEmoji = "📚",
            badge = "2 Documents",
            actionKey = "LECTURE_LIBRARY"
        ),
        HubItem(
            title = "Spaced Repetition Flashcards",
            subtitle = "SM-2 algorithm scheduled retrieval practice for long-term memory",
            iconEmoji = "🃏",
            badge = "4 Due",
            actionKey = "FLASHCARDS"
        ),
        HubItem(
            title = "PhysioChain Mechanism Lab",
            subtitle = "Assemble causal pathophysiological cascades and repair broken links",
            iconEmoji = "🧬",
            badge = "Interactive",
            actionKey = "PHYSIOCHAIN"
        ),
        HubItem(
            title = "Research Forge & EvidenceLens",
            subtitle = "Appraise clinical trials, calculate NNT, and build PICO research protocols",
            iconEmoji = "🔬",
            badge = "EBM Tools",
            actionKey = "RESEARCH"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Knowledge & Study Systems",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = WarmCream
            )
            Text(
                "Structured cognitive tools designed to bridge basic science and clinical understanding.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }

        items(items) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyElevated),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(item.actionKey) }
                    .testTag("learn_hub_${item.actionKey}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.iconEmoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WarmCream
                            )
                            Surface(
                                color = MedicalTealContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    item.badge,
                                    color = MedicalTeal,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCAD4DF),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
