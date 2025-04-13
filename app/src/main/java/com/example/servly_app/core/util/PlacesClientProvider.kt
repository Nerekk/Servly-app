package com.example.servly_app.core.util

import android.content.Context
import com.example.servly_app.BuildConfig
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesClientProvider {
    private var client: PlacesClient? = null

    fun getClient(context: Context): PlacesClient {
        if (client == null) {
            if (!Places.isInitialized()) {
                Places.initialize(context.applicationContext, BuildConfig.PLACES_API_KEY)
            }
            client = Places.createClient(context.applicationContext)
        }
        return client!!
    }
}