package com.beemer.unofficial.fromis_9.view

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumPhotoZoomBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityAlbumPhotoZoom : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumPhotoZoomBinding.inflate(layoutInflater) }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            closeActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val imageUrl = intent.getStringExtra("imageUrl")
        supportPostponeEnterTransition()
        binding.imgPhoto.transitionWithGlide(imageUrl, ::supportStartPostponedEnterTransition)

        binding.btnClose.setOnClickListener { closeActivity() }
        binding.btnDownload.setOnClickListener {
            imageUrl?.let { downloadImage(it) }
        }
    }

    private fun ImageView.transitionWithGlide(url: String?, onLoadingFinished: () -> Unit = {}) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }
        }

        Glide.with(this).load(url).apply(RequestOptions().dontTransform().placeholder(ColorDrawable(Color.TRANSPARENT))).listener(listener).into(this)
    }

    private fun closeActivity() {
        binding.imgPhoto.resetZoom()
        supportFinishAfterTransition()
    }

    private fun downloadImage(url: String) {
        val fileName = createFileName(url)
        val mimeType = determineMimeType(url)

        try {
            enqueueDownload(url, fileName, mimeType)
        } catch (e: Exception) {
            Toast.makeText(this, "다운로드 오류: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    // 현재 날짜 및 시간으로 파일 이름 설정
    private fun createFileName(url: String): String {
        val format = Uri.parse(url).getQueryParameter("format")?.lowercase() ?: "jpg"
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "$date.$format"
    }

    // 파일 확장자 설정
    private fun determineMimeType(url: String): String = when (Uri.parse(url).getQueryParameter("format")?.lowercase()) {
        "jpg" -> "image/jpeg"
        "png" -> "image/png"
        else -> "image/jpeg"
    }

    // 다운로드 매니저로 이미지 다운로드
    private fun enqueueDownload(url: String, fileName: String, mimeType: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("이미지 다운로드 중...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setMimeType(mimeType)
        downloadManager.enqueue(request)
    }
}