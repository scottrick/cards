package com.hatfat.cards.data.loader

import android.content.res.Resources
import android.util.Log
import androidx.annotation.RawRes
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawResourceLoader @Inject constructor(
    val resources: Resources,
    val gson: Gson,
) {
    fun <T> load(
        @RawRes resourceId: Int,
        type: Class<T>,
    ): T {
        var resource: T? = null

        try {
            val inputStream = resources.openRawResource(resourceId)
            val reader = BufferedReader(InputStreamReader(inputStream))
            resource = gson.fromJson(reader, type)
            reader.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading local resource from disk: $e")
        }

        return resource ?: throw IOException("Failed to load resource.")
    }

    companion object {
        private val TAG = RawResourceLoader::class.java.simpleName
    }
}
