# Contributing (WideMelon DS fork)

## Scope

This repository is a **fork** focused on Android WideMelon widescreen. Prefer small, testable changes:

1. Fix a seam / bleed / crash with a clear repro
2. Improve performance without breaking native 256 mode
3. UX that makes widescreen safer (presets, caps, docs)

See [docs/ROADMAP.md](docs/ROADMAP.md) for prioritized ideas.

## Before you push

- [ ] Native 256 (widescreen off) still looks correct
- [ ] OpenGL + width 384 @ 2×: top wide, bottom no bleed, touch OK
- [ ] Compute (Adreno) + same settings if you touch Compute paths
- [ ] Restart-after-width-change still documented if behavior changes
- [ ] No secrets (keystore passwords, PATs) in the tree
- [ ] Avoid committing large APKs; use Releases

## Code map

Start at [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## License

GPL-3.0 — same family as melonDS / melonDS-android / WideMelon. Your changes must remain compatible.
