package com.hatfat.swccg.inject

import com.hatfat.cards.base.DataReady
import com.hatfat.cards.results.SearchResultsListAdapter
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.temp.InterfaceForTesting
import com.hatfat.cards.temp.TestListInterface
import com.hatfat.swccg.base.SWCCGDataReady
import com.hatfat.swccg.results.SWCCGSearchResultsListAdapter
import com.hatfat.swccg.search.SWCCGCardSearchOptionsProvider
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
    abstract fun bindSearchOptionsProvider(
       swccgSearchOptionsProvider: SWCCGCardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchResultsListAdapter(
        adapter: SWCCGSearchResultsListAdapter
    ): SearchResultsListAdapter

    @Binds
    abstract fun bindInterfaceForTesting(
        tempClass: TempClass
    ): InterfaceForTesting

    @Binds
    abstract fun bindTempListInterface(
        tempListInterfaceImpl: TempListInterfaceImpl
    ): TestListInterface
}
