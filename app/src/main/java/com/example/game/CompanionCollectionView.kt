package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun CompanionCollectionView(
    userProfile: UserProfile?,
    onSelectCompanion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedId = userProfile?.selectedCompanionId ?: "orion"

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "Academy Companions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = WarmCream
            )
            Text(
                "Loyal mythical guides that accompany your medical studies. Each specializes in a core discipline.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }

        items(GameData.companions) { comp ->
            val isSelected = comp.id == selectedId
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) DeepNavySurface else NavyElevated
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MedicalTeal else Color(0x33FFFFFF)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCompanion(comp.id) }
                    .testTag("companion_card_${comp.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MedicalTealContainer else DeepNavySurface)
                                .border(1.5.dp, if (isSelected) MedicalTeal else Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(comp.emoji, fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    comp.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MedicalTeal else WarmCream
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = MedicalTealContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            "Active",
                                            color = MedicalTeal,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(comp.title, style = MaterialTheme.typography.bodySmall, color = RewardGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(comp.bio, style = MaterialTheme.typography.bodySmall, color = WarmCream)

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MidnightNavy.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                            Text("💡", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = comp.studyHint,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCAD4DF)
                            )
                        }
                    }
                }
            }
        }
    }
}
