package com.hatfat.meccg.inject

import com.hatfat.meccg.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object MECCGConfigModule {
    @Provides
    @Named("should use dreamcards")
    fun providesShouldUseDreamCards(): Boolean {
        return true
    }

    @Provides
    @Named("about resource list")
    fun providesAboutAdapterResources(): List<Int> {
        return ArrayList<Int>().apply {
            this.add(R.string.cards_about_desc_line)
            this.add(R.string.cards_about_contact_line)
            this.add(R.string.cards_about_remastered_images)
            this.add(R.string.cards_about_meccg)
            this.add(R.string.cards_about_affiliation)
        }
    }
}
