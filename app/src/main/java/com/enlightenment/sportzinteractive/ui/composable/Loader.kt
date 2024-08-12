package com.enlightenment.sportzinteractive.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enlightenment.sportzinteractive.ui.theme.DarkGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    task: String = "Loading",
    onLoading: (Boolean) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var loadingMessage by rememberSaveable { mutableStateOf("") }
    var counter by rememberSaveable { mutableIntStateOf(0) }
    var count by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        scope.launch {
            while (count < 2) {
                while (counter < 3) {
                    loadingMessage = "${".".repeat(counter)}$task"
                    delay(500)
                    counter++
                }
                counter = 0
                count++
            }
            onLoading(true)
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen,
            contentColor = Color.White
        ), modifier = modifier
    ) {
        Text(
            text = loadingMessage,
            fontSize = 17.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
    }
}