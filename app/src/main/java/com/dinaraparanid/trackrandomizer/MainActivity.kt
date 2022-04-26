package com.dinaraparanid.trackrandomizer

import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dinaraparanid.trackrandomizer.ui.Tracks
import com.dinaraparanid.trackrandomizer.ui.theme.TrackRandomizerTheme
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MainActivity : ComponentActivity(), EasyPermissions.PermissionCallbacks {
    private companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scope = rememberCoroutineScope()

            TrackRandomizerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Tracks(tracks = listOf())
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    private inline val isReadExternalStoragePermissionGranted
        get() = EasyPermissions.hasPermissions(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private fun requestReadExternalStoragePermission() = EasyPermissions.requestPermissions(
        this,
        resources.getString(R.string.read_ext_storage_why),
        READ_EXTERNAL_STORAGE_REQUEST_CODE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun loadTracks(): Deferred<List<Track>>? = when {
        !isReadExternalStoragePermissionGranted -> {
            requestReadExternalStoragePermission()
            null
        }

        else -> lifecycleScope.async(Dispatchers.IO) {
            val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            val order = MediaStore.Audio.Media.TITLE + " ASC"

            val projection = mutableListOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
            )

            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection.toTypedArray(),
                selection,
                null,
                order
            ).use { cursor ->
                val list = mutableListOf<Track>()

                if (cursor != null)
                    while (cursor.moveToNext())
                        list.add(
                            Track(
                                title = cursor.getString(0),
                                artist = cursor.getString(1),
                                album = cursor.getString(2),
                                path = cursor.getString(3)
                            )
                        )

                list
            }
        }
    }
}