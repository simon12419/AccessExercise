package com.example.accesstest.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

object JumpUtil {

    /**
     * 開啟外部瀏覽器
     *
     * @param context:
     * @param dnbUrl:  要跳轉的 url
     */
    fun toExternalWeb(context: Context, dnbUrl: String?) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dnbUrl?.httpFormat()))
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "address error", Toast.LENGTH_LONG)
        }
    }

    //20210401 記錄問題：retrofit.setBaseUrl() format http://[isNotEmpty]/ or https://[isNotEmpty]/，否則會直接 exception
    fun String.httpFormat(): String {
        val regex = "^http[s]?://.+".toRegex()
        return this.let {
            if (it.isEmpty()) "https://default/" else it
        }.let {
            if (it.contains(regex)) it else "https://$it"
        }.let {
            if (it.endsWith("/")) it else "$it/"
        }
    }
}