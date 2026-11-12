package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.CosmeticItem
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun CosmeticStoreView(
    items: List<CosmeticItem>,
    userProfile: UserProfile?,
    onPurchase: (CosmeticItem) -> Unit,
    onEquip: (CosmeticItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("ALL") }

    val filteredItems = remember(items, selectedCategory) {
        if (selectedCategory == "ALL") items
        else items.filter { it.type == selectedCategory }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Balance Header
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Academy Emporium", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = WarmCream)
                    Text("Spend earned study coins on cosmetic gear", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
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
                            "${userProfile?.coins ?: 0} Coins",
                            color = RewardGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "OUTFIT", "ACCESSORY", "HEADWEAR").forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        // Item List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filteredItems) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DeepNavySurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(item.iconEmoji, fontSize = 26.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, color = WarmCream, fontSize = 15.sp)
                            Text(item.description, color = Color.LightGray, fontSize = 12.sp, maxLines = 2)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        if (item.isPurchased) {
                            if (item.isEquipped) {
                                FilledTonalButton(
                                    onClick = { },
                                    enabled = false,
                                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MedicalTealContainer)
                                ) {
                                    Text("Equipped", color = MedicalTeal, fontSize = 12.sp)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { onEquip(item) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MedicalTeal)
                                ) {
                                    Text("Equip", fontSize = 12.sp)
                                }
                            }
                        } else {
                            val canAfford = (userProfile?.coins ?: 0) >= item.cost
                            Button(
                                onClick = { onPurchase(item) },
                                enabled = canAfford,
                                colors = ButtonDefaults.buttonColors(containerColor = RewardGold, contentColor = MidnightNavy),
                                modifier = Modifier.testTag("buy_${item.id}")
                            ) {
                                Text("${item.cost} 🪙", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
