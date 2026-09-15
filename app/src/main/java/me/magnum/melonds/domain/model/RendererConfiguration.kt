package me.magnum.melonds.domain.model

import me.magnum.melonds.domain.model.render.RenderStrategy

data class RendererConfiguration(
    val renderer: VideoRenderer,
    val videoFiltering: VideoFiltering,
    val threadedRendering: Boolean,
    val renderStrategy: RenderStrategy,
    private val internalResolutionScaling: Int,
    /** WideMelon 3D view width in pixels (even, 256..768). Only applied with OpenGL. */
    val widescreenViewWidth: Int = 256,
) {

    val resolutionScaling get() = when (renderer) {
        VideoRenderer.SOFTWARE -> 1
        VideoRenderer.OPENGL -> internalResolutionScaling
        VideoRenderer.COMPUTE -> internalResolutionScaling
    }

    /** Effective widescreen width; forced to native for non-OpenGL renderers. */
    val effectiveWidescreenViewWidth get() = when (renderer) {
        VideoRenderer.OPENGL -> widescreenViewWidth.coerceIn(256, 768).let { if (it % 2 != 0) it - 1 else it }
        else -> 256
    }
}
