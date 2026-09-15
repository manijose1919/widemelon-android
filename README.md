# WideMelon DS (Android)

Private/personal fork of [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) with **WideMelon-style true widescreen 3D** ported for phone play (Samsung S25 Ultra + USB-C controllers such as the ASUS ROG Tessen).

This is **not** the desktop WideMelon phone-bridge feature. Games run on the phone; your ROG pad is a normal Android gamepad.

Widescreen logic is adapted from [pruefsumme/widemelon](https://github.com/pruefsumme/widemelon) (GPL-3.0).

## What you get

- Expands the DS **3D** view (even widths from 256 to 768) while keeping **2D / menus / touchscreen** at native proportions in the center
- OpenGL renderer only (same constraint as desktop WideMelon)
- Settings → Video → **Widescreen 3D view**
- App ID `me.magnum.melonds.wide` so it can sit next to stock melonDS

## Requirements

- Android 7.0+ (API 24), GLES 3.2 for OpenGL renderer
- A physical controller is optional but recommended (ROG Tessen / any HID gamepad)
- Legally obtained `.nds` ROMs (not included)

## Install a prebuilt debug APK

After building (below), install:

```sh
adb install -r app/build/outputs/apk/gitHubProd/debug/app-gitHub-prod-debug.apk
```

## Enable widescreen on device

1. Open **WideMelon DS**
2. **Settings → Video**
3. Set **Renderer** to **OpenGL**
4. Set **Widescreen 3D view** (try **7:3 (448)** or **2:1 (384)** first)
5. Optionally raise **Internal resolution**
6. Start a game (change widescreen before launch, or restart the game after changing)
7. In **Input → Layouts**, widen the top screen rectangle if it looks letterboxed—the framebuffer is wider than 4:3

Your ROG USB-C controller should already work as a standard Android gamepad; map buttons under **Input** if needed.

## Build from source

Needs Android SDK, NDK `28.0.13004108`, CMake, JDK 17+.

```sh
git clone --recurse-submodules <this-repo>
cd <this-repo>
# Create local.properties with: sdk.dir=/path/to/Android/Sdk
./gradlew :app:assembleGitHubProdDebug
```

APK output: `app/build/outputs/apk/gitHubProd/debug/`

## Notes / limits

- Widescreen is **game-dependent** (same as desktop WideMelon). Some titles cull geometry outside the original view.
- Software and Compute renderers stay at native 4:3.
- Touch input coordinates for the bottom screen remain native 256×192; only the composited top 3D view expands.
- GPL-3.0: if you distribute binaries, you must provide corresponding source.

## Credits

- [melonDS](https://github.com/melonDS-emu/melonDS)
- [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) by Rafael Caetano
- [WideMelon](https://github.com/pruefsumme/widemelon) widescreen projection/compositing approach
