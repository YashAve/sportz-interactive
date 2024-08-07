package com.performance.sportzinteractive.data.model

import com.google.gson.annotations.SerializedName

data class Team (
    @SerializedName("Name_Full") val teamFullName: String,
    @SerializedName("Name_Short") val teamShortName: String,
    @SerializedName("Players") val players: MutableList<Player> = mutableListOf()
)

data class Player(
    @SerializedName("Batting") val batting: Batting,
    @SerializedName("Bowling") val bowling: Bowling,
    @SerializedName("Iskeeper") val isKeeper: Boolean,
    @SerializedName("Name_Full") val nameFull: String,
    @SerializedName("Position") val position: String
)

data class Batting(
    @SerializedName("Average") val average: String,
    @SerializedName("Runs") val runs: String,
    @SerializedName("Strikerate") val strikerate: String,
    @SerializedName("Style") val style: String
)

data class Bowling(
    @SerializedName("Average") val average: String,
    @SerializedName("Economyrate") val economyrate: String,
    @SerializedName("Style") val style: String,
    @SerializedName("Wickets") val wickets: String
)
