# Changelog

## 2.0.5-widescreen

- Fast-forward renders 3D at 1× internal resolution (widescreen width stays). Releasing fast-forward restores the configured scale.
- Skip rewind GPU readback while fast-forward is held. Manual screenshots still capture.
- OpenGL skips a 3D redraw when geometry is unchanged. A scale change still forces the next frame. On Compute, switching fast-forward on or off rebuilds shaders once for the new scale.
- Widescreen top always shows the 3D engine. The touch engine stays on the bottom screen, including when a game clears the display-swap bit. The in-app swap button still exchanges the two layout slots.

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
