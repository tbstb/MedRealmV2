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

@Composable
fun PracticeHubScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        HubItem(
            title = "Clinical Case Arena",
            subtitle = "Simulated patient encounters with chief complaints, exam findings, and diagnostic reasoning",
            iconEmoji = "🏥",
            badge = "Mr. Sterling Case",
            actionKey = "CLINICAL_CASE"
        ),
        HubItem(
            title = "WardVoice & Speaking Coach",
            subtitle = "Rehearse concise SBAR handovers and oral exam defenses with structured rubric evaluation",
            iconEmoji = "🗣️",
            badge = "Voice & Text",
            actionKey = "SPEAKING"
        ),
        HubItem(
            title = "Diagnostic MCQ Arena",
            subtitle = "Multi-step vignettes, confidence calibration ratings, and high-yield clinical pearls",
            iconEmoji = "🎯",
            badge = "3 High-Yield Qs",
            actionKey = "MCQ"
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
                "Clinical & Simulation Arena",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = WarmCream
            )
            Text(
                "Hands-on practice stations to develop diagnostic confidence and ward communication skills.",
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
                    .testTag("practice_hub_${item.actionKey}")
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
                                color = RewardGoldContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    item.badge,
                                    color = RewardGold,
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
