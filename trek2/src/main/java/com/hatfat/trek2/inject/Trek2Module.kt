package com.hatfat.trek2.inject

import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.trek2.data.Trek2DataReady
import com.hatfat.trek2.results.Trek2SearchResultsListAdapter
import com.hatfat.trek2.results.Trek2SearchResultsSwipeAdapter
import com.hatfat.trek2.search.Trek2CardSearchOptionsProvider
import com.hatfat.trek2.search.Trek2SearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class Trek2Module {

    @Binds
    abstract fun bindDataReady(
        dataReady: Trek2DataReady
    ): DataReady

    @Binds
    abstract fun bindSearchOptionsProvider(
        trek2SearchOptionsProvider: Trek2CardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        trek2SearchHandler: Trek2SearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsListAdapter(
        adapter: Trek2SearchResultsListAdapter
    ): SearchResultsListAdapter

    @Binds
    abstract fun bindSearchResultsSwipeAdapter(
        adapter: Trek2SearchResultsSwipeAdapter
    ): SearchResultsSwipeAdapter
}