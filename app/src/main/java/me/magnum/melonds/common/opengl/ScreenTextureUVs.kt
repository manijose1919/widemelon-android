package me.magnum.melonds.common.opengl

/**
 * UV helpers for the dual-screen compositor framebuffer.
 *
 * When WideMelon is active the texture is wider than 256px. Top screen samples
 * the full width (including 3D wings). Bottom/touch screen samples only the
 * centered native 256px so widescreen never bleeds into the touch panel.
 */
object ScreenTextureUVs {
    private const val NATIVE_WIDTH = 256
    private const val FRAME_HEIGHT = 192 * 2 + 2

    fun lineRelativeSize(): Float = 1f / FRAME_HEIGHT.toFloat()

    fun topUVs(widescreenViewWidth: Int = NATIVE_WIDTH): FloatArray {
        val line = lineRelativeSize()
        // Full horizontal span — widescreen wings included
        return floatArrayOf(
            0f, 0.5f - line,
            0f, 0f,
            1f, 0f,
            0f, 0.5f - line,
            1f, 0f,
            1f, 0.5f - line,
        )
    }

    fun bottomUVs(widescreenViewWidth: Int = NATIVE_WIDTH): FloatArray {
        val line = lineRelativeSize()
        val width = widescreenViewWidth.coerceAtLeast(NATIVE_WIDTH).toFloat()
        val pad = ((width - NATIVE_WIDTH) / 2f) / width
        val uLeft = pad
        val uRight = 1f - pad
        return floatArrayOf(
            uLeft, 1f,
            uLeft, 0.5f + line,
            uRight, 0.5f + line,
            uLeft, 1f,
            uRight, 0.5f + line,
            uRight, 1f,
        )
    }
}
