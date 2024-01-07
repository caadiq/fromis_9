package com.beemer.unofficial.fromis_9.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityYoutubeIframeBinding

interface FullscreenListener {
    fun onFullscreen(enable: Boolean)
}

class ActivityYouTubeIframe : AppCompatActivity(), FullscreenListener {
    private val binding by lazy { ActivityYoutubeIframeBinding.inflate(layoutInflater) }
    private val fullScreenWebChromeClient by lazy { FullscreenWebChromeClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        binding.txtTitle.text = title

        setupWebView()
    }


    private fun setupWebView() {
        val videoId = intent.getStringExtra("videoId")
        val iframeUrl = "https://www.youtube.com/embed/$videoId?enablejsapi=1&autoplay=0"

        configureWebView()
        setupWebViewClient(videoId)
        binding.webView.loadUrl(iframeUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
            }
            webChromeClient = fullScreenWebChromeClient
        }
    }

    private fun setupWebViewClient(videoId: String?) {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request?.url?.host != "www.youtube.com" && request?.url?.host != "m.youtube.com") {
                    view?.loadUrl(request?.url.toString())
                    return true
                }

                val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
                try {
                    startActivity(intentApp)
                } catch (ex: ActivityNotFoundException) {
                    startActivity(intentBrowser)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val script = """
                    var tag = document.createElement('script');
                    tag.src = 'https://www.youtube.com/iframe_api';
                    var firstScriptTag = document.getElementsByTagName('script')[0];
                    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
                """
                view?.evaluateJavascript(script, null)
                binding.progressIndicator.hide()
            }
        }
    }

    override fun onFullscreen(enable: Boolean) {
        if (enable) {
            enterFullscreenMode()
        } else {
            exitFullscreenMode()
        }
    }

    @Suppress("DEPRECATION")
    private fun enterFullscreenMode() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)

            val controller = window.insetsController
            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SourceLockedOrientationActivity")
    private fun exitFullscreenMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)

            val controller = window.insetsController
            controller?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        binding.webView.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        binding.webView.destroy()
        super.onDestroy()
    }

    private inner class FullscreenWebChromeClient(private val listener: FullscreenListener) : WebChromeClient() {
        private var customView: View? = null
        private var customViewCallback: CustomViewCallback? = null

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            super.onShowCustomView(view, callback)
            if (customView != null) {
                return
            }

            customView = view
            customView?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            binding.layout.addView(customView)
            binding.layout.visibility = View.VISIBLE
            customViewCallback = callback

            listener.onFullscreen(true)
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            if (customView == null) {
                return
            }

            binding.layout.removeView(customView)
            binding.layout.visibility = View.GONE
            customViewCallback?.onCustomViewHidden()

            customView = null
            listener.onFullscreen(false)
        }
    }
}