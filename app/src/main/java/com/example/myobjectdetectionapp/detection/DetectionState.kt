package com.example.myobjectdetectionapp.detection

import androidx.compose.runtime.mutableStateOf
import com.example.myobjectdetectionapp.ml.Detection

object DetectionState {
    val detections = mutableStateOf<List<Detection>>(emptyList())
}
