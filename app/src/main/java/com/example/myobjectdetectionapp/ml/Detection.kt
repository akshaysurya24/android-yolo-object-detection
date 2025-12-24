package com.example.myobjectdetectionapp.ml

import com.example.myobjectdetectionapp.detection.YOLO_CLASSES

data class Detection(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val confidence: Float,
    val classId: Int,
    val imageWidth: Int,
    val imageHeight: Int
) {
    val label: String
        get() = YOLO_CLASSES.getOrNull(classId) ?: "unknown"

    val confidencePercent: Int
        get() = (confidence * 100).toInt()
}