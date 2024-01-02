package com.mcr.e_library.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush

class ColorPalette() {
    val LightBlue = Color(0xFF03A9F4)
    val DarkBlue = Color(0xFF0B3C53)
    val Gray = Color(0xFF858686)
    val SoftGray = Color(0xFFE7E6E6)
    val SoftDarkGray = Color(0xFFDDDCDC)
    val Invisible = Color(0x858686)
    val Dark10 = Color(0x1A000000)
    val Dark25 = Color(0x40000000)
    val Dark50 = Color(0x80000000)
    val Pink = Color(0xFFEB69A0)

    //Gradient
    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(LightBlue,Pink),
                center = size.center,
                radius = biggerDimension / 2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }
}