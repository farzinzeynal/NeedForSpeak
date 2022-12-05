package az.needforspeak.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(var imageUrl: String, var imageMessage: String? = null) : Parcelable