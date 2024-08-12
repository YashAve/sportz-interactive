package com.enlightenment.sportzinteractive.data.model

import com.google.gson.annotations.SerializedName

/*
 {
 "Name_Full": "India",
            "Name_Short": "IND",
                    "Position": "1",
                    "Name_Full": "Rohit Sharma",
                    "Iscaptain": true,
                    "Batting": {
                        "Style": "RHB",
                        "Average": "47.88",
                        "Strikerate": "88.46",
                        "Runs": "7806"
                    }
                    
                    "Bowling": {
                        "Style": "OB",
                        "Average": "64.37",
                        "Economyrate": "5.21",
                        "Wickets": "8"
                    }
*/

data class Team(
    val fullName: String,
    val nameShort: String,
    val time: String,
    val venue: String,
    val players: List<Player>
)

data class Player(
    @SerializedName("Position") val position: String,
    @SerializedName("Name_Full") val fullName: String,
    @SerializedName("Iscaptain") val isCaptain: Boolean,
    @SerializedName("Batting") val batting: Batting,
    @SerializedName("Bowling") val bowling: Bowling
)

data class Batting(
    @SerializedName("Style") val style: String,
    @SerializedName("Average") val average: String,
    @SerializedName("Strikerate") val strikeRate: String,
    @SerializedName("Runs") val runs: String
)

data class Bowling(
    @SerializedName("Style") val style: String,
    @SerializedName("Average") val average: String,
    @SerializedName("Economyrate") val economyRate: String,
    @SerializedName("Wickets") val wickets: String
)