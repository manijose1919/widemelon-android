# Roadmap & future work

Living backlog for WideMelon DS (Android). Items are ordered by practical impact, not calendar estimates. Check boxes as work lands; open GitHub issues for anything you start tracking publicly.

## Known issues / bug fixes

### High impact

- [ ] **Residual tile seams** — re-verify rounded `ProjectX` / `MapHiresX` across IR 1–4 and widths 320–512 on HGSS/BW; fix any remaining precision paths (soft edges, shadow polys, special vertex paths).
- [ ] **Per-game pop-in** — document which titles cull hard (HGSS, etc.); investigate optional cheats/AR or title-specific hooks only where safe and legal.
- [ ] **Layout vs framebuffer mismatch** — custom layouts still letterbox a wide FB inside a 4:3 top rect; add “match widescreen width” helper or auto-resize top screen when width changes.
- [ ] **Runtime width change without full restart** — today users must restart the game; make settings apply cleanly mid-session (recreate renderer + notify UI) with fewer footguns.

### Medium

- [ ] **APK in git history** — `releases/*.apk` exceeds GitHub’s ~50 MB soft limit; move binaries to GitHub Releases / CI artifacts; purge or LFS if history must stay clean.
- [ ] **Filter cost on wide frames** — HQ2X/HQ4X/XBR still heavy; auto-suggest lighter filters when width × IR exceeds a threshold.
- [ ] **External display / dual-screen** — verify top-on-external + bottom-on-phone with widescreen UVs and aspect.
- [ ] **Foldables / book layouts** — default folding layouts still use native aspect for both screens; optional wide-top on the primary pane.
- [ ] **Regression suite** — scripted checklist: native 256, 384@2× OpenGL, 384@2× Compute, bottom touch, save-state screenshot, no bottom bleed.

### Lower

- [ ] **Software renderer widescreen** — usually not worth it (CPU); only if someone needs it for debugging.
- [ ] **i18n** — translate new widescreen / Compute help strings.
- [ ] **Telemetry-free FPS overlay tips** — in-app tip when sustained FPS < 55 with width×IR over budget.

## Performance optimizations

- [ ] **Default to Compute on Adreno** when available (first-run or capability probe), keep OpenGL fallback.
- [ ] **Adaptive IR** — optionally drop IR one step when frame time spikes under widescreen (user toggle).
- [ ] **Compositor pass** — profile `GLCompositor` on wide FBOs; avoid redundant clears/uploads; ensure padding fill isn’t per-frame heavy.
- [ ] **Compute tile sizing** — revisit `TileSize` / dispatch when `ScreenWidth` is non-multiple-of-old assumptions; micro-opt Adreno occupancy.
- [ ] **Presentation path** — skip expensive filter shader recompiles unless filtering or width actually changed.
- [ ] **Thermal / sustained** — document battery profiles (384@2× vs 512@2×) for S25 Ultra + ROG grip sessions.
- [ ] **Strip debug logging** in Compute (`printf` scale/tile) for release builds.

## Features worth adding

### Rendering / video

- [ ] **Presets** — “Pokémon handheld”, “Max wide”, “Performance” one-taps (renderer + width + IR + filter).
- [ ] **Per-game video overrides** — remember widescreen profile per ROM (like layouts).
- [ ] **Optional wing fill modes** — black / mirror edge / blur (cosmetic; default remains black/empty 3D clear).
- [ ] **Better Pokémon visual modes** — tune Vibrant; optional LCD+Vibrant combo carefully.
- [ ] **Shader inventory** — expose which filters are width-accurate; hide broken combos.

### Input / UI

- [ ] **Wide-first layout pack** — landscape: large top wide, small bottom corner (matches many users’ manual layouts).
- [ ] **Controller profiles** — ROG Tessen / Gamesir quick maps.
- [ ] **Onboarding** — first-run card: Compute + 384 + 2× + restart reminder.
- [ ] **In-settings cost meter** — show relative GPU cost of width × IR before apply.

### Platform / distribution

- [ ] **GitHub Actions** — build `assembleGitHubProdDebug` (and release) on tag; attach APK to Release.
- [ ] **Signed release builds** — user-owned keystore docs (never commit secrets).
- [ ] **Keeppace with upstream melonDS-android** — periodic merge; resolve WideMelon conflicts deliberately.
- [ ] **Changelog** — `CHANGELOG.md` per widescreen version.

### Stretch / research

- [ ] **True FOV patches** for specific 3D engines (high effort, per-game).
- [ ] **Vulkan** if upstream melonDS gains a mobile path (long-term).
- [ ] **Non-Adreno Compute** if melonDS Compute gains broader GPU support.
- [ ] **Desktop WideMelon parity audit** — compare projection/compositor behavior with pruefsumme/widemelon periodically.

## Documentation still useful to write

- [x] Architecture overview (`docs/ARCHITECTURE.md`)
- [x] Building (`docs/BUILDING.md`)
- [x] Troubleshooting (`docs/TROUBLESHOOTING.md`)
- [x] This roadmap
- [ ] **Game compatibility matrix** — community table (title, width sweet spot, Compute OK?, notes on cull/seams)
- [ ] **Settings reference** — every Video preference explained
- [ ] **Layout authoring guide** — designing for wide top + native bottom
- [ ] **Contributing** — coding style, how to test widescreen changes, PR checklist
- [ ] **Security / secrets** — never commit PATs; rotate if pasted into chat/CI logs

## Non-goals (for now)

- Shipping copyrighted ROMs or BIOS dumps
- Claiming “perfect” widescreen in cull-heavy overworlds without game patches
- Maintaining a huge APK inside git as the primary distribution method

## Suggested next 3 engineering slices

1. **CI + GitHub Release APK** (stop relying on fat files in `releases/`)
2. **Per-game video presets + onboarding defaults for Adreno Compute**
3. **Seam regression pass + optional adaptive IR**

---

When closing an item, note the app version (e.g. `2.0.4-widescreen`) in the commit or changelog.
