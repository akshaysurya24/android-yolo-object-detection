package com.example.myobjectdetectionapp.utils

import android.content.Context
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

fun loadModelFile(context: Context, modelName: String): ByteBuffer {
    val assetFileDescriptor = context.assets.openFd(modelName)
    val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel

    val startOffset = assetFileDescriptor.startOffset
    val declaredLength = assetFileDescriptor.declaredLength

    return fileChannel.map(
        FileChannel.MapMode.READ_ONLY,
        startOffset,
        declaredLength
    ).apply {
        order(ByteOrder.nativeOrder())
    }
}
