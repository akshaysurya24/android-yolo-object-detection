package com.example.myobjectdetectionapp.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.myobjectdetectionapp.ml.YoloDetector
import com.example.myobjectdetectionapp.camera.PreviewTransformHolder
import com.example.myobjectdetectionapp.camera.FrameAnalyzer
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            startCamera(ctx, lifecycleOwner, previewView)
            previewView
        }
    )
}

@SuppressLint("RestrictedApi")
@OptIn(TransformExperimental::class)
private fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({

        val cameraProvider = cameraProviderFuture.get()

        // 1️⃣ Camera Preview use-case
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // 2️⃣ YOLO Detector (load model ONCE)
        val detector = YoloDetector(context)

        // 3️⃣ Background executor for ML
        val analysisExecutor = Executors.newSingleThreadExecutor()

        // 4️⃣ Image analysis use-case
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(
                    analysisExecutor,
                    FrameAnalyzer(detector, previewView)
                )
            }

        // 5️⃣ Camera selector
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // 6️⃣ Bind use-cases
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )

// 🔴 IMPORTANT: capture transform on MAIN thread
        previewView.post {
            val outputTransform = previewView.outputTransform
            PreviewTransformHolder.matrix.set(outputTransform?.matrix)
        }

    }, ContextCompat.getMainExecutor(context))
}
