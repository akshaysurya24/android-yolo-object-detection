package com.example.myobjectdetectionapp.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.myobjectdetectionapp.camera.CameraPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {

                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    lifecycleOwner = LocalLifecycleOwner.current
                )

                BoundingBoxOverlay(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            WindowInsets.statusBars
                                .add(WindowInsets(top = 24.dp))
                                .asPaddingValues()
                        )
                )
            }
        }


    }
}