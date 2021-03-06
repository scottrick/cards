package com.hatfat.swccg.inject

import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.swccg.base.SWCCGDataReady
import com.hatfat.swccg.results.SWCCGSearchResultsListAdapter
import com.hatfat.swccg.results.SWCCGSearchResultsSwipeAdapter
import com.hatfat.swccg.search.SWCCGCardSearchOptionsProvider
import com.hatfat.swccg.search.SWCCGSearchHandler
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
    abstract fun bindSearchHandler(
        swccgSearchHandler: SWCCGSearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsListAdapter(
        adapter: SWCCGSearchResultsListAdapter
    ): SearchResultsListAdapter

    @Binds
    abstract fun bindSearchResultsSwipeAdapter(
        adapter: SWCCGSearchResultsSwipeAdapter
    ): SearchResultsSwipeAdapter
}
