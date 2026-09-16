# Settings reference (Video / WideMelon)

## Renderer

| Value | When to use |
|-------|-------------|
| Software | Compatibility / debugging only (no widescreen) |
| OpenGL | Default GLES path; widescreen supported |
| Compute | **Preferred on Adreno** (Snapdragon / S25 Ultra); widescreen supported |

## WideMelon preset

One-tap profiles (restart game after width changes):

| Preset | Width | IR | Filter | Notes |
|--------|-------|----|--------|--------|
| Performance | 320 | 2× | None | Lightest wide profile |
| Balanced | 384 | 2× | Vibrant | Recommended daily driver |
| Quality | 384 | 3× | Quilez | Heavier; needs headroom |
| Max wide | 512 | 2× | Vibrant | Wider FOV, still capped IR |

On non-Adreno devices, presets that prefer Compute fall back to OpenGL.

## Widescreen 3D view

Even widths 256–768. Only affects **3D**; 2D/touch stay centered native 256.

**Restart the game** after changing this value.

## Internal resolution

Multiplies 3D resolution. Combined with width and **auto-capped** so `width × IR ≤ 1536` (see `RendererConfiguration.MAX_HORIZONTAL_PIXELS`).

## Estimated 3D GPU cost

Read-only hint: `Light / Comfortable / Heavy / Very heavy` from width × effective IR. Use it before applying extreme combos.

## Filter

| Filter | Notes |
|--------|--------|
| None / Linear | Cheapest |
| Vibrant / Quilez | Recommended for Pokémon |
| LCD / Scanlines | Style filters |
| HQ2X / HQ4X / 2xBR | Expensive on wide frames — auto-downgraded when cost is high |

## Threaded rendering

Software renderer only. Improves 3D software performance; may cause rare glitches.
