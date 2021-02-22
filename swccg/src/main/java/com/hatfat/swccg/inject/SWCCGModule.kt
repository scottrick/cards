package com.hatfat.swccg.inject

import com.hatfat.cards.base.DataReady
import com.hatfat.cards.temp.InterfaceForTesting
import com.hatfat.cards.temp.TestListInterface
import com.hatfat.swccg.base.SWCCGDataReady
import com.hatfat.swccg.temp.TempClass
import com.hatfat.swccg.temp.TempListInterfaceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SWCCGModule {

    @Binds
    abstract fun bindDataReady(
        dataReady: SWCCGDataReady
    ): DataReady

    @Binds
    abstract fun bindInterfaceForTesting(
        tempClass: TempClass
    ): InterfaceForTesting

    @Binds
    abstract fun bindTempListInterface(
        tempListInterfaceImpl: TempListInterfaceImpl
    ): TestListInterface
}
