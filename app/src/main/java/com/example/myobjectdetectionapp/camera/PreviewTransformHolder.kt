package com.example.myobjectdetectionapp.camera

import android.graphics.Matrix
import java.util.concurrent.atomic.AtomicReference

object PreviewTransformHolder {
    val matrix = AtomicReference<Matrix?>(null)
}