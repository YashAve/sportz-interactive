package com.enlightenment.sportzinteractive.data.repository

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("nzin01312019187360.json")
    fun getFirstMatch(): Call<JsonObject>

    @GET("sapk01222019186652.json")
    fun getSecondMatch(): Call<JsonObject>
}