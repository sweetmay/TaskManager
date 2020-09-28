package com.sweetmay.taskmanager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(var title: String = "", var text: String = "", var date: Date = Date(), var id: String = "", var color: Color = Color.WHITE) : Parcelable, Comparable<Note>{

    override fun compareTo(other: Note): Int {
        return other.date.compareTo(date)
    }

    enum class Color {
        WHITE,
        VIOLET,
        YELLOW,
        RED,
        PINK,
        GREEN
    }

}