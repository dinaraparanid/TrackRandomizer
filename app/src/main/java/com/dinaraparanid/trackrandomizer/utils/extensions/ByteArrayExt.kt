package com.dinaraparanid.trackrandomizer.utils.extensions

import android.graphics.BitmapFactory

internal inline val ByteArray.bitmap
    get() = BitmapFactory.decodeByteArray(this, 0, size)