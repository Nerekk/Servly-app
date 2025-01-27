package com.example.servly_app.features._customer.profile

import android.content.res.Configuration
import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.ui.theme.Typography

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
fun PreviewProfileCard() {
    AppTheme {
        ProfileCard(
            customerAvatar = painterResource(R.drawable.test_square_image_large),
            customerName = "Jan Kowalski",
            customerAddress = "Łódź, Górna",
            customerPhoneNumber = "325532643"
        )
    }
}

@Composable
fun ProfileCard(
    customerAvatar: Painter,
    customerName: String,
    customerAddress: String,
    customerPhoneNumber: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile_customer),
                style = Typography.headlineSmall
            )


            Image(
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp),
                painter = customerAvatar,
                contentDescription = "avatar"
            )

            Text(
                text = customerName,
                style = Typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "icon"
                )

                Text(
                    text = customerAddress,
                    style = Typography.bodyMedium
                )
            }

            Row {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "icon"
                )

                Text(
                    text = PhoneNumberUtils.formatNumber(customerPhoneNumber, "pl"),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}