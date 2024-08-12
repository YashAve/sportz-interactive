package com.enlightenment.sportzinteractive.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enlightenment.sportzinteractive.R
import com.enlightenment.sportzinteractive.data.model.Player
import com.enlightenment.sportzinteractive.data.model.Team
import com.enlightenment.sportzinteractive.ui.composable.DynamicTabSelector
import com.enlightenment.sportzinteractive.ui.composable.Loader
import com.enlightenment.sportzinteractive.ui.font.GoogleFonts.shadowsIntoLightFamily
import com.enlightenment.sportzinteractive.ui.theme.SportzInteractiveTheme
import com.enlightenment.sportzinteractive.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
private lateinit var onFinish: () -> Unit
private var teams: MutableMap<Int, Team> = mutableMapOf()
private var first: Team? = null
private var second: Team? = null

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        onFinish = { finish() }

        setContent {
            SportzInteractiveTheme {
                Main(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun Main(modifier: Modifier = Modifier, viewModel: MainViewModel? = null) {

    val context = LocalContext.current
    var available by rememberSaveable { mutableStateOf(false) }
    var selection by rememberSaveable { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            Box(
                modifier = modifier
                    .padding(23.dp)
                    .statusBarsPadding()
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "exit app",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { if (selection) selection = false else onFinish() }
                )
                Text(
                    text = context.resources.getString(R.string.app_name),
                    style = TextStyle(
                        fontFamily = shadowsIntoLightFamily,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }) { scaffold ->
            Box(
                modifier = modifier
                    .padding(scaffold)
                    .fillMaxSize()
            ) {
                if (!available) {
                    Loader(modifier = Modifier.align(Alignment.Center)) {
                        available = it
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(23.dp)
                    ) {
                        if (!selection) {
                            if (viewModel?.teams?.isNotEmpty() == true) {
                                MatchCard(viewModel.teams[0], viewModel.teams[1]) {
                                    selection = it
                                }
                                MatchCard(viewModel.teams[2], viewModel.teams[3]) {
                                    selection = it
                                }
                            }
                        } else {
                            MatchScreen(team = first, modifier = Modifier.weight(0.5f))
                            MatchScreen(team = second, modifier = Modifier.weight(0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(vararg team: Team?, onSelect: (Boolean) -> Unit = {}) {
    OutlinedCard(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(modifier = Modifier.padding(23.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team[0]?.fullName ?: "India")
                    Text(
                        text = "vs",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = shadowsIntoLightFamily
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    Text(text = team[1]?.fullName ?: "New Zealand")
                }
                Log.d(TAG, "MatchCard: ${team[0]}")
                Text(text = "Time: ${team[0]?.time}")
                Text(text = "Venue: ${team[1]?.venue}")
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                first = team[0]
                second = team[1]
                onSelect(true)
            }) {
                Text(text = "View more")
                Icon(imageVector = Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "")
            }
        }
    }
}

@Composable
fun MatchScreen(modifier: Modifier = Modifier, team: Team? = null) {
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(23.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = team?.fullName?.uppercase() ?: "India",
                    textAlign = TextAlign.Center,
                    fontFamily = shadowsIntoLightFamily,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            team?.players.let {
                LazyColumn {
                    items(it?.sortedBy { !it.isCaptain } ?: listOf()) { player ->
                        Player(player = player)
                    }
                }
            }
        }
    }
}

@Composable
private fun Player(modifier: Modifier = Modifier, player: Player? = null) {
    var show by rememberSaveable { mutableStateOf(false) }
    OutlinedCard(modifier = modifier.padding(7.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${if (player?.isCaptain == true) "Captain " else ""}${player?.fullName}",
                    fontWeight = if (player?.isCaptain == true
                    ) FontWeight.Bold else FontWeight.Normal
                )
                Text(text = "Position: ${player?.position}")
            }
            Row(modifier = Modifier
                .clickable { show = true }
                .padding(start = 3.dp)) {
                Text(text = "View more")
                Icon(imageVector = Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "")
            }
        }
    }
    if (show) {
        PlayerMeta(player = player) {
            show = it
        }
    }
}

@Composable
private fun PlayerMeta(
    modifier: Modifier = Modifier,
    player: Player? = null,
    onDismiss: (Boolean) -> Unit = {}
) {
    var selection by rememberSaveable { mutableIntStateOf(0) }
    AlertDialog(onDismissRequest = { onDismiss(false) }, confirmButton = { }, title = {
        Text(
            text = player?.fullName ?: "Rohit Sharma",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            textAlign = TextAlign.Center
        )
    }, text = {
        Column(modifier = Modifier.fillMaxWidth()) {
            DynamicTabSelector(
                tabs = arrayListOf("Batting", "Bowling"),
                selectedOption = selection,
                onTabSelected = {
                    selection = it
                })
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = if (selection == 0) "Style: ${player?.batting?.style}" else "Style: ${player?.bowling?.style}")
                Text(text = if (selection == 0) "Average: ${player?.batting?.average}" else "Average: ${player?.bowling?.average}")
                Text(text = if (selection == 0) "Strike rate: ${player?.batting?.strikeRate}" else "Economy rate: ${player?.bowling?.economyRate}")
                Text(text = if (selection == 0) "Runs: ${player?.batting?.runs}" else "Wickets: ${player?.bowling?.wickets}")
            }
        }
    })
}

@Preview(showBackground = true)
@Composable
private fun MatchCardPreview() {
    MatchCard(null, null)
}

@Preview(showBackground = true)
@Composable
private fun MatchScreenPreview() {
    MatchScreen()
}

@Preview(showBackground = true)
@Composable
private fun PlayerPreview() {
    Player()
}

@Preview(showBackground = true)
@Composable
private fun PlayerMetaPreview() {
    PlayerMeta()
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    SportzInteractiveTheme {
        Main()
    }
}