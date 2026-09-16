# Architecture — WideMelon on melonDS-android

This fork adds a **true widescreen 3D view** to the Android port of melonDS. 2D HUD / menus / touchscreen stay centered at native 256×192; only the 3D framebuffer grows horizontally.

## High-level flow

```
Game GPU commands
    → GPU3D (clip space) + WideMelon::ProjectX
    → OpenGL or Compute 3D renderer (ScreenW = Width × IR)
    → GLCompositor (top wings = 3D only; bottom sides black)
    → App presents texture with ScreenTextureUVs
         top  = full width
         bottom = center-crop 256
```

## Key native pieces

| Piece | Role |
|-------|------|
| `melonDS-android-lib/src/WideMelon.h` | Session width (256–768, even), `ProjectX`, `MapHiresX` / `MapFinalX`, side pad |
| `GPU3D.cpp` | Applies `ProjectX` after clip transform |
| `GPU3D_OpenGL.cpp` | Wide FB size; maps vertex X into wide pixels |
| `GPU3D_Compute.cpp` + shaders | Same width path for Adreno Compute; low-res capture center-cropped |
| `GPU_OpenGL.cpp` (compositor) | Top-only wings; bottom side columns stay black (no bleed) |
| `app/.../MelonInstance.cpp` | Sets width before (re)creating OpenGL/Compute renderer; frame size uses `WideMelon::Width()` |

## Key Android / Kotlin pieces

| Piece | Role |
|-------|------|
| `RendererConfiguration` | Effective width for OpenGL+Compute; **IR × width pixel budget cap** |
| `pref_video.xml` / `VideoPreferencesFragment` | Widescreen UI for OpenGL and Compute |
| `ScreenTextureUVs` | Top full-bleed; bottom center UV crop |
| `VideoFilterShaderProvider` | Filters regenerated with actual texture width |
| `DefaultLayoutProvider` | Landscape default uses wide top aspect (~384:192) |
| `ScreenshotRenderer` | Center-crops wide frames to native 256 for save-state thumbs |

## Renderers

| Renderer | Widescreen | Notes |
|----------|------------|--------|
| Software | No | Forced 256 |
| OpenGL | Yes | Baseline WideMelon path |
| Compute | Yes (Adreno) | Usually more efficient on Snapdragon; same width model |

## Performance model

3D cost scales roughly with:

```
horizontal_pixels ≈ widescreen_width × internal_resolution
```

`RendererConfiguration.MAX_HORIZONTAL_PIXELS` (~256×6) caps IR so ultra-wide + high IR cannot silently destroy frame rate.

## Package / identity

- Application ID: `me.magnum.melonds.wide`
- Can be installed beside stock melonDS
- Version name pattern: `X.Y.Z-widescreen` (see `buildSrc/.../AppConfig.kt`)

## Upstream

Track improvements from:

- [rafaelvcaetano/melonDS-android](https://github.com/rafaelvcaetano/melonDS-android)
- [melonDS-emu/melonDS](https://github.com/melonDS-emu/melonDS)
- [pruefsumme/widemelon](https://github.com/pruefsumme/widemelon)
