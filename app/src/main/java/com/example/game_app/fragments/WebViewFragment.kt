package com.example.game_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game_app.R
import com.example.game_app.databinding.FragmentWebviewBinding
import com.example.game_app.utils.viewBinding
import com.example.game_app.viewModels.WebViewViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class WebViewFragment() : Fragment(R.layout.fragment_webview) {

    private val viewModel: WebViewViewModel by viewModels()

    private val binding by viewBinding(FragmentWebviewBinding::bind)

    private val client: WebViewClient by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        initWebViewSettings()
        initListeners()
        viewModel.getUrls()
        with(binding) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
            webView.webViewClient = client
            webView.canGoBack()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.url.collectLatest { url ->
                binding.webView.loadUrl(url)

            }
        }
    }

    private fun initListeners() {
        binding.webView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK
                && event.action == MotionEvent.ACTION_UP
                && binding.webView.canGoBack()
            ) {
                binding.webView.goBack()
                return@OnKeyListener true
            }
            false
        })
    }
    @SuppressLint("SetJavaScriptEnabled")
    private  fun initWebViewSettings() {
       binding.webView.settings.javaScriptEnabled = true
       binding.webView.settings.allowContentAccess = true
       binding.webView.settings.domStorageEnabled = true
       binding.webView.settings.useWideViewPort = true
       binding.webView.settings.displayZoomControls = true
    }

    companion object{
        fun newInstance() = WebViewFragment()
    }
}


