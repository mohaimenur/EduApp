package com.example.eduapp.helper

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.IOException
import java.io.InputStream

/**
 * Utility function to load an [ImageBitmap] from the application's assets.
 * 
 * @param context The context used to access asset manager.
 * @param path The relative path to the image in the assets folder.
 * @return The loaded bitmap or null if an error occurs.
 */
fun loadAssetImage(context: Context, path: String): ImageBitmap? {
    return try {
        context.assets.open(path).use { inputStream: InputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/**
 * Composable helper that remembers an image loaded from assets.
 * Ensures the image is not reloaded during every recomposition.
 * 
 * @param path The relative path to the image asset.
 * @return The remembered ImageBitmap or null if not found.
 */
@Composable
fun rememberAssetImage(path: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(path) {
        loadAssetImage(context, path)
    }
}
