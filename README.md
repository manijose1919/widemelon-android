# WideMelon DS (Android)

Private/personal fork of [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) with **WideMelon-style true widescreen 3D** ported for phone play (Samsung S25 Ultra + USB-C controllers such as the ASUS ROG Tessen).

This is **not** the desktop WideMelon phone-bridge feature. Games run on the phone; your ROG pad is a normal Android gamepad.

Widescreen logic is adapted from [pruefsumme/widemelon](https://github.com/pruefsumme/widemelon) (GPL-3.0).

## What you get

- Expands the DS **3D** view (even widths from 256 to 768) while keeping **2D / menus / touchscreen** at native proportions in the center
- Works with **OpenGL** and **Compute** (Adreno) renderers
- Internal resolution is **auto-capped** with wider views so WideMelon × IR stays smooth on phones
- Defaults: OpenGL, 2:1 (384) widescreen, 2× IR, Vibrant filter
- Landscape default layout uses a wide top screen aspect
- Settings → Video → **Widescreen 3D view**
- App ID `me.magnum.melonds.wide` so it can sit next to stock melonDS

## Requirements

- Android 7.0+ (API 24), GLES 3.2 for OpenGL / Compute renderers
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
3. Set **Renderer** to **OpenGL** (or **Compute** on Adreno / S25 Ultra for better efficiency)
4. Set **Widescreen 3D view** (try **2:1 (384)** first; higher widths cost more GPU)
5. Keep **Internal resolution** at **2×** unless the game stays full speed at higher values
6. Prefer **Vibrant** or **Quilez** filters; heavy filters (HQ4X) cost more with wide frames
7. Start a game (change widescreen before launch, or restart the game after changing)

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

- Widescreen is **game-dependent** (same as desktop WideMelon). Titles like HGSS often cull map tiles outside the original FOV, so **pop-in when panning** can still happen—that is game logic, not the compositor.
- Dashed seam artifacts from projection truncation are reduced via rounded WideMelon math in 2.0.3+.
- Software renderer stays at native 4:3.
- Touch input coordinates for the bottom screen remain native 256×192; only the composited top 3D view expands.
- GPL-3.0: if you distribute binaries, you must provide corresponding source.

## Credits

- [melonDS](https://github.com/melonDS-emu/melonDS)
- [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) by Rafael Caetano
- [WideMelon](https://github.com/pruefsumme/widemelon) widescreen projection/compositing approach
