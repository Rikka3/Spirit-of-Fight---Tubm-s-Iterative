<div style="text-align: center;">
  <img src="./IMG/Logo.png" alt="Logo" width="256">
</div>

# Spirit of Fight's Tubm's Iterative (T14)

A Minecraft NeoForge mod that adds an advanced combat system with combo-based weapon skills, blocking mechanics, stun effects, and multiple weapon types.

*this is half written by Ai but important parts are written by me(cut me some slack)*

## What This Mod Does

Spirit of Fight transforms Minecraft combat into a skill by-based fighting game adding:

- **Combo Systems**: Chain attacks together for devastating combos
- **Weapon Skills**: Each weapon type has unique skills (block, dodge, special attacks)
- **Block Mechanics**: Raise your shield to block incoming damage with a visual progress bar
- **Stun System**: Hit enemies to stun them, preventing their movement and attacks
- **Weapon Types**: Multiple weapons including swords, spears, hammers, greatswords, gloves, and axes

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.95+
- **SparkCore**: Required dependency (auto-downloaded from GitHub Packages)

---

## How to Build

1. **Configure GitHub Credentials** (see section below)
2. Open a terminal in the project root
3. Run the build command:

```bash
./gradlew build
```

---

## Configuring GitHub Credentials

This mod uses **SparkCore** as a dependency, which is hosted on GitHub Packages. To build the project, you need GitHub authentication.

### Option 1: Environment Variables (Recommended)

Set these environment variables on your system:

```bash
# Linux/Mac
export GITHUB_ACTOR=your_github_username
export GITHUB_TOKEN=ghp_your_personal_access_token

# Windows (Command Prompt)
set GITHUB_ACTOR=your_github_username
set GITHUB_TOKEN=ghp_your_personal_access_token

# Windows (PowerShell)
$env:GITHUB_ACTOR = "your_github_username"
$env:GITHUB_TOKEN = "ghp_your_personal_access_token"
```

### Option 2: gradle.properties (Local Only)

1. Open `gradle.properties` in a text editor
2. Add your credentials:

```properties
gpr.user=your_github_username
gpr.key=ghp_your_personal_access_token
```

### How to Generate a GitHub Token

1. Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Give it a descriptive name (e.g., "Spirit of Fight Build")
4. Select scopes:
   - ✅ `read:packages` - Required for downloading GitHub Packages
5. Click "Generate token"
6. Copy the token (it starts with `ghp_`)



## Fixes & Improvements

> This section documents the critical fixes applied to this build compared to the original Spirit of Fight.

### Module Registration Fix
**Problem**: SparkCore couldn't sync module resources due to "Module node does not exist" errors.

**Solution**: Fixed double namespace bug in `SpiritOfFight.java` - changed from `spirit_of_fight:spirit_of_fight` to just `spirit_of_fight`.

### Skill Initialization Fix
**Problem**: All weapon skills returned "Skill not found in SkillManager" errors.

**Solution**: In `SOFSkills.kt`, the `register()` function was empty. Added `forceInit()` method that accesses all skill properties, forcing Kotlin to initialize them and trigger registration with SparkCore's SkillManager.

### Animation Path Fix
**Problem**: Game crashed with "Animation not found" errors when using block skills.

**Solution**: Animation resources are in the `sparkcore` namespace, not `spirit_of_fight`. Updated `GenericWeaponSkill.kt` to use correct namespace path: `sparkcore:spirit_of_fight/animations/player/fight_skill/...`

### Deprecated API Fix
**Problem**: Build failed due to deprecated `isMoving` property.

**Solution**: Replaced `animatable.isMoving` with `animatable.deltaMovement.lengthSqr() > 0.001` in `JSSOFAnimatable.kt`.

### Dependency Fix
**Problem**: Bundled SparkCore and jme3 source code was outdated and incompatible.

**Solution**: Removed all bundled source code (92 files). Now uses `spark-core-neoforge` as a Maven dependency from GitHub Packages.

### Ghost Blocks Fix
**Problem**: Ghost blocks appearing after block interactions.

**Solution**: Added try-catch error handling around animation playback in `StateAnimApplier.kt` to prevent server crashes.

### Block Breaking System
**Problem**: No block break mechanics existed.

**Solution**: Implemented full block breaking system with:
- Damage tracking per source
- Block break at 10 hearts damage from single source
- Visual progress bar (action bar)
- Block break animation and recovery

### Stun System
**Problem**: No stun mechanics to reward successful hits.

**Solution**: Implemented comprehensive stun system:
- 10-tick hitstun on every combat hit
- Animation-linked stun duration
- AI action blocking for stunned entities
- Player attack blocking when stunned
- Client-side input blocking

### Greatsword Implementation
**Problem**:It is a placeholder for the time being until future update.

### Custom Items Implementation
**Problem**: You could not use custom items at all 

**Solution**:
Check the wiki page.

---

## Credits

- **Original Mod**: Spirit of Fight by SolarMoon
- **Framework**: SparkCore
- **Build System**: Gradle with NeoForge

## License

GPL-3.0 License
