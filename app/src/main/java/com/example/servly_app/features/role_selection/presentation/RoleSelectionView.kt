package com.example.servly_app.features.role_selection.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role


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
fun PreviewRoleSelectionView() {
    val navController = rememberNavController()
    AppTheme {
        RoleSelectionContent(
            navController = navController,
            onCustomerSelect = {},
            onProviderSelect = {}
        )
    }
}

@Composable
fun RoleSelectionView(
    navController: NavHostController,
    onCustomerSelect: (Role) -> Unit,
    onProviderSelect: (Role) -> Unit
) {
    val viewModel: RoleSelectionViewModel = hiltViewModel()
    val state = viewModel.roleState.collectAsStateWithLifecycle()

    Log.i("Navigation", "RoleSelectionView")

    if (state.value.role == null) {
        LoadingScreen()
    } else {
        when (state.value.role) {
            Role.BOTH, Role.NONE -> {
                RoleSelectionContent(
                    navController = navController,
                    onCustomerSelect = {
                        onCustomerSelect(state.value.role!!)
                    },
                    onProviderSelect = {
                        onProviderSelect(state.value.role!!)
                    }
                )
            }
            Role.CUSTOMER -> onCustomerSelect(state.value.role!!)
            Role.PROVIDER -> onProviderSelect(state.value.role!!)
            null -> {}
        }
    }
}

@Composable
private fun RoleSelectionContent(
    navController: NavHostController,
    onCustomerSelect: () -> Unit,
    onProviderSelect: () -> Unit
) {
    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HeaderTitle(stringResource(R.string.role_question))

                    Image(
                        painter = painterResource(R.drawable.groups_24px),
                        contentDescription = "some image",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(160.dp)
                            .padding(top = 16.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = stringResource(R.string.role_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(top = 32.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onCustomerSelect()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.role_customer),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            onProviderSelect()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.role_service),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}


