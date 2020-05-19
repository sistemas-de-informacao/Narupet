package dev.edsoncamargo.utils

import android.app.AlertDialog
import android.content.Context

fun alert(title: String, msg: String, context: Context) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(context)
    builder
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("OKAY", null)
        .create()
        .show()
}