package com.dinaraparanid.trackrandomizer

import java.io.Serializable

internal data class Track(
    internal val path: String,
    internal val title: String,
    internal val artist: String,
    internal val album: String,
) : Serializable