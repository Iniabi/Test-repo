# 🛩 Pixel Plane Fighter

![Gameplay Screenshot](screenshot.png)

A retro-style pixel plane shooter game for Android

## 📖 Features
- 🔥 Intuitive touch controls (move plane by dragging, tap to shoot)
- 🔫 Enemy wave generation with increasing difficulty
- ⚔️ Collision detection and scoring system
- 🎮 Simple but addictive gameplay

## 🗄 Build Instructions
```bash
# Clone repository
git clone https://github.com/iniabi/pixel-plane-fighter.git

# Build APK
cd pixel-plane-fighter
./gradlew assembleDebug
```

## 📁 Automatic Builds
APK artifacts are automatically generated on every push to `main` branch:
1. Go to [Actions tab](https://github.com/iniabi/pixel-plane-fighter/actions)
2. Select latest successful workflow run
3. Download "pixel-plane-game" artifact

## 🤖 Technology Stack
- Kotlin
- Android Canvas
- GitHub Actions CI/CD

## 🚀 Coming Soon
- [ ] Multiple enemy types
- [ ] Power-up items
- [ ] Boss battles
- [ ] Online leaderboard