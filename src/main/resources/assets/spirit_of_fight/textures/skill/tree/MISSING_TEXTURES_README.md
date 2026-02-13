# Missing Skill Tree Textures

This file documents the missing skill tree textures that need to be created for the Spirit of Fight mod.

## Current Status
Currently, only sword-related textures exist in this directory. Other weapons (gloves, hammers, spears) are missing their corresponding skill tree icons.

## Required Textures

### Gloves Textures
- `gloves_combo.png` - For glove combo attacks
- `gloves_attack.png` - For glove attack skills
- `gloves_block.png` - For glove blocking
- `gloves_dodge.png` - For glove dodging
- `gloves_switch_attack.png` - For glove switch attacks

### Hammer Textures
- `hammer_combo.png` - For hammer combo attacks (some exist but incomplete)
- `hammer_jump_attack.png` - For hammer jump attacks
- `hammer_sprint_attack.png` - For hammer sprint attacks
- `hammer_block.png` - For hammer blocking
- `hammer_dodge.png` - For hammer dodging
- `hammer_switch_attack.png` - For hammer switch attacks

### Spear Textures
- `spear_combo.png` - For spear combo attacks
- `spear_switch_attack.png` - For spear switch attacks

## Texture Guidelines

When creating these textures, follow these guidelines:
1. Use the same resolution as existing textures (typically 16x16 or 32x32 pixels)
2. Follow the same art style as existing textures
3. Use transparent backgrounds (PNG format)
4. Make icons visually distinct for each weapon type
5. Consider using weapon silhouettes or symbols to represent each skill type

## How to Add Textures

1. Create the PNG files in this directory
2. Ensure filenames match the exact registry keys used in the code
3. Test in-game to ensure they appear correctly in the skill tree UI

## Registry Keys Reference

The texture names correspond to these registry keys:
- Gloves: `gloves.combo`, `gloves.attack`, `gloves.block`, `gloves.dodge`, `gloves.switch_attack`
- Hammers: `hammer.combo`, `hammer_jump_attack`, `hammer.sprint_attack`, `hammer.block`, `hammer.dodge`, `hammer.switch_attack`
- Spears: `spear.combo`, `spear.switch_attack`

Note: The texture path follows the pattern `spirit_of_fight:textures/skill/tree/[registry_path].png`