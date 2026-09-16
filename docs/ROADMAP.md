# Roadmap & future work

Living backlog for WideMelon DS (Android). Items are ordered by practical impact, not calendar estimates. Check boxes as work lands.

## Known issues / bug fixes

### High impact

- [ ] **Residual tile seams** — re-verify rounded `ProjectX` / `MapHiresX` across IR 1–4 and widths 320–512 on HGSS/BW; fix any remaining precision paths (soft edges, shadow polys, special vertex paths).
- [ ] **Per-game pop-in** — document which titles cull hard (HGSS, etc.); investigate optional cheats/AR or title-specific hooks only where safe and legal.
- [ ] **Layout vs framebuffer mismatch** — custom layouts still letterbox a wide FB inside a 4:3 top rect; add “match widescreen width” helper or auto-resize top screen when width changes.
- [ ] **Runtime width change without full restart** — today users must restart the game; make settings apply cleanly mid-session (recreate renderer + notify UI) with fewer footguns.

### Medium

- [x] **APK in git** — stopped tracking `releases/*.apk`; CI uploads artifacts (`2.0.4`)
- [x] **Filter cost on wide frames** — cost hint + auto-downgrade HQ/XBR when width×IR is heavy (`2.0.4`)
- [ ] **External display / dual-screen** — verify top-on-external + bottom-on-phone with widescreen UVs and aspect.
- [ ] **Foldables / book layouts** — default folding layouts still use native aspect for both screens; optional wide-top on the primary pane.
- [ ] **Regression suite** — scripted checklist: native 256, 384@2× OpenGL, 384@2× Compute, bottom touch, save-state screenshot, no bottom bleed.

### Lower

- [ ] **Software renderer widescreen** — usually not worth it (CPU); only if someone needs it for debugging.
- [ ] **i18n** — translate new widescreen / Compute help strings.
- [ ] **Telemetry-free FPS overlay tips** — in-app tip when sustained FPS < 55 with width×IR over budget.

## Performance optimizations

- [x] **Default to Compute on Adreno** when available (first-run) (`2.0.4`)
- [ ] **Adaptive IR** — optionally drop IR one step when frame time spikes under widescreen (user toggle).
- [ ] **Compositor pass** — profile `GLCompositor` on wide FBOs; avoid redundant clears/uploads; ensure padding fill isn’t per-frame heavy.
- [ ] **Compute tile sizing** — revisit `TileSize` / dispatch for non-256 widths; micro-opt Adreno occupancy.
- [x] **Presentation path** — skip filter shader recompiles unless filtering or width changed (`2.0.4`)
- [ ] **Thermal / sustained** — document battery profiles (384@2× vs 512@2×) for S25 Ultra + ROG grip sessions.
- [x] **Strip debug logging** in Compute (`printf` scale/tile) (`2.0.4`)

## Features worth adding

### Rendering / video

- [x] **Presets** — Performance / Balanced / Quality / Max wide (`2.0.4`)
- [ ] **Per-game video overrides** — remember widescreen profile per ROM (like layouts).
- [ ] **Optional wing fill modes** — black / mirror edge / blur (cosmetic; default remains black/empty 3D clear).
- [ ] **Better Pokémon visual modes** — tune Vibrant; optional LCD+Vibrant combo carefully.
- [ ] **Shader inventory** — expose which filters are width-accurate; hide broken combos.

### Input / UI

- [ ] **Wide-first layout pack** — landscape: large top wide, small bottom corner (matches many users’ manual layouts).
- [ ] **Controller profiles** — ROG Tessen / Gamesir quick maps.
- [ ] **Onboarding** — first-run card: Compute + 384 + 2× + restart reminder.
- [x] **In-settings cost meter** — relative GPU cost of width × IR (`2.0.4`)

### Platform / distribution

- [x] **GitHub Actions debug APK** — `widemelon-debug.yml` (`2.0.4`)
- [ ] **Tag Release workflow** — attach APK on version tags
- [ ] **Signed release builds** — user-owned keystore docs (never commit secrets).
- [ ] **Keep pace with upstream melonDS-android** — periodic merge; resolve WideMelon conflicts deliberately.
- [x] **Changelog** — `CHANGELOG.md`

### Stretch / research

- [ ] **True FOV patches** for specific 3D engines (high effort, per-game).
- [ ] **Vulkan** if upstream melonDS gains a mobile path (long-term).
- [ ] **Non-Adreno Compute** if melonDS Compute gains broader GPU support.
- [ ] **Desktop WideMelon parity audit** — compare projection/compositor behavior with pruefsumme/widemelon periodically.

## Documentation

- [x] Architecture / Building / Troubleshooting / Roadmap / Contributing
- [x] Settings reference (`docs/SETTINGS.md`)
- [ ] **Game compatibility matrix**
- [ ] **Layout authoring guide**
- [ ] **Security / secrets** note for contributors

## Suggested next slices

1. **Layout match-widescreen helper** + wide-first landscape pack
2. **Seam regression pass** + any remaining OpenGL/Compute X precision paths
3. **Per-game video overrides** + optional adaptive IR toggle
4. **Tag → GitHub Release** APK workflow

---

When closing an item, note the app version (e.g. `2.0.4-widescreen`) in the commit or changelog.
