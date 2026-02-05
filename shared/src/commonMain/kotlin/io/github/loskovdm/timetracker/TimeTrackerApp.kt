package io.github.loskovdm.timetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.ui.NavDisplay
import io.github.loskovdm.timetracker.navigation.NavigationRoot
import org.jetbrains.compose.resources.painterResource
import timetracker.shared.generated.resources.timer
import timetracker.shared.generated.resources.Res
import timetracker.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun TimeTrackerApp() {
    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//        Scaffold(
//            bottomBar = { TimeTrackerBottomBar() }
//        ) { innerPadding ->
//            NavDisplay(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                onBack = ,
//                entries = ,
//            )
//        }
        NavigationRoot()
    }
}