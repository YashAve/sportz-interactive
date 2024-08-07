package com.performance.sportzinteractive.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.performance.sportzinteractive.data.model.Match
import com.performance.sportzinteractive.data.model.Player
import com.performance.sportzinteractive.data.model.Team
import com.performance.sportzinteractive.data.repository.remote.APIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(private val retrofit: Retrofit) : ViewModel() {

    private lateinit var service: APIService
    private var cricketTeams: MutableList<Team> = mutableListOf()
    private var matches: MutableList<Match> = mutableListOf()

    private suspend fun populate(selection: Int = 0) {
        val json = try {
            val response =
                if (selection == 0) service.getFirstMatchDetails() else service.getSecondMatchDetails()
            response.execute()
        } catch (e: Exception) {
            Log.d(TAG, "create: ${e.message}")
            null
        }
        Log.d(TAG, "create: ${json?.body()}")
        restructure(json)
    }

    private suspend fun create() {
        withContext(Dispatchers.IO) {
            service = retrofit.create(APIService::class.java)
            launch { populate(selection = 0) }
            launch { populate(selection = 1) }
        }
    }

    private suspend fun restructure(json: Response<JsonObject>?) =
        withContext(Dispatchers.Default) {
            val gson = Gson()

            var fullName = ""
            var shortName = ""
            val players: MutableList<Player> = mutableListOf()
            var date = ""
            var time = ""
            var venue = ""

            json?.body()?.entrySet()?.forEach {
                var key = it.key
                var value = it.value
                Log.d(TAG, "restructure: $key")

                when (key) {
                    "MatchDetail" -> {
                        date = value.asJsonObject.get("Match").asJsonObject.get("Date").asString ?: ""
                        time = value.asJsonObject.get("Match").asJsonObject.get("Time").asString ?: ""
                        venue = value.asJsonObject.get("Venue").asJsonObject.get("Name").asString ?: ""
                    }
                    "Teams" -> {
                        value.asJsonObject.entrySet().forEach { inner ->
                            key = inner.key
                            value = inner.value
                            Log.d(TAG, "restructure: main key = $key")
                            val values = value.asJsonObject
                            values.entrySet().forEach { teams ->
                                when (teams.key) {
                                    "Name_full" -> {
                                        fullName = teams.value.asString
                                    }

                                    "Name_Short" -> {
                                        shortName = teams.value.asString
                                    }

                                    "Players" -> {
                                        val output = teams.value.asJsonObject
                                        output.entrySet().forEach { player ->
                                            val playerValue = gson.fromJson(
                                                player.value.asJsonObject,
                                                Player::class.java
                                            )
                                            Log.d(TAG, "restructure: player = $playerValue")
                                            players.add(playerValue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                cricketTeams.add(Team(
                    teamFullName = fullName,
                    teamShortName = shortName,
                    players = players
                ))
            }
            matches.add(Match(first = cricketTeams[0], second = cricketTeams[1], date = date, time = time, venue = venue))
        }

    suspend fun getData() {
        viewModelScope.async {
            create()
        }.await()
        Log.d(TAG, "getData: teams size = ${cricketTeams.size}")
        Log.d(TAG, "getData: matches size = ${matches.size}")
    }
}