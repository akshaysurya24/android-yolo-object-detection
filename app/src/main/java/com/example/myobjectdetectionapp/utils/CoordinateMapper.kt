package com.example.myobjectdetectionapp.utils

import android.graphics.Matrix
import android.graphics.RectF

fun mapRectWithMatrix(
    matrix: Matrix?,
    rect: RectF
): RectF {
    if (matrix == null) return rect

    val mapped = RectF(rect)
    matrix.mapRect(mapped)
    return mapped
}
