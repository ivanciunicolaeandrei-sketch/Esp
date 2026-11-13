# ESP Visual Demo

A small offline Android sandbox that draws **sample-only** ESP-style boxes and labels.

## What it does
- Shows a mock preview with a few fixed sample targets.
- Includes an on/off toggle for the visual demo.
- Builds a debug APK with GitHub Actions.

## What it does not do
- Does not connect to Standoff 2 or any other game.
- Does not read game memory, inspect processes, or display real players.
- Does not modify games or provide gameplay advantages.

## Build
Push to `main`, or open **Actions → Build Android APK → Run workflow**.
When the run completes, download the `ESP-Visual-Demo-debug` artifact.

The APK is a debug build for testing.
