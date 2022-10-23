package az.needforspeak.model.local


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class PostModel(
    var title: String?,
    var description: String?,
    var date: String?,
    var plate: String?,
    var image: String?,
    var comments: @RawValue List<CommentsModel>?,
    var isSaved: Boolean = false
): Parcelable