package ir.tehranshomal.samarium

//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ir.tehranshomal.samarium.logic.CellInfoGetter
import ir.tehranshomal.samarium.ui.theme.SamariumTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SamariumTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val params=getServingCellParameters(this)
//                    val a=params[0]./
//                    Greeting()
                    UpdateTextButton()
                }
            }
        }
    }
}


@Composable
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
fun UpdateTextButton() {
    val context = LocalContext.current
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val phonePermission = rememberPermissionState(
        permission = Manifest.permission.READ_PHONE_STATE
    )
    // Create a mutable state to hold the text
    val text = remember { mutableStateOf("Initial Text") }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            if (!locationPermission.status.isGranted) {
                locationPermission.launchPermissionRequest()
            } else {
                Toast.makeText(context, "Permission Given Already", Toast.LENGTH_LONG).show()
            }
            if (!phonePermission.status.isGranted) {
                phonePermission.launchPermissionRequest()
            } else {
                Toast.makeText(context, "Permission Given Already", Toast.LENGTH_LONG).show()
            }
        }) {
            Text(text = "Ask for permission")
        }
        // Text Composable that displays the current value of 'text'
        Text(text = text.value)

        Spacer(modifier = Modifier.height(16.dp))

        // Button Composable that updates the 'text' when clicked
        Button(
            onClick = {
                if (!locationPermission.status.isGranted) {
                    locationPermission.launchPermissionRequest()
                }
                if (!phonePermission.status.isGranted) {
                    phonePermission.launchPermissionRequest()
                }
                val cellInfoGetter=CellInfoGetter()
                text.value = cellInfoGetter.getServingCellParameters(context).toString()
//                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Text Updated!", Toast.LENGTH_SHORT).show()


//                Toast.makeText(this@MainActivity, "Its a toast!", Toast.LENGTH_SHORT).show()

            }
        ) {
            Text("Update Text")
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SamariumTheme {
        Greeting("Android")
    }
}