package com.example.myobjectdetectionapp.camera

import android.graphics.RectF
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import com.example.myobjectdetectionapp.detection.DetectionState
import com.example.myobjectdetectionapp.ml.YoloDetector
import com.example.myobjectdetectionapp.ml.parseYoloOutput
import com.example.myobjectdetectionapp.utils.bitmapToYoloInput
import com.example.myobjectdetectionapp.utils.mapRectWithMatrix

class FrameAnalyzer(
    private val detector: YoloDetector,
    private val previewView: PreviewView
) : ImageAnalysis.Analyzer {


    private val inputBuffer = detector.createInputBuffer()
    private var lastAnalyzedTime = 0L

    override fun analyze(image: ImageProxy) {

        val bitmap = image.toBitmap()
        bitmapToYoloInput(bitmap, 320, inputBuffer)

        val output = detector.detect(inputBuffer)

        val rawDetections = parseYoloOutput(
            output,
            bitmap.width,
            bitmap.height
        )

        val mappedDetections = rawDetections.map { det ->

            val rect = RectF(
                det.left,
                det.top,
                det.right,
                det.bottom
            )

            val matrix = PreviewTransformHolder.matrix.get()

            val mappedRect = mapRectWithMatrix(
                matrix,
                rect
            )

            det.copy(
                left = mappedRect.left,
                top = mappedRect.top,
                right = mappedRect.right,
                bottom = mappedRect.bottom
            )
        }

        DetectionState.detections.value = mappedDetections

        image.close()
    }

}