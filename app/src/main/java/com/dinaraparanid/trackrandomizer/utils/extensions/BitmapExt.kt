package com.dinaraparanid.trackrandomizer.utils.extensions

import android.graphics.Bitmap
import android.graphics.Matrix

internal fun Bitmap.getInScale(width: Int, height: Int) = Bitmap.createBitmap(
    this,
    0, 0,
    width, height,
    Matrix(),
    false
)