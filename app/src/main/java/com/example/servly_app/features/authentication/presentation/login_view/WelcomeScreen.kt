package com.example.servly_app.features.authentication.presentation.login_view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
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
fun PreviewWelcomeScreen() {
    val navController = rememberNavController()
    AppTheme {
        WelcomeScreen(navController)
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    BasicScreenLayout {
        ArrangedColumn {
            WelcomeHeader()
            WelcomeButton(navController)
        }
    }
}

@Composable
private fun WelcomeButton(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        OutlinedButton(
            onClick = {
                navController.navigate(AuthNavItem.Login.route)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.app_login),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.test_square_image_large),
                contentDescription = "logo"
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        Text(
            text = stringResource(R.string.app_description),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 32.dp)
        )
    }
}