package com.beemer.unofficial.fromis_9.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenYouTube {
    companion object {
        fun openYoutube(context: Context, id: String) {
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
            try {
                context.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                context.startActivity(intentBrowser)
            }
        }
    }
}