package com.example.myobjectdetectionapp.ml

import com.example.myobjectdetectionapp.ml.Detection

private const val CONF_THRESHOLD = 0.25f

fun parseYoloOutput(
    output: Array<Array<FloatArray>>,
    imageWidth: Int,
    imageHeight: Int
): List<Detection> {

    val detections = mutableListOf<Detection>()

    val channels = output[0]      // 84
    val numBoxes = channels[0].size

    for (i in 0 until numBoxes) {

        val x = channels[0][i]
        val y = channels[1][i]
        val w = channels[2][i]
        val h = channels[3][i]

        // Find class with highest confidence
        var maxClassScore = 0f
        var classId = -1

        for (c in 4 until 84) {
            if (channels[c][i] > maxClassScore) {
                maxClassScore = channels[c][i]
                classId = c - 4
            }
        }

        if (maxClassScore < CONF_THRESHOLD) continue

        val left = (x - w / 2f) * imageWidth
        val top = (y - h / 2f) * imageHeight
        val right = (x + w / 2f) * imageWidth
        val bottom = (y + h / 2f) * imageHeight


        detections.add(
            Detection(
                left = left,
                top = top,
                right = right,
                bottom = bottom,
                confidence = maxClassScore,
                classId = classId,
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        )
    }

    return detections
}
