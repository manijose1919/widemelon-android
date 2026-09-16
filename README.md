# WideMelon DS (Android)

Private/personal fork of [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) with **WideMelon-style true widescreen 3D** for phone play (e.g. Samsung S25 Ultra + USB-C controllers such as the ASUS ROG Tessen).

This is **not** the desktop WideMelon phone-bridge feature. Games run on the phone; a ROG pad is a normal Android gamepad.

Widescreen logic is adapted from [pruefsumme/widemelon](https://github.com/pruefsumme/widemelon) (GPL-3.0).

**Current app version:** `2.0.4-widescreen`

## Docs

| Doc | Contents |
|-----|----------|
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | How WideMelon is wired into melonDS-android |
| [docs/BUILDING.md](docs/BUILDING.md) | Build, flavors, APK output |
| [docs/SETTINGS.md](docs/SETTINGS.md) | Video / WideMelon settings reference |
| [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md) | Seams, pop-in, slowdowns, layouts |
| [docs/ROADMAP.md](docs/ROADMAP.md) | Known bugs, perf work, feature ideas, backlog |
| [CHANGELOG.md](CHANGELOG.md) | Version history |
| [NOTICE.md](NOTICE.md) | License / attribution notes |

## What you get

- Expands the DS **3D** view (even widths from 256 to 768) while keeping **2D / menus / touchscreen** at native proportions in the center
- Works with **OpenGL** and **Compute** (Adreno) renderers
- Internal resolution is **auto-capped** with wider views so WideMelon × IR stays smoother on phones
- Defaults: OpenGL, 2:1 (384) widescreen, 2× IR, Vibrant filter
- Landscape default layout uses a wide top-screen aspect
- Settings → Video → **Widescreen 3D view**
- App ID `me.magnum.melonds.wide` so it can sit next to stock melonDS

## Requirements

- Android 7.0+ (API 24), GLES 3.2 for OpenGL / Compute
- Physical controller optional but recommended
- Legally obtained `.nds` ROMs (not included)

## Quick start (device)

1. Install a debug APK (`adb install -r …`) or build from source ([docs/BUILDING.md](docs/BUILDING.md))
2. **Settings → Video**
3. Renderer: **Compute** on Adreno (S25 Ultra), else **OpenGL**
4. Widescreen: start at **2:1 (384)**; IR **2×**; filter **Vibrant** or **Quilez**
5. Restart the game after changing widescreen width

## Recommended settings (S25 Ultra + HGSS/SS)

| Setting | Value |
|---------|--------|
| Renderer | Compute |
| Widescreen 3D view | 2:1 (384) |
| Internal resolution | 2× |
| Filter | Vibrant (Pokémon) or Quilez |

Higher width × IR multiplies GPU cost. Prefer raising width *or* IR, not both aggressively.

## Notes / limits

- Widescreen is **game-dependent**. Titles like HeartGold/SoulSilver often cull map tiles outside the original FOV → **pop-in when panning** (game logic).
- Dashed seam artifacts are reduced in 2.0.3+ via rounded WideMelon math; report remaining cases with game + width + IR.
- Software renderer stays native 4:3.
- Bottom-screen touch stays native 256×192.
- GPL-3.0: distributing binaries requires corresponding source.

## Credits

- [melonDS](https://github.com/melonDS-emu/melonDS)
- [melonDS-android](https://github.com/rafaelvcaetano/melonDS-android) by Rafael Caetano
- [WideMelon](https://github.com/pruefsumme/widemelon) projection/compositing approach
