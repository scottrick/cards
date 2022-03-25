package com.hatfat.meccg.inject

import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import com.hatfat.cards.info.InfoScreenDataProvider
import com.hatfat.cards.info.NotImplementedInfoScreenDataProvider
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.meccg.app.MECCGCardsConfig
import com.hatfat.meccg.data.MECCGDataReady
import com.hatfat.meccg.data.providers.MECCGSingleCardScreenDataProvider
import com.hatfat.meccg.results.MECCGSearchResultsListAdapter
import com.hatfat.meccg.results.MECCGSearchResultsSwipeAdapter
import com.hatfat.meccg.search.MECCGCardSearchOptionsProvider
import com.hatfat.meccg.search.MECCGSearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class MECCGModule {

    @Binds
    abstract fun bindDataReady(
        dataReady: MECCGDataReady
    ): DataReady

    @Binds
    abstract fun bindCardsConfig(
        config: MECCGCardsConfig
    ): CardsConfig

    @Binds
    abstract fun bindSingleCardScreenDataProvider(
        dataProvider: MECCGSingleCardScreenDataProvider
    ): SingleCardScreenDataProvider

    @Binds
    abstract fun bindInfoDataProvider(
        dataProvider: NotImplementedInfoScreenDataProvider
    ): InfoScreenDataProvider

    @Binds
    abstract fun bindSearchOptionsProvider(
        meccgSearchOptionsProvider: MECCGCardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        meccgSearchHandler: MECCGSearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsListAdapter(
        adapter: MECCGSearchResultsListAdapter
    ): SearchResultsListAdapter

    @Binds
    abstract fun bindSearchResultsSwipeAdapter(
        adapter: MECCGSearchResultsSwipeAdapter
    ): SearchResultsSwipeAdapter
}