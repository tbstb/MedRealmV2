package com.example.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*

data class AcademyBuilding(
    val id: String,
    val name: String,
    val destinationLabel: String,
    val tileX: Int,
    val tileY: Int,
    val widthTiles: Int,
    val heightTiles: Int,
    val roofColor: Color,
    val doorX: Int,
    val doorY: Int,
    val iconEmoji: String,
    val description: String
)

data class AcademyNpc(
    val id: String,
    val name: String,
    val tileX: Int,
    val tileY: Int,
    val emoji: String,
    val dialogue: String
)

@Composable
fun AcademyWorldView(
    userProfile: UserProfile?,
    onEnterBuilding: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 2D Grid: 20 columns by 20 rows
    val gridWidth = 20
    val gridHeight = 20

    var playerX by remember { mutableFloatStateOf(10f) }
    var playerY by remember { mutableFloatStateOf(12f) }
    var activeDialogue by remember { mutableStateOf<String?>(null) }
    var activeBuildingAlert by remember { mutableStateOf<AcademyBuilding?>(null) }

    val buildings = remember {
        listOf(
            AcademyBuilding(
                id = "LIBRARY",
                name = "Grand Library & Archives",
                destinationLabel = "Lecture Library & Flashcards",
                tileX = 2,
                tileY = 2,
                widthTiles = 4,
                heightTiles = 4,
                roofColor = Color(0xFF3E2723),
                doorX = 3,
                doorY = 6,
                iconEmoji = "📚",
                description = "Houses the Spaced Repetition Archives and Lecture Summarizer."
            ),
            AcademyBuilding(
                id = "HOSPITAL",
                name = "Teaching Hospital",
                destinationLabel = "Clinical Cases & Bias Breaker",
                tileX = 14,
                tileY = 2,
                widthTiles = 4,
                heightTiles = 4,
                roofColor = Color(0xFF004D40),
                doorX = 15,
                doorY = 6,
                iconEmoji = "🏥",
                description = "Simulation wards for diagnostic calibration and patient journeys."
            ),
            AcademyBuilding(
                id = "SPEAKING_HALL",
                name = "Hall of Eloquence",
                destinationLabel = "WardVoice & Speaking Coach",
                tileX = 2,
                tileY = 13,
                widthTiles = 4,
                heightTiles = 4,
                roofColor = Color(0xFF4A148C),
                doorX = 4,
                doorY = 13,
                iconEmoji = "🏛️",
                description = "Where students practice oral exams and structured SBAR handovers."
            ),
            AcademyBuilding(
                id = "RESEARCH_TOWER",
                name = "Tower of Inquiry",
                destinationLabel = "Research Forge & EvidenceLens",
                tileX = 14,
                tileY = 13,
                widthTiles = 4,
                heightTiles = 4,
                roofColor = Color(0xFF0D47A1),
                doorX = 15,
                doorY = 13,
                iconEmoji = "🔭",
                description = "Appraise clinical trials, calculate odds ratios, and forge PICO protocols."
            )
        )
    }

    val npcs = remember {
        listOf(
            AcademyNpc(
                id = "archmage",
                name = "Headmaster Osler",
                tileX = 10,
                tileY = 8,
                emoji = "🧙‍♂️",
                dialogue = "Welcome, apprentice! 'He who studies medicine without books sails an uncharted sea, but he who studies medicine without patients does not go to sea at all.'"
            ),
            AcademyNpc(
                id = "curator",
                name = "Archivist Elena",
                tileX = 6,
                tileY = 4,
                emoji = "👩‍🏫",
                dialogue = "Check your flashcards daily. The forgetting curve is steep, but spaced retrieval makes memory permanent!"
            ),
            AcademyNpc(
                id = "resident",
                name = "Chief Resident Tariq",
                tileX = 13,
                tileY = 5,
                emoji = "👨‍⚕️",
                dialogue = "When you present on rounds, always lead with the patient's acuity and primary problem. Be concise and confident!"
            )
        )
    }

    fun isWalkable(x: Float, y: Float): Boolean {
        if (x < 1 || x > gridWidth - 2 || y < 1 || y > gridHeight - 2) return false
        // Check building collisions (solid walls, except doors)
        for (b in buildings) {
            if (x >= b.tileX && x < (b.tileX + b.widthTiles) &&
                y >= b.tileY && y < (b.tileY + b.heightTiles)
            ) {
                return false
            }
        }
        return true
    }

    fun movePlayer(dx: Float, dy: Float) {
        val newX = playerX + dx
        val newY = playerY + dy
        if (isWalkable(newX, newY)) {
            playerX = newX
            playerY = newY

            // Check if stepped near a building door
            for (b in buildings) {
                val dist = kotlin.math.hypot((newX - b.doorX).toDouble(), (newY - b.doorY).toDouble())
                if (dist <= 2.2) {
                    activeBuildingAlert = b
                    return
                }
            }
            activeBuildingAlert = null

            // Check NPC proximity
            for (npc in npcs) {
                val dist = kotlin.math.hypot((newX - npc.tileX).toDouble(), (newY - npc.tileY).toDouble())
                if (dist < 1.4) {
                    activeDialogue = "${npc.name}: \"${npc.dialogue}\""
                    return
                }
            }
            activeDialogue = null
        }
    }

    Column(modifier = modifier.fillMaxSize().background(MidnightNavy)) {
        // Academy Header bar
        Surface(
            color = DeepNavySurface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏰", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "MedRealm Academy Campus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WarmCream
                        )
                        Text(
                            text = "Coordinates: (${playerX.toInt()}, ${playerY.toInt()}) • 16-Bit Living World",
                            style = MaterialTheme.typography.bodySmall,
                            color = MedicalTeal
                        )
                    }
                }
                Surface(
                    color = RewardGoldContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile?.coins ?: 0}",
                            color = RewardGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Interactive 2D Pixel Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E3A2B)) // Lush emerald grass
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Allow tapping anywhere on map to navigate player toward that direction
                        val tileW = size.width / gridWidth
                        val tileH = size.height / gridHeight
                        val targetX = offset.x / tileW
                        val targetY = offset.y / tileH
                        val dx = (targetX - playerX).coerceIn(-1f, 1f)
                        val dy = (targetY - playerY).coerceIn(-1f, 1f)
                        movePlayer(dx, dy)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val tileW = size.width / gridWidth
                val tileH = size.height / gridHeight

                // Draw Cobblestone Paths
                drawCobblestonePaths(tileW, tileH)

                // Draw Central Academy Fountain
                drawCentralFountain(10f * tileW, 10f * tileH, tileW * 1.5f)

                // Draw Buildings
                for (b in buildings) {
                    val left = b.tileX * tileW
                    val top = b.tileY * tileH
                    val width = b.widthTiles * tileW
                    val height = b.heightTiles * tileH

                    // Building Base Wall
                    drawRoundRect(
                        color = Color(0xFFD7CCC8),
                        topLeft = Offset(left, top),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                    // Building Roof
                    drawRoundRect(
                        color = b.roofColor,
                        topLeft = Offset(left - 2, top - 2),
                        size = Size(width + 4, height * 0.45f),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                    // Door
                    val doorLeft = b.doorX * tileW
                    val doorTop = (b.tileY + b.heightTiles - 1) * tileH
                    drawRoundRect(
                        color = Color(0xFF5D4037),
                        topLeft = Offset(doorLeft, doorTop),
                        size = Size(tileW, tileH),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                    // Door threshold indicator
                    drawCircle(
                        color = RewardGold,
                        radius = tileW * 0.2f,
                        center = Offset(doorLeft + tileW * 0.5f, doorTop + tileH * 0.5f)
                    )
                }

                // Draw NPCs
                for (npc in npcs) {
                    val cx = (npc.tileX + 0.5f) * tileW
                    val cy = (npc.tileY + 0.5f) * tileH
                    drawCircle(
                        color = Color(0xFF8E24AA),
                        radius = tileW * 0.4f,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        color = Color(0xFFFFD54F),
                        radius = tileW * 0.25f,
                        center = Offset(cx, cy - tileH * 0.2f)
                    )
                }

                // Draw Player Sprite (16-bit style layered character)
                val px = (playerX + 0.5f) * tileW
                val py = (playerY + 0.5f) * tileH

                // Player Shadow
                drawOval(
                    color = Color(0x66000000),
                    topLeft = Offset(px - tileW * 0.4f, py + tileH * 0.2f),
                    size = Size(tileW * 0.8f, tileH * 0.35f)
                )
                // Player Body (Coat)
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(px - tileW * 0.3f, py - tileH * 0.2f),
                    size = Size(tileW * 0.6f, tileH * 0.6f),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                // Stethoscope accent
                drawCircle(
                    color = MedicalTeal,
                    radius = tileW * 0.12f,
                    center = Offset(px, py)
                )
                // Player Head (Skin tone)
                val skinColor = Color(userProfile?.skinTone ?: 0xFFF5D0A9)
                drawCircle(
                    color = skinColor,
                    radius = tileW * 0.3f,
                    center = Offset(px, py - tileH * 0.45f)
                )
                // Player Hair
                val hairColor = Color(userProfile?.hairColor ?: 0xFF4A3728)
                drawArc(
                    color = hairColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(px - tileW * 0.32f, py - tileH * 0.75f),
                    size = Size(tileW * 0.64f, tileH * 0.5f)
                )
                // Companion following player
                val compX = px + tileW * 0.6f
                val compY = py - tileH * 0.2f
                drawCircle(
                    color = RewardGold,
                    radius = tileW * 0.22f,
                    center = Offset(compX, compY)
                )
            }

            // Notification / Alert Overlay
            if (activeBuildingAlert != null) {
                val b = activeBuildingAlert!!
                Surface(
                    color = MidnightNavy.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalTeal),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .testTag("enter_building_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${b.iconEmoji} ${b.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RewardGold
                        )
                        Text(
                            text = b.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmCream,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { onEnterBuilding(b.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                            modifier = Modifier.testTag("enter_building_button")
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null, tint = MidnightNavy)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enter ${b.destinationLabel}", color = MidnightNavy, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else if (activeDialogue != null) {
                Surface(
                    color = MidnightNavy.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RewardGold),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = activeDialogue!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmCream,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // On-screen D-Pad Touch Controls
        Surface(
            color = DeepNavySurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quick info about companion
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🦉", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Orion is hovering nearby",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = RewardGold
                        )
                        Text(
                            text = "Tap arrows or tap map to explore",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray
                        )
                    }
                }

                // D-Pad
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { movePlayer(0f, -1f) },
                        modifier = Modifier
                            .size(44.dp)
                            .background(NavyElevated, CircleShape)
                            .testTag("dpad_up")
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move Up", tint = MedicalTeal)
                    }
                    Row {
                        IconButton(
                            onClick = { movePlayer(-1f, 0f) },
                            modifier = Modifier
                                .size(44.dp)
                                .background(NavyElevated, CircleShape)
                                .testTag("dpad_left")
                        ) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Move Left", tint = MedicalTeal)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { movePlayer(1f, 0f) },
                            modifier = Modifier
                                .size(44.dp)
                                .background(NavyElevated, CircleShape)
                                .testTag("dpad_right")
                        ) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Move Right", tint = MedicalTeal)
                        }
                    }
                    IconButton(
                        onClick = { movePlayer(0f, 1f) },
                        modifier = Modifier
                            .size(44.dp)
                            .background(NavyElevated, CircleShape)
                            .testTag("dpad_down")
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move Down", tint = MedicalTeal)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawCobblestonePaths(tileW: Float, tileH: Float) {
    val pathColor = Color(0xFFC4B89B)
    // Horizontal central avenue
    drawRect(
        color = pathColor,
        topLeft = Offset(0f, 9.5f * tileH),
        size = Size(size.width, tileH * 2f)
    )
    // Vertical central boulevard
    drawRect(
        color = pathColor,
        topLeft = Offset(9.5f * tileW, 0f),
        size = Size(tileW * 2f, size.height)
    )
}

private fun DrawScope.drawCentralFountain(cx: Float, cy: Float, radius: Float) {
    // Outer stone basin
    drawCircle(color = Color(0xFF9E9E9E), radius = radius, center = Offset(cx, cy))
    // Inner water
    drawCircle(color = Color(0xFF29B6F6), radius = radius * 0.8f, center = Offset(cx, cy))
    // Center fountain spout
    drawCircle(color = Color.White, radius = radius * 0.25f, center = Offset(cx, cy))
}
