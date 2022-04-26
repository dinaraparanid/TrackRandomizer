package com.dinaraparanid.trackrandomizer.ui

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinaraparanid.trackrandomizer.R
import com.dinaraparanid.trackrandomizer.Track
import com.dinaraparanid.trackrandomizer.ui.theme.Purple700
import com.dinaraparanid.trackrandomizer.ui.theme.TextLight
import com.dinaraparanid.trackrandomizer.utils.extensions.bitmap
import com.skydoves.landscapist.glide.GlideImage
import org.jaudiotagger.audio.AudioFileIO
import java.io.File

@Composable
internal fun TrackView(track: Track) = Row(Modifier.wrapContentWidth()) {
    GlideImage(
        imageModel = getTrackCover(track, LocalContext.current.resources),
        contentScale = ContentScale.FillBounds,
        placeHolder = painterResource(id = R.drawable.album_default),
        error = painterResource(id = R.drawable.album_default),
        modifier = Modifier
            .size(60.dp, 60.dp)
            .border(
                width = 2.dp,
                color = Purple700,
                shape = CircleShape
            )
    )

    Column(modifier = Modifier.weight(weight = 1F, fill = true)) {
        Text(text = track.title, fontSize = 20.sp, color = TextLight)
        Text(text = "${track.artist} / ${track.album}", fontSize = 16.sp, color = TextLight)
    }
}

private fun getTrackCover(track: Track, resources: Resources) = when {
    Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> MediaMetadataRetriever()
        .apply { setDataSource(track.path) }
        .embeddedPicture
        ?.bitmap

    else -> AudioFileIO
        .read(File(track.path))
        .tagOrCreateAndSetDefault
        ?.firstArtwork
        ?.binaryData
        ?.bitmap
} ?: BitmapFactory.decodeResource(resources, R.drawable.album_default)