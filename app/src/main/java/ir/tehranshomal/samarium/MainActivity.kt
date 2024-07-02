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
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
//import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

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
fun getServingCellParameters(context: Context): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request permissions if not granted
        return "nadariiii"
    }

    val cellInfo: List<CellInfo>? = telephonyManager.allCellInfo
    var result="initee ${cellInfo?.count()}\n"
    cellInfo?.forEach { info ->
        when (info) {
            is CellInfoGsm -> {
                val cellIdentityGsm = info.cellIdentity
                val cellSignalStrengthGsm = info.cellSignalStrength
                println("GSM Cell:\n")
                result+="GSM Cell:\n"
                println("CID: ${cellIdentityGsm.cid}\n")
                result+="CID: ${cellIdentityGsm.cid}\n"
                println("LAC: ${cellIdentityGsm.lac}\n")
                result+="CID: ${cellIdentityGsm.cid}\n"
                println("MCC: ${cellIdentityGsm.mcc}\n")
                result+="MCC: ${cellIdentityGsm.mcc}\n"
                println("MNC: ${cellIdentityGsm.mnc}\n")
                result+="MNC: ${cellIdentityGsm.mnc}\n"
                println("Signal Strength: ${cellSignalStrengthGsm.dbm} dBm\n")
                result+="Signal Strength: ${cellSignalStrengthGsm.dbm} dBm\n"
            }
            is CellInfoLte -> {
                val cellIdentityLte = info.cellIdentity
                val cellSignalStrengthLte = info.cellSignalStrength
                println("LTE Cell:")
                result+="LTE Cell:"
                println("CI: ${cellIdentityLte.ci}")
                result+="CI: ${cellIdentityLte.ci}"
                println("TAC: ${cellIdentityLte.tac}")
                result+="TAC: ${cellIdentityLte.tac}"
                println("MCC: ${cellIdentityLte.mcc}")
                result+="MCC: ${cellIdentityLte.mcc}"
                println("MNC: ${cellIdentityLte.mnc}")
                result+="MNC: ${cellIdentityLte.mnc}"
                println("PCI: ${cellIdentityLte.pci}")
                result+="PCI: ${cellIdentityLte.pci}"
                println("Signal Strength: ${cellSignalStrengthLte.dbm} dBm")
                result+="Signal Strength: ${cellSignalStrengthLte.dbm} dBm"
            }
            is CellInfoWcdma -> {
                val cellIdentityWcdma = info.cellIdentity
                val cellSignalStrengthWcdma = info.cellSignalStrength
                println("WCDMA Cell:")
                result+="WCDMA Cell:"
                println("CID: ${cellIdentityWcdma.cid}")
                result+=("CID: ${cellIdentityWcdma.cid}")
                println("LAC: ${cellIdentityWcdma.lac}")
                result+=("LAC: ${cellIdentityWcdma.lac}")
                println("MCC: ${cellIdentityWcdma.mcc}")
                result+=("MCC: ${cellIdentityWcdma.mcc}")
                println("MNC: ${cellIdentityWcdma.mnc}")
                result+=("MNC: ${cellIdentityWcdma.mnc}")
                println("PSC: ${cellIdentityWcdma.psc}")
                result+=("PSC: ${cellIdentityWcdma.psc}")
                println("Signal Strength: ${cellSignalStrengthWcdma.dbm} dBm")
                result+=("Signal Strength: ${cellSignalStrengthWcdma.dbm} dBm")
            }
            // Add more cases for other cell types if needed
        }
    }
    return result
//        return cellInfo
}
@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
fun UpdateTextButton() {
    val context = LocalContext.current
    val locationPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
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
        }) {
            Text(text = "Ask for permission")
        }
        // Text Composable that displays the current value of 'text'
        Text(text = text.value)

        Spacer(modifier = Modifier.height(16.dp))

        // Button Composable that updates the 'text' when clicked
        Button(
            onClick = {
                text.value = getServingCellParameters(context)
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