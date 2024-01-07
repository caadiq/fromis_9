package com.beemer.unofficial.fromis_9.view

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ActivityAlbumPhoto : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumPhotoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val concept = intent.getStringExtra("concept")
        val imageUrl = intent.getStringExtra("imageUrl")

        Glide.with(this).load(imageUrl).placeholder(ColorDrawable(Color.TRANSPARENT)).transition(DrawableTransitionOptions.withCrossFade()).into(binding.imgPhoto)
        concept?.let { binding.txtConcept.text = it }

        binding.imgPhoto.setOnClickListener {
            val options  = ActivityOptions.makeSceneTransitionAnimation(this, binding.imgPhoto, "transitionImagePhoto").toBundle()
            val intent = Intent(this, ActivityAlbumPhotoZoom::class.java).apply {
                putExtra("imageUrl", imageUrl)
            }
            startActivity(intent, options)
        }
    }
}