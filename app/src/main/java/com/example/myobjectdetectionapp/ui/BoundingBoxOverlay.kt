package com.example.myobjectdetectionapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import android.text.TextPaint
import com.example.myobjectdetectionapp.detection.DetectionState


@Composable
fun BoundingBoxOverlay(
    modifier: Modifier = Modifier
) {
    val detections by DetectionState.detections

    Canvas(modifier = modifier) {

        detections.forEach { det ->

            // Choose class color (deterministic, pretty)
            val color = getPrettyColor(det.classId)

            val boxLeft = det.left
            val boxTop = det.top
            val boxRight = det.right
            val boxBottom = det.bottom

            // Draw rounded bounding box
            drawRoundRect(
                color = color,
                topLeft = Offset(boxLeft, boxTop),
                size = Size(boxRight - boxLeft, boxBottom - boxTop),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = 5f)
            )

            val label = "${det.label} ${det.confidencePercent}%"

            // Prepare text
            val textPaint = TextPaint().apply {
                this.color = Color.White.toArgb()
                textSize = 32.sp.toPx()
                isAntiAlias = true
            }

            val textWidth = textPaint.measureText(label)
            val textHeight = textPaint.textSize

            // Background for text (semi-transparent)
            /*val bgPaint = Paint().apply {
                this.color = color.copy(alpha = 0.8f)
            }*/

            // Draw label background rounded rectangle
            drawRoundRect(
                color = color.copy(alpha = 0.75f),
                topLeft = Offset(boxLeft, boxTop - textHeight - 16),
                size = Size(textWidth + 24, textHeight + 12),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Draw label text
            drawContext.canvas.nativeCanvas.drawText(
                label,
                boxLeft + 12,
                boxTop - 16,
                textPaint
            )
        }
    }
}

/**
 * Creates beautiful, consistent class colors similar to YOLO’s style.
 */
fun getPrettyColor(classId: Int): Color {
    val r = (abs((classId * 37) % 255))
    val g = (abs((classId * 17) % 255))
    val b = (abs((classId * 29) % 255))

    return Color(r, g, b, 255)
}
