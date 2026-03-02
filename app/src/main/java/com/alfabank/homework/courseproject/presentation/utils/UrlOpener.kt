package com.alfabank.homework.courseproject.presentation.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun Context.openUrl(
    url: String?,
    onError: (String) -> Unit = {}
) {
    if (url.isNullOrBlank()) {
        onError("Ссылка отсутствует")
        return
    }

    val fixedUrl = if (!url.startsWith("http")) {
        "https://$url"
    } else url

    val uri = try {
        fixedUrl.toUri()
    } catch (e: Exception) {
        onError("Некорректная ссылка")
        return
    }

    try {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setStartAnimations(
                this,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .setExitAnimations(
                this,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .build()

        customTabsIntent.launchUrl(this, uri)

    } catch (e: Exception) {
        onError("Ошибка открытия ссылки")
    }
}