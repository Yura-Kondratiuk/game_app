package com.example.game_app.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _isGame = MutableStateFlow(false)
    val isGame: StateFlow<Boolean> = _isGame

    fun getIsGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 1000
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            _isGame.emit(remoteConfig.getBoolean(KEY))
        }
    }

    companion object {
        const val KEY = "RemoteConfig"
    }
}