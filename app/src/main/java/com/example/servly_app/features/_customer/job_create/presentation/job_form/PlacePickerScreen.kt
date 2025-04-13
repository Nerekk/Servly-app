package com.example.servly_app.features._customer.job_create.presentation.job_form

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.util.PlacesClientProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

@Composable
fun PlacePickerScreen(
    onPlaceSelected: (String, LatLng) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { PlacesClientProvider.getClient(context) }

    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)

    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { newValue ->
                query = newValue
                if (newValue.isNotEmpty()) {
                    val request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(newValue)
                        .setTypesFilter(listOf(PlaceTypes.GEOCODE))
                        .setCountries(listOf("PL"))
                        .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                            predictions = response.autocompletePredictions
                        }
                        .addOnFailureListener {
                            predictions = emptyList()
                        }
                } else {
                    predictions = emptyList()
                }
            },
            label = { Text(stringResource(R.string.write_address)) },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(predictions) { prediction ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                fetchPlaceDetails(
                                    placesClient,
                                    prediction.placeId
                                ) { address, latLng ->
                                    onPlaceSelected(address, latLng) // Zwraca adres i zamyka ekran
                                    onDismiss()
                                }
                            }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = prediction.getPrimaryText(null).toString()
                        )
                        prediction.getSecondaryText(null).let { secondary ->
                            Text(
                                text = secondary.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun fetchPlaceDetails(
    placesClient: PlacesClient,
    placeId: String,
    onDetailsFetched: (String, LatLng) -> Unit
) {
    val request = FetchPlaceRequest.newInstance(
        placeId, listOf(Place.Field.LOCATION, Place.Field.DISPLAY_NAME, Place.Field.FORMATTED_ADDRESS)
    )

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            val place = response.place
            if (place.location != null && place.formattedAddress != null) {
                onDetailsFetched(place.formattedAddress!!, place.location!!)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Błąd pobierania miejsca: ${exception.message}")
        }
}
