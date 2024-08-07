package com.performance.sportzinteractive.data.repository.remote

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL = "https://demo.sportz.io/"

interface APIService {

    @GET("nzin01312019187360.json")
    fun getFirstMatchDetails(): Call<JsonObject>

    @GET("sapk01222019186652.json")
    fun getSecondMatchDetails(): Call<JsonObject>
}