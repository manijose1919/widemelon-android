package me.magnum.melonds.domain.model

const val SCREEN_WIDTH = 256
const val SCREEN_HEIGHT = 192

/** Default WideMelon top-screen profile used by auto layouts (2:1 / 384×192). */
const val WIDE_TOP_SCREEN_WIDTH = 384

val consoleAspectRatio: Float get() {
    return SCREEN_WIDTH.toFloat() / SCREEN_HEIGHT
}

val wideTopAspectRatio: Float get() {
    return WIDE_TOP_SCREEN_WIDTH.toFloat() / SCREEN_HEIGHT
}
