package com.hatfat.cards.inject

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.TypedValue
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.hatfat.cards.R
import com.hatfat.cards.glide.AddCardBorderTransformation
import com.hatfat.cards.glide.CardCornersTransformation
import com.hatfat.cards.glide.CardRotationTransformation
import com.hatfat.cards.glide.CardTransformations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardsConfigModule {
    @Provides
    @Named("CardCornerRadiusInMillimeters")
    fun providesCardCornerRadius(resources: Resources): Float {
        val typedValue = TypedValue()
        resources.getValue(R.dimen.card_corner_radius_in_millimeters, typedValue, true)
        return typedValue.float
    }

    @Provides
    @Named("CardBorderWidthInMillimeters")
    fun providesCardBorderWidthInMillimeters(resources: Resources): Float {
        val typedValue = TypedValue()
        resources.getValue(R.dimen.card_border_width_in_millimeters, typedValue, true)
        return typedValue.float
    }

    @Provides
    @Named("CardBorderColor")
    fun providesCardBorderColor(resources: Resources): Int {
        @Suppress("DEPRECATION")
        return resources.getColor(R.color.card_border_color)
    }

    @Provides
    @Singleton
    @Named("CardImageTransformations")
    fun providesCardTransformations(
        cardCornersTransformation: CardCornersTransformation,
        addCardBorderTransformation: AddCardBorderTransformation,
        resources: Resources,
    ): CardTransformations {
        val transformations = mutableListOf<Transformation<Bitmap>>()
        transformations.add(CardRotationTransformation())
        if (resources.getBoolean(R.bool.add_card_border_to_images)) {
            transformations.add(addCardBorderTransformation)
        }
        transformations.add(FitCenter())
        transformations.add(cardCornersTransformation)

        return CardTransformations(transformations)
    }
}