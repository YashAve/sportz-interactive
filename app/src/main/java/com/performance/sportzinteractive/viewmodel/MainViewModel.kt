package com.performance.sportzinteractive.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.performance.sportzinteractive.data.repository.remote.APIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(private val retrofit: Retrofit) : ViewModel() {

    private lateinit var service: APIService
    private var json: Response<JsonObject>? = null

    suspend fun create() {
        withContext(Dispatchers.IO) {
            service = retrofit.create(APIService::class.java)
            json = try {
                service.getFirstMatchDetails().execute()
            } catch (e: Exception) {
                Log.d(TAG, "create: ${e.message}")
                null
            }
        }
    }

    suspend fun getData() =
        viewModelScope.async {
            create()
        }.await()
}