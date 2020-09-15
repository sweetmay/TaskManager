package com.sweetmay.taskmanager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note (var title: String, var text: String, var date: Date, var id: String) : Parcelable {
}