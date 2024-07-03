package ir.tehranshomal.samarium.logic

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionWrapper(
    permissions: List<String> = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
    ),
    content: @Composable (MultiplePermissionsState) -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)

    when {
        permissionsState.allPermissionsGranted -> {
            content(permissionsState)
        }
        else -> {
            PermissionRequest(permissionsState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(permissionsState: MultiplePermissionsState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("The app needs location permission to function properly")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
            Text("Grant permissions")
        }
    }
}