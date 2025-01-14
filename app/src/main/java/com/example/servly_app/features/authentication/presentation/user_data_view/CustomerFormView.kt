package com.example.servly_app.features.authentication.presentation.user_data_view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.core.components.HeaderTitle
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout

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
fun PreviewCustomerFormView() {
    val navController = rememberNavController()
    AppTheme {
        CustomerFormView(navController)
    }
}

@Composable
fun CustomerFormView(navController: NavHostController) {
    var customerName by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                CustomerHeader(customerName, isValid)

                CustomerButton(navController)
            }
        }
    }
}

@Composable
private fun CustomerButton(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Button(
            onClick = {
                navController.navigate(AuthNavItem.CustomerMain.route) {
                    popUpTo(AuthNavItem.Welcome.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.role_save),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun CustomerHeader(customerName: String, isValid: Boolean) {
    var customerName1 = customerName
    Column {
        HeaderTitle(stringResource(R.string.customer_title))

        OutlinedTextField(
            value = customerName1,
            onValueChange = { newValue -> customerName1 = newValue },
            label = { Text("Imie i nazwisko") },
            placeholder = { Text("Imie i nazwisko") },
            isError = !isValid,
            modifier = Modifier
                .fillMaxWidth(),
            supportingText = {
                if (!isValid) {
                    Text(
                        text = "Nieprawidłowe znaki",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )

        OutlinedTextField(
            value = customerName1,
            onValueChange = { newValue -> customerName1 = newValue },
            label = { Text("Adres zamieszkania") },
            placeholder = { Text("Adres") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
