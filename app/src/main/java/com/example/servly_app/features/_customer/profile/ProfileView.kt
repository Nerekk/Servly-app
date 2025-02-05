package com.example.servly_app.features._customer.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.CustomerState
import com.example.servly_app.features._customer.profile.components.ProfileCard

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
    val state = remember { mutableStateOf(CustomerState()) }
    AppTheme {
        ProfileView(state, onEditClick = {})
    }
}

@Composable
fun ProfileView(customerState: State<CustomerState>, onEditClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding()
            .padding(16.dp)
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
            customerPhoneNumber = customerState.value.phoneNumber
        )

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
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}