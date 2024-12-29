package com.example.servly_app.features.offers

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.servly_app.R

data class ServiceCategory(
    @DrawableRes val categoryImage: Int,
    @StringRes val categoryName: Int
)

val serviceCategories = listOf(
    ServiceCategory(R.drawable.test_square_image, R.string.cat_electrician),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_plumber),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_painter),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_cleaning),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_mechanic),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_installations),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_repairs),
    ServiceCategory(R.drawable.test_square_image, R.string.cat_others),
)