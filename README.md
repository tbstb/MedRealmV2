# MedRealm: "The Medical Learning Adventure"

MedRealm combines evidence-based medical education, personalized AI mentoring, clinical simulation, and a cozy 16-bit RPG fantasy academy. Every time students improve their real-world medical knowledge and clinical skills, they also develop their characters, companions, and fantasy academy.

---

## 🌟 The Five Integrated Learning Systems

1. **Smart Study Engine**
   - **Lecture Library**: Import medical document syllabi, view extracted clinical summaries, and generate board-style active recall questions.
   - **Spaced Retrieval (SM-2 Algorithm)**: Dynamic spaced-repetition scheduler (`Again`, `Hard`, `Good`, `Easy`) preserving long-term retention.
   - **Smart Daily Study Planner**: Realistic study blocks that reward completion and never penalize rest days.

2. **Knowledge Lab**
   - **PhysioChain Mechanism Lab**: Construct causal biological pathways (e.g. myocardial insult → stroke volume decrease → neurohormonal activation → preload elevation → bibasilar crackles).
   - **Medical Knowledge Concept Maps**: Multi-tier pathophysiology connections.

3. **Clinical Arena**
   - **Authentic Clinical Cases**: Interactive multi-tab scenarios (Chief Complaint & HPI, Physical Exam, Labs & Imaging, Diagnostic Synthesis).
   - **Confidence Calibration**: Metacognitive slider (0–100%) to align confidence with actual diagnostic accuracy.
   - **Clinical Pearls & Debriefs**: Comprehensive management outlines for every patient encounter.

4. **Speaking & OSCE Studio**
   - **WardVoice**: Practice structured 60-second SBAR presentations (Situation, Background, Assessment, Recommendation).
   - **Speaking Coach**: Voice or text input evaluated against objective examiner rubrics (Organization, Medical Accuracy, Clarity).

5. **Research Academy**
   - **EvidenceLens & ResearchForge**: Formulate PICO clinical questions (Population, Intervention, Comparison, Outcome) and critically appraise landmark trials (e.g., EMPEROR-Reduced SGLT2i trial) with hazard ratios and numbers needed to treat (NNT).

---

## 🎮 The 16-Bit RPG World: MedRealm Academy

- **2D Explorable Academy Campus**: Interactive top-down world with cobblestone boulevards, a central fountain, and buildings:
  - 📚 **Grand Library & Archives**: Lecture Library & Spaced Flashcards
  - 🏥 **Teaching Hospital**: Clinical Simulation Cases
  - 🏛️ **Hall of Eloquence**: Speaking Coach & SBAR Wards
  - 🔭 **Tower of Inquiry**: Research Forge & Trial Appraisal
- **Collision Detection & D-Pad Controls**: Fluid navigation with collision boundaries preventing wall walking.
- **Interactive NPCs**: Headmaster Osler, Archivist Elena, and Chief Resident Tariq.
- **Customizable Avatar**: Hair styles, hair colors, skin tones, coats, stethoscopes, and accessories saved persistently.
- **Four Companions**:
  - 🦉 **Orion the Owl**: Knowledge & Spaced Retrieval
  - 🦊 **Ember the Fox**: Clinical Reasoning & Bias Avoidance
  - 🐲 **Nova the Dragon**: Speaking & Ward Confidence
  - 🤖 **Atlas the Golem-Bot**: Research & Evidence Appraisal
- **Cosmetic Emporium**: Spend earned coins on robes, vests, spectacles, and scholar mortarboards.

---

## 🧬 Scientific Foundations

MedRealm is designed on peer-reviewed educational psychology and medical literature:
- **Retrieval Practice**: Roediger & Karpicke (*Science*, 2006)
- **Distributed Spacing**: Cepeda et al. (*J Exp Psychol Appl*, 2008)
- **Deliberate Simulation**: Cook et al. (*JAMA*, 2011)
- **Metacognitive Self-Explanation**: Dunlosky et al. (*Psychol Sci Public Interest*, 2013)
- **Intrinsic Motivation**: Deci & Ryan (Self-Determination Theory, 2000)

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose with Material Design 3
- **Design Tokens**: Midnight Navy (`#101827`), Royal Purple (`#30264D`), Medical Teal (`#30C7B5`), Reward Gold (`#E5B760`), Warm Cream (`#F3ECDC`)
- **Persistence**: Room Database 2.7.0 with Kotlin Symbol Processing (KSP)
- **Architecture**: MVVM with clean Repository pattern & reactive `StateFlow`
- **Testing**: JUnit 4 local unit tests and Robolectric JVM test framework

---

## 📦 How to Build the APK

### Local Command Line
```bash
gradle :app:assembleDebug
```
The output APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Automated GitHub Actions Workflow
The project contains `.github/workflows/android_build.yml` which automatically executes unit tests and uploads the compiled Debug APK as a downloadable artifact on push or pull request.
