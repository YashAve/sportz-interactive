package com.enlightenment.sportzinteractive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enlightenment.sportzinteractive.data.model.Team
import com.enlightenment.sportzinteractive.data.repository.LiveMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val liveMatch: LiveMatch) : ViewModel() {

    var teams: MutableMap<Int, Team> = mutableMapOf()

    init {
        viewModelScope.launch {
            teams = liveMatch.populate()
        }
    }
}