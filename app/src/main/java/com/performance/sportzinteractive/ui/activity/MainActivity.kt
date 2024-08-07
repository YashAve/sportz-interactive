package com.performance.sportzinteractive.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.performance.sportzinteractive.ui.GoogleFonts
import com.performance.sportzinteractive.ui.theme.SportzInteractiveTheme
import com.performance.sportzinteractive.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportzInteractiveTheme {
                Main(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun Main(modifier: Modifier = Modifier, viewModel: MainViewModel? = null) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            viewModel?.create()
        }
    }

    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Scaffold(modifier = modifier.fillMaxSize()) { scaffold ->
            Box(modifier = Modifier.padding(scaffold)) {
                Column(
                    modifier = Modifier
                        .padding(scaffold)
                        .fillMaxSize()
                ) {
                    TeamCard(modifier = Modifier.weight(0.5f), team = "India")
                    TeamCard(team = "New Zealand", modifier = Modifier.weight(0.5f))
                }
                Text(
                    text = "VS",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 90.sp, fontFamily = GoogleFonts.kalamFamily),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCard(modifier: Modifier = Modifier, team: String = "India") {
    ElevatedCard(
        onClick = { }, modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Column {
            Text(text = team)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    Main()
}