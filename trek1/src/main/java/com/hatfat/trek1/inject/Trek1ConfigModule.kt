package com.hatfat.trek1.inject

import com.hatfat.trek1.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object Trek1ConfigModule {
    @Provides
    @Named("about resource list")
    fun providesAboutAdapterResources(): List<Int> {
        return ArrayList<Int>().apply {
            this.add(R.string.cards_about_desc_line)
            this.add(R.string.cards_about_contact_line)
            this.add(R.string.cards_about_trek1)
            this.add(R.string.cards_about_affiliation)
        }
    }
}
