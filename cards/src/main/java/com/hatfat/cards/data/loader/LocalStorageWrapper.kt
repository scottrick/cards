package com.hatfat.cards.data.loader

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.*

class LocalStorageWrapper<T>(
    val context: Context,
    val gson: Gson,
    val resourceName: String,
    val type: Class<T>,
) {
    private val fileName: String
        get() = "$resourceName.json"

    fun load(): T {
        var result: T? = null

        try {
            val inputFileStream = FileInputStream(File(context.cacheDir, fileName))
            val bufferedInputStream = BufferedInputStream(inputFileStream)
            val inputStreamReader = InputStreamReader(bufferedInputStream)
            result = gson.fromJson(inputStreamReader, type)
            inputFileStream.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error reading json from disk: $e")
        }

        return result ?: throw IOException("Failed to load resource.")
    }

    fun save(newValue: T) {
        try {
            val outputFileStream = FileOutputStream(File(context.cacheDir, fileName))
            val bufferedOutputStream = BufferedOutputStream(outputFileStream)
            val outputStreamWriter = OutputStreamWriter(bufferedOutputStream)
            gson.toJson(newValue, outputStreamWriter)
            outputStreamWriter.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error writing json to disk: $e")
        }
    }

    companion object {
        private val TAG = LocalStorageWrapper::class.java.simpleName
    }
}