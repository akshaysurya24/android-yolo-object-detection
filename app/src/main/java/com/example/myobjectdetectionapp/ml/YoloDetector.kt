package com.example.myobjectdetectionapp.ml

import android.content.Context
import com.example.myobjectdetectionapp.utils.loadModelFile
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class YoloDetector(context: Context) {

    private val interpreter: Interpreter

    private val inputSize = 320
    private val numClasses = 80

    init {
        val modelBuffer = loadModelFile(
            context,
            "yolov8n_float32.tflite"
        )

        val options = Interpreter.Options().apply {
            setNumThreads(4)
        }

        interpreter = Interpreter(modelBuffer, options)
    }

    fun createInputBuffer(): ByteBuffer {
        return ByteBuffer
            .allocateDirect(1 * inputSize * inputSize * 3 * 4)
            .order(ByteOrder.nativeOrder())
    }

    fun detect(inputBuffer: ByteBuffer): Array<Array<FloatArray>> {

        // YOLOv8 output shape: [1, 84, 2100]
        val output = Array(1) {
            Array(84) {
                FloatArray(2100)
            }
        }

        interpreter.run(inputBuffer, output)
        return output
    }
}