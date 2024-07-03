package ir.tehranshomal.samarium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ir.tehranshomal.samarium.ui.theme.SamariumTheme
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ir.tehranshomal.samarium.DB.DbHelper
import ir.tehranshomal.samarium.DB.PointDAO
import ir.tehranshomal.samarium.Views.MainScreen
import ir.tehranshomal.samarium.Views.SettingsScreen
import ir.tehranshomal.samarium.logic.PermissionWrapper
import ir.tehranshomal.samarium.logic.PreferenceManager
import ir.tehranshomal.samarium.logic.getServingCellParameters
import ir.tehranshomal.samarium.map.MapView2
import ir.tehranshomal.samarium.model.Point
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {

//    public lateinit var pointDAO:PointDAO
//    public var timeInterval:Int=10
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getPreferences(Context.MODE_PRIVATE))
//        val dbHelper = DbHelper()
//        val db = dbHelper.getDb(this)
//        pointDAO=db.pointDAO()
//        val preferenceManager= PreferenceManager(this)
//        val timeKey="time_interval"
//        timeInterval=preferenceManager.getInt(timeKey,10)


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
//                    MapView2().MapScreen(this)
//                    UpdateTextButton()
                    Screen()
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Screen() {
    val navController = rememberNavController()
    PermissionWrapper { permissionsState ->
        // Your app's main content goes here
        NavHost(navController = navController, startDestination = "map") {
            composable("map") { MainScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
        }
    }
//    NavHost(navController = navController, startDestination = "map") {
//        composable("map") { MainScreen(navController) }
//        composable("settings") { SettingsScreen(navController) }
//    }
}

@Composable
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
fun UpdateTextButton() {
    val context = LocalContext.current
    val mapView=MapView2()
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val phonePermission = rememberPermissionState(
        permission = Manifest.permission.READ_PHONE_STATE
    )
    // Create a mutable state to hold the text
    val text = remember { mutableStateOf("Initial Text") }
    val points = remember {
        mutableStateListOf<Point>().apply{
            addAll(
                listOf(
                Point(1, 101, "432", "10", "43210", "4G", 4, 12345, -70, 2, 51.5074, -0.1278),
                Point(2, 102, "432", "11", "43211", "4G", 4, 12346, -65, 3, 48.8566, 2.3522),
                Point(3, 103, "432", "12", "43212", "4G", 4, 12347, -80, 1, 40.7128, -74.0060)
            ))
        }
    }



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
            points.add(Point(points.last().Id+1, 101, "432", "10", "43210", "4G", 4, 12345, -70, 2, 51.5074+ Math.random()*10, -0.1278+ Math.random()*10),)
//            mapView.addMarkersToMap()
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
                text.value = getServingCellParameters(context).toString()
//                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Text Updated!", Toast.LENGTH_SHORT).show()


//                Toast.makeText(this@MainActivity, "Its a toast!", Toast.LENGTH_SHORT).show()

            }
        ) {
            Text("Update Text")
        }
//        val mapView=MapView()
//        mapView.MyMapScreen(points)

        mapView.MapScreen(context,points)
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Helo $name!",
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