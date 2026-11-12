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
import com.example.data.model.CosmeticItem
import com.example.data.model.UserProfile
import com.example.game.AcademyWorldView
import com.example.game.AvatarCustomizer
import com.example.game.CompanionCollectionView
import com.example.game.CosmeticStoreView
import com.example.ui.theme.*

@Composable
fun WorldHubScreen(
    userProfile: UserProfile?,
    inventory: List<CosmeticItem>,
    onEnterBuilding: (String) -> Unit,
    onProfileUpdated: (UserProfile) -> Unit,
    onSelectCompanion: (String) -> Unit,
    onPurchaseCosmetic: (CosmeticItem) -> Unit,
    onEquipCosmetic: (CosmeticItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Academy Campus", "Avatar Creator", "Companions", "Emporium")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        // Sub-tabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = DeepNavySurface,
            contentColor = MedicalTeal
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    text = {
                        Text(
                            title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("world_subtab_$index")
                )
            }
        }

        when (selectedSubTab) {
            0 -> {
                // Interactive 2D Pixel World
                AcademyWorldView(
                    userProfile = userProfile,
                    onEnterBuilding = onEnterBuilding,
                    modifier = Modifier.fillMaxSize()
                )
            }
            1 -> {
                // Avatar Creator
                if (userProfile != null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        item {
                            AvatarCustomizer(
                                userProfile = userProfile,
                                onProfileUpdated = onProfileUpdated
                            )
                        }
                    }
                }
            }
            2 -> {
                // Companions
                CompanionCollectionView(
                    userProfile = userProfile,
                    onSelectCompanion = onSelectCompanion,
                    modifier = Modifier.fillMaxSize()
                )
            }
            3 -> {
                // Cosmetic Emporium
                CosmeticStoreView(
                    items = inventory,
                    userProfile = userProfile,
                    onPurchase = onPurchaseCosmetic,
                    onEquip = onEquipCosmetic,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
