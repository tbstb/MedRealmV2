package com.example.learning

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class ChainNode(
    val id: Int,
    val text: String,
    val stageLabel: String
)

@Composable
fun PhysioChainScreen(
    onCompleteChain: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Challenge: Order the cascade of acute decompensated heart failure from trigger to clinical sign
    val correctOrder = remember {
        listOf(
            ChainNode(1, "Primary Myocardial Insult (e.g. Ischemia / Infarction)", "Trigger"),
            ChainNode(2, "Decreased Left Ventricular Stroke Volume & Ejection Fraction", "Hemodynamic Deficit"),
            ChainNode(3, "Neurohormonal Activation (Sympathetic Outflow & RAAS Activation)", "Compensatory Response"),
            ChainNode(4, "Systemic Vasoconstriction & Renal Sodium/Water Retention", "Afterload/Volume Load"),
            ChainNode(5, "Elevated Left Ventricular End-Diastolic Pressure & Pulmonary Capillary Wedge Pressure", "Hydrostatic Congestion"),
            ChainNode(6, "Alveolar Transudation, Pulmonary Edema & Bibasilar Inspiratory Crackles", "Clinical Manifestation")
        )
    }

    // Shuffled initial bank
    var availableNodes by remember {
        mutableStateOf(correctOrder.shuffled())
    }
    var currentPlacedNodes by remember {
        mutableStateOf<List<ChainNode>>(emptyList())
    }
    var isSubmitted by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_physio")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WarmCream)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "PhysioChain Causal Puzzle",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = WarmCream
                )
                Text(
                    "Construct the Stepwise Cascade",
                    style = MaterialTheme.typography.bodySmall,
                    color = MedicalTeal
                )
            }
            IconButton(
                onClick = {
                    currentPlacedNodes = emptyList()
                    availableNodes = correctOrder.shuffled()
                    isSubmitted = false
                    isSuccess = false
                }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = RewardGold)
            }
        }

        // Prompt Info
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "Causal Cascade: Acute Decompensated Heart Failure",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = RewardGold
                )
                Text(
                    "Select steps in chronological order from root cellular insult down to bed-side physical examination signs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }

        // Placed Chain Area
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepNavySurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isSubmitted && isSuccess) SuccessGreen else if (isSubmitted) ErrorRed else MedicalTeal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 12.dp)
        ) {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize().padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (currentPlacedNodes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Tap an available step below to add it to your causal chain.",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    items(currentPlacedNodes.size) { idx ->
                        val node = currentPlacedNodes[idx]
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NavyElevated),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isSubmitted) {
                                    // Remove from placed and return to available
                                    currentPlacedNodes = currentPlacedNodes.toMutableList().also { it.removeAt(idx) }
                                    availableNodes = availableNodes + node
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MedicalTealContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${idx + 1}",
                                        color = MedicalTeal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        node.stageLabel.uppercase(),
                                        fontSize = 10.sp,
                                        color = RewardGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        node.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = WarmCream,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                if (!isSubmitted) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        if (idx < currentPlacedNodes.size - 1) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("↓", color = MedicalTeal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Available Pool
        if (availableNodes.isNotEmpty()) {
            Text(
                "Available Steps (Tap to Place):",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = WarmCream,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(availableNodes.size) { idx ->
                    val node = availableNodes[idx]
                    Surface(
                        color = NavyElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                availableNodes = availableNodes.toMutableList().also { it.removeAt(idx) }
                                currentPlacedNodes = currentPlacedNodes + node
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("➕", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(node.text, style = MaterialTheme.typography.bodySmall, color = WarmCream)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Submission & Feedback
        if (!isSubmitted) {
            Button(
                onClick = {
                    isSubmitted = true
                    val matches = currentPlacedNodes.map { it.id } == correctOrder.map { it.id }
                    isSuccess = matches
                    if (matches) {
                        onCompleteChain()
                    }
                },
                enabled = currentPlacedNodes.size == correctOrder.size,
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("submit_physio_button")
            ) {
                Text("Validate Mechanism (+60 XP, +35 🪙)", color = MidnightNavy, fontWeight = FontWeight.Bold)
            }
        } else {
            if (isSuccess) {
                Surface(
                    color = Color(0xFF1B5E20),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("✨ Perfect Causal Cascade!", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "You demonstrated sound mastery of backward failure physiology: preload elevation leads to hydrostatic capillary pressure exceeding oncotic pressure, producing alveolar transudation.",
                            fontSize = 12.sp,
                            color = Color(0xFFE8F5E9)
                        )
                    }
                }
            } else {
                Surface(
                    color = Color(0xFFB71C1C),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("⚠️ Cascade Order Discrepancy", fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "One or more nodes are out of sequence. Consider how the initial hemodynamic deficit triggers compensatory neurohormones prior to venous congestion.",
                            fontSize = 12.sp,
                            color = Color(0xFFFFEBEE)
                        )
                    }
                }
            }
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return to Lab", color = MidnightNavy, fontWeight = FontWeight.Bold)
            }
        }
    }
}
