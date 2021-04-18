package com.hatfat.trek1.inject

import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.trek1.data.Trek1DataReady
import com.hatfat.trek1.results.Trek1SearchResultsListAdapter
import com.hatfat.trek1.results.Trek1SearchResultsSwipeAdapter
import com.hatfat.trek1.search.Trek1CardSearchOptionsProvider
import com.hatfat.trek1.search.Trek1SearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class Trek1Module {

    @Binds
    abstract fun bindDataReady(
        dataReady: Trek1DataReady
    ): DataReady

    @Binds
    abstract fun bindSearchOptionsProvider(
        trek1SearchOptionsProvider: Trek1CardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        trek1SearchHandler: Trek1SearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsListAdapter(
        adapter: Trek1SearchResultsListAdapter
    ): SearchResultsListAdapter

    @Binds
    abstract fun bindSearchResultsSwipeAdapter(
        adapter: Trek1SearchResultsSwipeAdapter
    ): SearchResultsSwipeAdapter
}