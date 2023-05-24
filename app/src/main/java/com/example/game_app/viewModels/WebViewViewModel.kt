package com.example.game_app.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Collections.emptyList


class WebViewViewModel : ViewModel() {

    private val database = Firebase.database("https://gameapp-50b6c-default-rtdb.firebaseio.com")
    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url

     fun getUrls() {
         viewModelScope.launch(Dispatchers.IO) {
             database.getReference("url")
                 .addValueEventListener(
                     object : ValueEventListener {
                         override fun onDataChange(dataSnapshot: DataSnapshot) {
                             dataSnapshot.getValue<String>()?.let {
                                 _url.value = it
                             }
                         }

                         override fun onCancelled(error: DatabaseError) {
                             Log.w(TAG, "Failed to read value.", error.toException())
                         }
                     }
                 )
         }
    }
}