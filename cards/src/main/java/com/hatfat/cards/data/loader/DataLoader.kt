package com.hatfat.cards.data.loader

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson,
    private val rawResourceLoader: RawResourceLoader,
) {
    private val skipNetwork = false
    private val skipCache = false
    private val skipDisk = false

    suspend fun <T> load(desc: DataDesc<T>): T {
        var result: T? = null

        val localStorageWrapper = LocalStorageWrapper(
            context,
            gson,
            desc.name,
            desc.typeToken,
        )

        /* first try the network loader */
        if (!skipNetwork && result == null) {
            try {
                result = desc.networkLoader()
                localStorageWrapper.save(result)
                Log.i(TAG, "[${desc.name}] loaded successfully from the network.")
            } catch (e: Exception) {
                Log.i(TAG, "[${desc.name}] network failure.")
            }
        }

        /* if the network failed, load the latest cached version */
        if (!skipCache && result == null) {
            try {
                result = localStorageWrapper.load()
                Log.i(TAG, "[${desc.name}] loaded successfully from the cache.")
            } catch (e: Exception) {
                Log.i(TAG, "[${desc.name}] cache failure.")
            }
        }

        /* if the cache failed, load the disk backup */
        if (!skipDisk && result == null) {
            try {
                result = rawResourceLoader.load(desc.resourceId, desc.typeToken)
                Log.i(TAG, "[${desc.name}] loaded successfully from the disk.")
            } catch (e: Exception) {
                Log.i(TAG, "[${desc.name}] disk failure.")
            }
        }

        /* if the disk backup failed, use the fallback provided */
        return result ?: desc.fallbackValue
    }

    companion object {
        private val TAG = DataLoader::class.java.simpleName
    }
}