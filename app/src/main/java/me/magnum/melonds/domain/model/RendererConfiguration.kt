package me.magnum.melonds.domain.model

import me.magnum.melonds.domain.model.render.RenderStrategy

data class RendererConfiguration(
    val renderer: VideoRenderer,
    val videoFiltering: VideoFiltering,
    val threadedRendering: Boolean,
    val renderStrategy: RenderStrategy,
    private val internalResolutionScaling: Int,
    /** WideMelon 3D view width in pixels (even, 256..768). Applied with OpenGL and Compute. */
    val widescreenViewWidth: Int = 256,
) {
    companion object {
        /**
         * Cap horizontal 3D framebuffer pixels so WideMelon × IR stays mobile-friendly.
         * Budget ≈ native 256 × 6 (1536). Examples: 384→max 4×, 512→max 3×, 768→max 2×.
         */
        const val MAX_HORIZONTAL_PIXELS = 256 * 6
    }

    val resolutionScaling get() = when (renderer) {
        VideoRenderer.SOFTWARE -> 1
        VideoRenderer.OPENGL, VideoRenderer.COMPUTE -> {
            val width = effectiveWidescreenViewWidth
            val maxScale = (MAX_HORIZONTAL_PIXELS / width).coerceAtLeast(1)
            internalResolutionScaling.coerceIn(1, maxScale)
        }
    }

    /** Effective widescreen width; forced to native for software. */
    val effectiveWidescreenViewWidth get() = when (renderer) {
        VideoRenderer.OPENGL, VideoRenderer.COMPUTE ->
            widescreenViewWidth.coerceIn(256, 768).let { if (it % 2 != 0) it - 1 else it }
        else -> 256
    }
}
