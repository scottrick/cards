package com.hatfat.trek2.inject

import com.hatfat.trek2.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object Trek2ConfigModule {
    @Provides
    @Named("about resource list")
    fun providesAboutAdapterResources(): List<Int> {
        return ArrayList<Int>().apply {
            this.add(R.string.cards_about_desc_line)
            this.add(R.string.cards_about_contact_line)
            this.add(R.string.cards_about_trek2)
            this.add(R.string.cards_about_affiliation)
        }
    }
}
