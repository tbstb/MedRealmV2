package com.example.game

data class CompanionInfo(
    val id: String,
    val name: String,
    val title: String,
    val specialty: String,
    val emoji: String,
    val bio: String,
    val studyHint: String,
    val unlocked: Boolean = true
)

object GameData {
    val companions = listOf(
        CompanionInfo(
            id = "orion",
            name = "Orion the Owl",
            title = "Companion of Knowledge & Understanding",
            specialty = "Spaced retrieval, lecture synthesis, and conceptual mastery",
            emoji = "🦉",
            bio = "Wise and calm, Orion perches in the Grand Library, turning vast medical syllabi into bite-sized retrieval questions.",
            studyHint = "Remember: retrieval practice beats passive rereading by strengthening synaptic recall pathways!"
        ),
        CompanionInfo(
            id = "ember",
            name = "Ember the Fox",
            title = "Companion of Clinical Reasoning",
            specialty = "Diagnostic thinking, bias avoidance, and emergency prioritization",
            emoji = "🦊",
            bio = "Alert and sharp-eyed, Ember roams the Clinical Hospital, questioning assumptions and sniffing out premature cognitive closure.",
            studyHint = "Beware of anchoring bias! Always test your initial impression against alternative differential diagnoses."
        ),
        CompanionInfo(
            id = "nova",
            name = "Nova the Dragon",
            title = "Companion of Speaking & Confidence",
            specialty = "Ward presentations, oral examinations, and structured OSCE communication",
            emoji = "🐲",
            bio = "Warm and encouraging, Nova frequents the Speaking Hall, helping students overcome hesitation during ward rounds.",
            studyHint = "Structure is your shield in ward rounds: Situation, Background, Assessment, Recommendation (SBAR) never fails."
        ),
        CompanionInfo(
            id = "atlas",
            name = "Atlas the Golem-Bot",
            title = "Companion of Evidence & Research",
            specialty = "Trial appraisal, statistical significance, and PICO formulation",
            emoji = "🤖",
            bio = "Constructed with precision gears in the Research Tower, Atlas calculates odds ratios and demystifies confidence intervals.",
            studyHint = "P-values are not effect sizes! Always examine confidence intervals and clinical relevance before adopting a new trial."
        )
    )

    val hairStyles = listOf("Academic Short", "Wizard Bob", "Pixie Mage", "Knight Waves")
    val hairColors = listOf(
        Pair("Espresso", 0xFF2B1D0C),
        Pair("Auburn", 0xFF7A2B12),
        Pair("Golden", 0xFFD4AF37),
        Pair("Silver Gray", 0xFF9E9E9E),
        Pair("Teal Mist", 0xFF1EA393)
    )
    val skinTones = listOf(
        Pair("Porcelain", 0xFFFFE0BD),
        Pair("Warm Sand", 0xFFF1C27D),
        Pair("Golden Tan", 0xFFE0AC69),
        Pair("Deep Amber", 0xFFC68642),
        Pair("Rich Chestnut", 0xFF8D5524)
    )
    val outfits = listOf("Apprentice White Coat", "Royal Violet Robe", "Field Medic Vest", "Gold Scholar Tunic")
    val accessories = listOf("Classic Stethoscope", "Sage Glasses", "Dragon Brooch", "Research Magnifier")
    val headwears = listOf("None", "Scholar Mortarboard", "Golden Healer Laurels", "Mystic Hood")
}
