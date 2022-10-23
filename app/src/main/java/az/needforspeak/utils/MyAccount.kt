package az.needforspeak.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


open class MyAccount(
    @SerializedName("certNumber")
    var certNumber: String? = null,

    @SerializedName("lastname")
    var lastname: String? = null,

    @SerializedName("firstname")
    var firstname: String? = null,

    @SerializedName("day_of_birth")
    var dayOfBirth: String? = null,

    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    var additional: Additional? = null
)

data class Additional(
    @SerializedName("user_username")
    var username: String? = null,

    @SerializedName("user_career")
    var career: String? = null,

    @SerializedName("user_about")
    var about: String? = null,

    @SerializedName("user_education")
    var education: String? = null,

    @SerializedName("user_interests")
    var interests: String? = null,

    @SerializedName("user_day_of_birth")
    var dayOfBirth: String? = null,

    @SerializedName("user_martial_status")
    var martialStatus: String? = null,

    @SerializedName("user_photos")
    var userPhotos: String? = null

)


open class UserIfo(
    @SerializedName("value")
    var infoText: String? = null,

    @SerializedName("visible")
    var isVisible: Boolean? = true

) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}


