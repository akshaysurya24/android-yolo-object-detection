package com.example.myobjectdetectionapp.utils

import android.graphics.Bitmap
import java.nio.ByteBuffer

fun bitmapToYoloInput(
    bitmap: Bitmap,
    inputSize: Int,
    buffer: ByteBuffer
) {
    buffer.rewind()

    val resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
    val pixels = IntArray(inputSize * inputSize)
    resized.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

    for (pixel in pixels) {
        val r = (pixel shr 16 and 0xFF) / 255f
        val g = (pixel shr 8 and 0xFF) / 255f
        val b = (pixel and 0xFF) / 255f

        buffer.putFloat(r)
        buffer.putFloat(g)
        buffer.putFloat(b)
    }
}
