# Flow Photos

Custom Android photo & video gallery — Jetpack Compose, local-first, Flow aesthetic.

## Stack

- Kotlin + Jetpack Compose + Material 3
- Coil for image loading
- MediaStore for local media
- Accompanist permissions

## Features (v0.1)

- Permission flow (READ_MEDIA_* / storage)
- Adaptive photo/video grid
- Flow cinematic dark theme
- Video indicator on thumbs

## Build

```bash
./gradlew :app:assembleDebug
```

Open in Android Studio, sync Gradle, run on device/emulator.

**Note:** Add a launcher icon under `app/src/main/res/mipmap-*` (or use Android Studio → New → Image Asset) before release builds.

## Roadmap

- [ ] Full-screen viewer (pinch zoom, swipe)
- [ ] Albums / folders
- [ ] Video playback
- [ ] Search
- [ ] Liquid glass polish
- [ ] Brand assets (icon, splash)
