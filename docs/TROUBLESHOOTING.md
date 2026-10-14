# Troubleshooting

## Performance is much worse with widescreen

**Cause:** 3D framebuffer width × internal resolution. Filters (HQ4X, etc.) add more cost on wide textures.

**Try:**

1. Renderer → **Compute** on Adreno (S25 Ultra)
2. Width **384**, IR **2×**
3. Filter **Vibrant** / **Quilez** / **None** (avoid HQ4X while tuning)
4. Confirm IR wasn’t left at 6–8× from earlier experiments (2.0.3 caps IR vs width, but lower is still faster)

## Black dashed lines / tile seams while moving

**Cause:** Historical integer truncation in WideMelon X projection / screen mapping.

**Status:** Rounded math in `WideMelon.h` (2.0.3+) reduces this. If you still see seams:

- Note game, width, IR, renderer (OpenGL vs Compute)
- Capture a screenshot
- Check whether seams sit on tile boundaries or at wing edges

## Pop-in / geometry appearing late when entering new areas

**Cause:** Many DS games (notably HGSS overworld) **cull** objects outside the original FOV / load distance. Widescreen reveals empty wings until the game submits those polygons.

**Status:** Mostly **not fixable** in the compositor alone. Possible future research: game-specific cheats/patches, or deeper geometry pipeline hacks (high risk, per-title).

## Top screen “bleeding” under / behind the touchscreen

**Cause (pre-2.0.2):** Compositor drew wide 3D into bottom side columns; presentation UVs sampled full width for bottom.

**Status:** Top-only wings + bottom center-crop UVs. If bleed returns:

- Confirm you’re on 2.0.2+
- Custom layouts: bottom rect should not expect stretched widescreen HUD

## Touch screen appears on the widescreen top

**Cause:** The wide view followed the upper LCD. Games that clear the display-swap bit put the touch engine on that LCD, so its UI was drawn across the widescreen top as well as in the touch slot.

**Status:** Fixed in 2.0.5. The widescreen half always shows the 3D engine and the bottom slot always shows the touch engine. Use the in-app swap button if you want those slots exchanged. A game that intentionally puts 3D on the lower LCD will still show 3D on the wide view.

## Fast-forward is barely faster than normal speed

**Cause:** Fast-forward still rendered 3D at the full internal resolution, and rewind captured a GPU readback on that path.

**Status:** 2.0.5 drops internal resolution to 1× while fast-forward is held (widescreen width stays) and skips rewind screenshots during fast-forward. Normal speed uses your configured IR again. On Compute, the first press and release can hitch once while shaders rebuild for that scale.

## Touch / stylus wrong on bottom screen

Bottom input is still native 256×192 mapped into the centered bottom view. If a custom layout stretches the bottom oddly, remapping can feel off—use a layout where bottom stays ~4:3.

## Widescreen option missing

- Software renderer: widescreen forced off
- Compute: requires GLES 3.2 + Adreno (`qcom`); option should appear when Compute is selected (2.0.3+)
- OpenGL: always shows widescreen when OpenGL is selected

## Save-state screenshot looks “zoomed wrong”

Screenshots center-crop the wide frame to 256. Thumbs should look like native DS, not ultra-wide.

## Build / install issues

- Use `--recurse-submodules`
- Match NDK version in `AppConfig`
- `compileSdk` / local SDK must exist
- Prefer `assembleGitHubProdDebug` over multi-flavor assemble
