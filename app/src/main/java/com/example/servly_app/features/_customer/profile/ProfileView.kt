package com.example.servly_app.features._customer.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.CustomerState
import com.example.servly_app.features._customer.profile.components.ProfileCard
import com.example.servly_app.features.reviews.presentation.ReviewListView

@Preview(
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightEN"
)
@Preview(
    showBackground = true,
    locale = "pl",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkPL"
)
@Composable
fun PreviewProfileView() {
    val state = remember { mutableStateOf(CustomerState(
        phoneNumber = "+48324125234",
        rating = 4.0
    )) }
    AppTheme {
        ProfileContent(state, {}, false, {})
    }
}

@Composable
fun ProfileView(customerId: Long, onEditClick: (() -> Unit)? = null) {

    val viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.ProviderViewModelFactory> { factory ->
        factory.create(customerId)
    }
    val state = viewModel.customerState.collectAsState()
    val reviewsVisibilityState = viewModel.reviewsVisibilityState.collectAsState()

    ProfileContent(
        state,
        onEditClick,
        reviewsVisibilityState.value,
        updateVisibility = viewModel::changeReviewsVisibility
    )

}

@Composable
private fun ProfileContent(
    customerState: State<CustomerState>,
    onEditClick: (() -> Unit)?,
    reviewsVisible: Boolean,
    updateVisibility: (Boolean) -> Unit
) {
    if (customerState.value.isLoading) {
        LoadingScreen()
        return
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileCard(
                title = stringResource(R.string.profile_customer),
                customerAvatar = painterResource(R.drawable.test_square_image_large),
                customerName = customerState.value.name,
                customerAddress = if (customerState.value.houseNumber != null) {
                    "${customerState.value.city}, ${customerState.value.street}, ${customerState.value.houseNumber}"
                } else {
                    "${customerState.value.city}, ${customerState.value.street}"
                },
                customerPhoneNumber = customerState.value.phoneNumber,
                rating = customerState.value.rating,
                reviewsVisible = reviewsVisible,
                updateVisibility = updateVisibility
            )

            if (reviewsVisible) {
                ReviewListView(customerState.value.customerId!!, Role.CUSTOMER)
            } else {
                onEditClick?.let {
                    ExtendedFloatingActionButton(
                        onClick = { onEditClick() },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "edit"
                            )
                        },
                        text = {
                            Text(stringResource(R.string.profile_button_edit))
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}