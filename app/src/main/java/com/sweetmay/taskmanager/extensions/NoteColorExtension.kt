package com.sweetmay.taskmanager.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.sweetmay.taskmanager.R
import com.sweetmay.taskmanager.view.ui.App

inline fun App.Color.getColorInt(context: Context) = ContextCompat.getColor(context, getColorRes())

inline fun App.Color.getColorRes() = when (this) {
    App.Color.WHITE -> R.color.white
    App.Color.VIOLET -> R.color.violet
    App.Color.YELLOW -> R.color.yellow
    App.Color.RED -> R.color.red
    App.Color.PINK -> R.color.pink
    App.Color.GREEN -> R.color.green
}