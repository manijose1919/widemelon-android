package me.magnum.melonds.domain.model

enum class VideoFiltering {
    NONE,
    LINEAR,
    XBR2,
    HQ2X,
    HQ4X,
    QUILEZ,
    LCD,
    SCANLINES,
    /** Soft upscale + mild vibrance/contrast for DS Pokémon-style pixel art. */
    VIBRANT,
}