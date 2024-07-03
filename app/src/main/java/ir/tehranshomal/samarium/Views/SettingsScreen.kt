package ir.tehranshomal.samarium.Views
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.tehranshomal.samarium.DB.DbHelper
import ir.tehranshomal.samarium.logic.PreferenceManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }

    var timeInterval by remember { mutableStateOf(preferenceManager.getInt("time_interval", 10).toString()) }
    var showSaveConfirmation by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Time Interval (seconds)", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = timeInterval,
                onValueChange = { timeInterval = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val intervalValue = timeInterval.toIntOrNull() ?: 10
                    preferenceManager.setInt("time_interval", intervalValue)
                    showSaveConfirmation = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }

            if (showSaveConfirmation) {
                AlertDialog(
                    onDismissRequest = { showSaveConfirmation = false },
                    title = { Text("Settings Saved") },
                    text = { Text("The time interval has been updated.") },
                    confirmButton = {
                        Button(onClick = { showSaveConfirmation = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showDeleteConfirmation = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Database")
            }


            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Clear Database") },
                    text = { Text("Are you sure you want to clear database? This action cannot be undone.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                DbHelper().getDb(context).pointDAO().deleteAll()
                                showDeleteConfirmation = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteConfirmation = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

        }
    }
}