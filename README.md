# Nutrition

> Diet history and food variety for TF-Minecraft characters.

Nutrition is a standalone diet plugin that evaluates what a player has eaten over time. It combines the nutritional value of food groups with variety across recent meals, then maps the result to a diet level and associated character effects.

> **Archived repository:** this README describes the preserved plugin logic. The bundled example data is incomplete and does not represent a ready-to-play food catalogue.

## Features

- **Recent meal history** — tracks a rolling selection of consumed foods instead of judging a diet from a single meal.
- **Food groups** — recognises supported vanilla foods and custom MMOItems foods through their assigned groups.
- **Variety scoring** — repeated meals and a more balanced spread of different foods produce different diversity multipliers.
- **Diet levels** — combines food-group values and diversity to determine the player's current nutrition level.
- **Visible feedback** — displays nutrition and diversity bars, with a notification when the diet level changes.
- **Character effects** — diet levels can apply MMOCore attribute values, and meal history is saved between sessions.

## Documentation

[Project documentation](https://github.com/TF-Minecraft/Docs/blob/main/projects/Nutrition/README.md)

Technical documentation is maintained in [TF-Minecraft/Docs](https://github.com/TF-Minecraft/Docs).
