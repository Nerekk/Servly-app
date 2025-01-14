package com.example.servly_app.features.authentication.presentation.user_data_view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
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
fun PreviewProviderFormView() {
    val navController = rememberNavController()
    AppTheme {
        ProviderFormView(navController)
    }
}



@Composable
fun ProviderFormView(navController: NavHostController) {
    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                Column {
                    Text(
                        text = "Wprowadz swoje dane",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(AuthNavItem.ProviderMain.route) {
                                popUpTo(AuthNavItem.Welcome.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Zatwierdź",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
