package me.magnum.melonds.common.opengl

import me.magnum.melonds.domain.model.VideoFiltering

object VideoFilterShaderProvider {
    fun getShaderSource(filtering: VideoFiltering, textureWidth: Int = 256): ShaderProgramSource {
        val width = textureWidth.coerceIn(256, 768).let { if (it % 2 != 0) it - 1 else it }
        return when (filtering) {
            VideoFiltering.NONE -> ShaderProgramSource.NoFilterShader
            VideoFiltering.LINEAR -> ShaderProgramSource.LinearShader
            VideoFiltering.XBR2 -> ShaderProgramSource.createXbrShader(width)
            VideoFiltering.HQ2X -> ShaderProgramSource.createHq2xShader(width)
            VideoFiltering.HQ4X -> ShaderProgramSource.createHq4xShader(width)
            VideoFiltering.QUILEZ -> ShaderProgramSource.createQuilezShader(width)
            VideoFiltering.LCD -> ShaderProgramSource.createLcdShader(width)
            VideoFiltering.SCANLINES -> ShaderProgramSource.createScanlinesShader(width)
            VideoFiltering.VIBRANT -> ShaderProgramSource.createVibrantShader(width)
        }
    }
}
