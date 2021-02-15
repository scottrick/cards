package com.hatfat.meccg.inject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.temp.InterfaceForTesting
import com.hatfat.cards.temp.TestListInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object MECCGModule {

    @Provides
    fun bindTempListInterface(
    ): TestListInterface {
        return object : TestListInterface {
            override val stringsLiveData: LiveData<List<String>>
                get() = MutableLiveData(listOf<String>("asdfasdf", "asdfadsF", "213v23v"))
        }
    }

    @Provides
    fun provideTempClass(): InterfaceForTesting {
        return object : InterfaceForTesting {
            override fun testString(): String {
                return "MECCG IMPLEMENTED String."
            }
        }
    }
}
