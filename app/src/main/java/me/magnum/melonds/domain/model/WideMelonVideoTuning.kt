package me.magnum.melonds.domain.model

/**
 * Helpers for WideMelon video cost hints and safe presets.
 * Horizontal cost ≈ width × internal resolution (3D framebuffer width in pixels).
 */
object WideMelonVideoTuning {
    /** Above this, heavy pixel filters (HQ/XBR) tend to hitch on phones. */
    const val HEAVY_FILTER_COST_THRESHOLD = 768

    /** Soft “comfortable” budget for sustained handheld play (~384×2). */
    const val COMFORT_COST = 768

    fun normalizeWidth(width: Int): Int =
        width.coerceIn(256, 768).let { if (it % 2 != 0) it - 1 else it }

    fun cappedScale(width: Int, requestedScale: Int): Int {
        val w = normalizeWidth(width)
        val maxScale = (RendererConfiguration.MAX_HORIZONTAL_PIXELS / w).coerceAtLeast(1)
        return requestedScale.coerceIn(1, maxScale)
    }

    fun horizontalCost(width: Int, scale: Int): Int =
        normalizeWidth(width) * cappedScale(width, scale)

    fun isHeavyFilter(filtering: VideoFiltering): Boolean = when (filtering) {
        VideoFiltering.XBR2, VideoFiltering.HQ2X, VideoFiltering.HQ4X -> true
        else -> false
    }

    fun preferLighterFilter(filtering: VideoFiltering, width: Int, scale: Int): VideoFiltering {
        if (!isHeavyFilter(filtering)) return filtering
        return if (horizontalCost(width, scale) > HEAVY_FILTER_COST_THRESHOLD) {
            VideoFiltering.VIBRANT
        } else {
            filtering
        }
    }

    enum class Preset(val key: String) {
        PERFORMANCE("performance"),
        BALANCED("balanced"),
        QUALITY("quality"),
        MAX_WIDE("max_wide");

        companion object {
            fun fromKey(key: String): Preset =
                entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: BALANCED
        }
    }

    data class PresetValues(
        val rendererHint: String, // "compute" preferred when Adreno; UI may override
        val width: Int,
        val scale: Int,
        val filtering: String,
    )

    fun presetValues(preset: Preset): PresetValues = when (preset) {
        Preset.PERFORMANCE -> PresetValues("compute", 320, 2, "none")
        Preset.BALANCED -> PresetValues("compute", 384, 2, "vibrant")
        Preset.QUALITY -> PresetValues("compute", 384, 3, "quilez")
        Preset.MAX_WIDE -> PresetValues("compute", 512, 2, "vibrant")
    }

    fun costLabel(width: Int, scale: Int): String {
        val cost = horizontalCost(width, scale)
        val effectiveScale = cappedScale(width, scale)
        val w = normalizeWidth(width)
        val tier = when {
            cost <= 512 -> "Light"
            cost <= COMFORT_COST -> "Comfortable"
            cost <= 1152 -> "Heavy"
            else -> "Very heavy"
        }
        return "$tier · ${w}×${effectiveScale} (= $cost px wide)"
    }
}
