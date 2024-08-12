package com.enlightenment.sportzinteractive.data.repository

import android.util.Log
import com.enlightenment.sportzinteractive.data.model.Player
import com.enlightenment.sportzinteractive.data.model.Team
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

private const val TAG = "LiveMatch"
const val BASE_URL = "https://demo.sportz.io/"

class LiveMatch @Inject constructor(private val retrofit: Retrofit) {

    private var jsonObjects: MutableList<Call<JsonObject>?> = mutableListOf()
    private var allTeams: MutableList<JsonObject> = mutableListOf()
    private var teamsList: MutableMap<Int, Team> = hashMapOf()
    private var counter = 0

    suspend fun populate(): MutableMap<Int, Team> {
        var counter = 0
        coroutineScope {
            while (counter < 2) {
                val json: Call<JsonObject>? = async(Dispatchers.IO) {
                    try {
                        val service = retrofit.create(APIService::class.java)
                        if (counter == 0) service.getFirstMatch() else service.getSecondMatch()
                    } catch (e: Exception) {
                        Log.d(TAG, "populate: ${e.message}")
                        null
                    } finally {
                        counter++
                    }
                }.await()
                jsonObjects.add(json)
            }

            jsonObjects.removeIf { it == null }
            async {
                jsonObjects.forEach { launch { restructure(it!!) } }
            }.await()
        }
        return teamsList
    }

    private suspend fun restructure(json: Call<JsonObject>) {
        withContext(Dispatchers.Default) {
            val matchDetails = json.execute().body()?.entrySet()
            var (time, date, place) = arrayOf("", "", "")
            matchDetails?.forEach { detail ->
                if (detail.key == "Matchdetail") {
                    time = detail.value.asJsonObject.get("Match").asJsonObject.get("Time").asString
                    date = detail.value.asJsonObject.get("Match").asJsonObject.get("Date").asString
                    place = detail.value.asJsonObject.get("Venue").asJsonObject.get("Name").asString
                }
                if (detail.key == "Teams") {
                    val team = detail.value.asJsonObject.entrySet()
                    team.forEach {
                        val teams = it.value.asJsonObject
                        parse(team = teams, time = "$time, $date", venue = place)
                        allTeams.add(teams)
                    }
                }
            }
        }
    }

    private suspend fun parse(team: JsonObject, time: String, venue: String) {
        withContext(Dispatchers.Default) {
            val gson = Gson()
            val players: MutableList<Player> = mutableListOf()
            val playersJson = team["Players"].asJsonObject.entrySet()
            playersJson.forEach {
                players.add(gson.fromJson(it.value, Player::class.java))
            }
            val individual = Team(
                fullName = team["Name_Full"].asString,
                nameShort = team["Name_Short"].asString,
                time = time,
                venue = venue,
                players = players
            )
            teamsList[counter] = individual
            counter++
        }
    }
}