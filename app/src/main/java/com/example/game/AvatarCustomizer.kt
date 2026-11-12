package com.example.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun AvatarCustomizer(
    userProfile: UserProfile,
    onProfileUpdated: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var characterName by remember(userProfile) { mutableStateOf(userProfile.characterName) }
    var selectedHairStyle by remember(userProfile) { mutableStateOf(userProfile.hairStyle) }
    var selectedHairColor by remember(userProfile) { mutableLongStateOf(userProfile.hairColor) }
    var selectedSkinTone by remember(userProfile) { mutableLongStateOf(userProfile.skinTone) }
    var selectedOutfit by remember(userProfile) { mutableStateOf(userProfile.outfit) }
    var selectedAccessory by remember(userProfile) { mutableStateOf(userProfile.accessory) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Pixel-art Avatar Preview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pixel Avatar Preview
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NavyElevated)
                        .border(2.dp, MedicalTeal, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        val w = size.width
                        val h = size.height

                        // Shadow
                        drawOval(
                            color = Color(0x55000000),
                            topLeft = Offset(w * 0.2f, h * 0.85f),
                            size = Size(w * 0.6f, h * 0.12f)
                        )

                        // Body / Coat
                        val outfitColor = when (selectedOutfit) {
                            "Royal Violet Robe" -> Color(0xFF673AB7)
                            "Field Medic Vest" -> Color(0xFF388E3C)
                            "Gold Scholar Tunic" -> Color(0xFFFFB300)
                            else -> Color(0xFFECEFF1) // White Coat
                        }
                        drawRoundRect(
                            color = outfitColor,
                            topLeft = Offset(w * 0.25f, h * 0.45f),
                            size = Size(w * 0.5f, h * 0.42f),
                            cornerRadius = CornerRadius(8f)
                        )

                        // Stethoscope / Accessory
                        if (selectedAccessory == "Classic Stethoscope") {
                            drawCircle(
                                color = MedicalTeal,
                                radius = w * 0.08f,
                                center = Offset(w * 0.5f, h * 0.62f)
                            )
                        } else if (selectedAccessory == "Sage Glasses") {
                            drawRect(
                                color = RewardGold,
                                topLeft = Offset(w * 0.35f, h * 0.35f),
                                size = Size(w * 0.3f, h * 0.06f)
                            )
                        }

                        // Head
                        val skin = Color(selectedSkinTone)
                        drawCircle(
                            color = skin,
                            radius = w * 0.22f,
                            center = Offset(w * 0.5f, h * 0.32f)
                        )

                        // Hair
                        val hair = Color(selectedHairColor)
                        drawArc(
                            color = hair,
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = true,
                            topLeft = Offset(w * 0.24f, h * 0.12f),
                            size = Size(w * 0.52f, h * 0.35f)
                        )

                        // Face eyes
                        drawCircle(color = MidnightNavy, radius = 3f, center = Offset(w * 0.42f, h * 0.32f))
                        drawCircle(color = MidnightNavy, radius = 3f, center = Offset(w * 0.58f, h * 0.32f))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = characterName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    text = "Level ${userProfile.level} • ${userProfile.academicYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = RewardGold
                )
            }
        }

        // Customization Selectors
        OutlinedTextField(
            value = characterName,
            onValueChange = {
                characterName = it
                onProfileUpdated(userProfile.copy(characterName = it))
            },
            label = { Text("Character Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MedicalTeal,
                unfocusedBorderColor = NavyElevated,
                focusedTextColor = WarmCream,
                unfocusedTextColor = WarmCream
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        // Hairstyle Selection
        Text("Hairstyle", color = MedicalTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        LazyRow(modifier = Modifier.padding(vertical = 6.dp)) {
            items(GameData.hairStyles) { style ->
                FilterChip(
                    selected = selectedHairStyle == style,
                    onClick = {
                        selectedHairStyle = style
                        onProfileUpdated(userProfile.copy(hairStyle = style))
                    },
                    label = { Text(style) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        // Hair Color Palette
        Text("Hair Color", color = MedicalTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        LazyRow(modifier = Modifier.padding(vertical = 6.dp)) {
            items(GameData.hairColors) { (name, colorLong) ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(colorLong))
                        .border(
                            width = if (selectedHairColor == colorLong) 2.5.dp else 0.dp,
                            color = if (selectedHairColor == colorLong) RewardGold else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            selectedHairColor = colorLong
                            onProfileUpdated(userProfile.copy(hairColor = colorLong))
                        }
                )
            }
        }

        // Skin Tone Palette
        Text("Skin Tone", color = MedicalTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        LazyRow(modifier = Modifier.padding(vertical = 6.dp)) {
            items(GameData.skinTones) { (name, toneLong) ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(toneLong))
                        .border(
                            width = if (selectedSkinTone == toneLong) 2.5.dp else 0.dp,
                            color = if (selectedSkinTone == toneLong) RewardGold else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            selectedSkinTone = toneLong
                            onProfileUpdated(userProfile.copy(skinTone = toneLong))
                        }
                )
            }
        }

        // Outfit Selection
        Text("Outfit", color = MedicalTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        LazyRow(modifier = Modifier.padding(vertical = 6.dp)) {
            items(GameData.outfits) { outfit ->
                FilterChip(
                    selected = selectedOutfit == outfit,
                    onClick = {
                        selectedOutfit = outfit
                        onProfileUpdated(userProfile.copy(outfit = outfit))
                    },
                    label = { Text(outfit) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        // Accessory Selection
        Text("Accessory", color = MedicalTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        LazyRow(modifier = Modifier.padding(vertical = 6.dp)) {
            items(GameData.accessories) { acc ->
                FilterChip(
                    selected = selectedAccessory == acc,
                    onClick = {
                        selectedAccessory = acc
                        onProfileUpdated(userProfile.copy(accessory = acc))
                    },
                    label = { Text(acc) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
