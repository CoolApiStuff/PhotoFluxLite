
package com.example.photofluxlite

import android.graphics.Bitmap
import android.graphics.Color

object Flux1Lite {
    fun adjustBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, bitmap.config)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel) + value
                val g = Color.green(pixel) + value
                val b = Color.blue(pixel) + value
                adjustedBitmap.setPixel(x, y, Color.rgb(clamp(r), clamp(g), clamp(b)))
            }
        }
        return adjustedBitmap
    }

    fun adjustContrast(bitmap: Bitmap, value: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
        val contrast = (value + 100) / 100f
        val offset = 128 * (1 - contrast)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val r = (Color.red(pixel) * contrast + offset).toInt()
                val g = (Color.green(pixel) * contrast + offset).toInt()
                val b = (Color.blue(pixel) * contrast + offset).toInt()
                adjustedBitmap.setPixel(x, y, Color.rgb(clamp(r), clamp(g), clamp(b)))
            }
        }
        return adjustedBitmap
    }

    fun adjustSaturation(bitmap: Bitmap, value: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val adjustedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
        val saturation = value / 100f

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val gray = (r + g + b) / 3
                val newR = gray + (r - gray) * saturation
                val newG = gray + (g - gray) * saturation
                val newB = gray + (b - gray) * saturation
                adjustedBitmap.setPixel(x, y, Color.rgb(clamp(newR.toInt()), clamp(newG.toInt()), clamp(newB.toInt())))
            }
        }
        return adjustedBitmap
    }

    fun crop(bitmap: Bitmap, left: Int, top: Int, right: Int, bottom: Int): Bitmap {
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    private fun clamp(value: Int): Int {
        return when {
            value < 0 -> 0
            value > 255 -> 255
            else -> value
        }
    }
}
