package com.example.game_app.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game_app.R
import com.example.game_app.adapters.ViewPagerAdapter
import com.example.game_app.databinding.FragmentSplashBinding
import com.example.game_app.utils.viewBinding
import com.example.game_app.viewModels.SplashViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()
    private val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager()
        observeData()
        initListeners()
        viewModel.getIsGame()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isGame.collectLatest {
                updateConfig()
                openScreen(it)
            }
        }
    }

    private fun initListeners() {
        Firebase.remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                viewModel.getIsGame()
            }

            override fun onError(error: FirebaseRemoteConfigException) {}
        })
    }

    private fun updateConfig() {
        Firebase.remoteConfig.fetchAndActivate()
    }

    private fun openScreen(isGame: Boolean) {
        binding.viewpager.currentItem = if (isGame) 0 else 1
    }

    private fun pager() {
        val fragmentList = arrayListOf(
            GameFragment.newInstance(),
            WebViewFragment.newInstance(),
        )
        binding.viewpager.isUserInputEnabled = false
        binding.viewpager.adapter = ViewPagerAdapter(this, fragmentList)

    }
}
