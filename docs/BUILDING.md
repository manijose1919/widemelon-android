# Building WideMelon DS

## Prerequisites

- JDK 17+
- Android SDK (compile/target SDK per `AppConfig`)
- NDK `28.0.13004108` (see `buildSrc/src/main/kotlin/AppConfig.kt`)
- CMake (via SDK)
- Git with submodules initialized

```sh
git clone --recurse-submodules https://github.com/manijose1919/widemelon-android.git
cd widemelon-android
```

Create `local.properties`:

```properties
sdk.dir=/path/to/Android/Sdk
```

## Debug APK (GitHub flavor)

```sh
./gradlew :app:assembleGitHubProdDebug
```

Output:

```text
app/build/outputs/apk/gitHubProd/debug/app-gitHub-prod-debug.apk
```

Install:

```sh
adb install -r app/build/outputs/apk/gitHubProd/debug/app-gitHub-prod-debug.apk
```

A copy is sometimes kept under `releases/WideMelonDS-debug.apk` for convenience. That file is **~50 MB** (GitHub’s soft limit); prefer CI artifacts or Releases with attached binaries rather than bloating git history long-term.

## Useful Gradle tips

```sh
./gradlew --stop                    # clear stuck daemons
./gradlew :app:assembleGitHubProdDebug --no-daemon
```

Avoid assembling every flavor at once if the machine is under memory pressure; prefer a single flavor (`GitHubProdDebug`).

## After changing widescreen

Widescreen width is applied when the 3D renderer is created. **Restart the game** (or reload) after changing **Widescreen 3D view** so FBOs and projection match.

## Release hygiene (suggested)

- [ ] Bump `versionCode` / `versionName` in `AppConfig.kt`
- [ ] Update `docs/ROADMAP.md` / changelog notes
- [ ] Do **not** commit multi‑tens‑of‑MB APKs forever; use GitHub Releases
- [ ] Keep GPL source available if you distribute binaries
