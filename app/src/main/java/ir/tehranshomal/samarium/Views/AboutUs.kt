package ir.tehranshomal.samarium.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsPage(navController: NavHostController) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us") },
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About Samarium",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Samarium is a cutting-edge network quality measurement application for Android devices. Our mission is to empower users with real-time insights into cellular network performance while on the move.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Key Features:",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        BulletPoint("Real-time tracking of network quality metrics")
        BulletPoint("Support for 4G and 3G networks")
        BulletPoint("Detailed cell information extraction")
        BulletPoint("Visual representation of signal strength on maps")
        BulletPoint("Intelligent location estimation in areas with poor GPS coverage")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Developed with love using Kotlin and Jetpack Compose, Samarium leverages the latest Android technologies to provide a smooth and intuitive user experience.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Developers:",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinkedText("Mohammad Sadegh Poulaei","https://t.me/Sadegh369",uriHandler)

        Spacer(modifier = Modifier.height(4.dp))

        LinkedText("Sobhan Kazemi","https://t.me/sobi_kazemi",uriHandler)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "For more information, visit our GitHub repository or contact our support team.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinkedText("https://github.com/MSPoulaei/Samarium/","https://github.com/MSPoulaei/Samarium/",uriHandler,true)
    }
        }
}

@Composable
private fun LinkedText(text:String, link:String, uriHandler: UriHandler, mediumFont:Boolean=false) {
    val annotatedSobhanString = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = link
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontStyle = if (mediumFont) MaterialTheme.typography.bodyMedium.fontStyle else MaterialTheme.typography.bodyLarge.fontStyle,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(text)
        }
        pop()
    }
    ClickableText(
        text = annotatedSobhanString,
        onClick = { offset ->
            annotatedSobhanString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}