# Changelog

## 2.0.4-widescreen

- Strip Compute renderer scale/tile debug `printf` spam
- Prefer **Compute** as first-run default on Adreno (`qcom`) devices
- WideMelon **presets** (Performance / Balanced / Quality / Max wide)
- Live **GPU cost hint** for width × internal resolution
- Auto-downgrade HQ/XBR filters when width×IR is too heavy (favor Vibrant)
- Skip redundant presentation shader rebuilds when filter/width unchanged
- GitHub Actions workflow `widemelon-debug.yml` uploads debug APK artifacts
- Stop tracking fat APKs in git (`releases/` now points at CI/Releases)

## 2.0.3-widescreen

- Rounded WideMelon X projection/mapping to reduce dashed tile seams
- IR × width performance cap
- Compute renderer widescreen support (Adreno)
- Defaults: OpenGL, 384 width, 2× IR, Vibrant filter
- Auto-wide landscape default layout
- Width-aware presentation filters
- Screenshot center-crop for wide frames
- Docs: architecture, building, troubleshooting, roadmap

## 2.0.2-widescreen

- Top-only widescreen wings (no bottom bleed)
- Vibrant (Pokémon) filter
- Bottom UV center-crop for touchscreen

## Earlier

- Initial WideMelon Android port (OpenGL), app id `me.magnum.melonds.wide`
