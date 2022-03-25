package com.hatfat.swccg.inject

import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import com.hatfat.cards.info.InfoScreenDataProvider
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.swccg.app.SWCCGCardsConfig
import com.hatfat.swccg.data.SWCCGDataReady
import com.hatfat.swccg.data.providers.SWCCGInfoScreenDataProvider
import com.hatfat.swccg.data.providers.SWCCGSingleCardScreenDataProvider
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
    abstract fun bindCardsConfig(
        config: SWCCGCardsConfig
    ): CardsConfig

    @Binds
    abstract fun bindSingleCardScreenDataProvider(
        dataProvider: SWCCGSingleCardScreenDataProvider
    ): SingleCardScreenDataProvider

    @Binds
    abstract fun bindsInfoScreenDataProvider(
        dataProvider: SWCCGInfoScreenDataProvider
    ): InfoScreenDataProvider

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
