package com.dinaraparanid.trackrandomizer.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.dinaraparanid.trackrandomizer.Track

@Composable
internal fun Tracks(tracks: List<Track>) = LazyColumn { items(tracks) { TrackView(track = it) } }