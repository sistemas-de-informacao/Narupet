package dev.edsoncamargo.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import dev.edsoncamargo.R

fun alert(title: String, msg: String, context: Context) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(context)
    builder
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("OKAY", null)
        .create()
        .show()
}

fun progress(context: Context, layoutInflate: LayoutInflater): AlertDialog {
    val builder = AlertDialog.Builder(context)
    val view = layoutInflate.inflate(R.layout.progress, null)
    builder.setView(view)
    builder.setCancelable(false)
    val dialog = builder.create()
    dialog.show()
    return dialog
}