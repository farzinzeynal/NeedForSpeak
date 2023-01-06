package az.needforspeak.model.remote.auth.response

import az.needforspeak.model.remote.auth.InputValueModel
import com.google.gson.annotations.SerializedName

data class ProfileResponseModel(
    @SerializedName("name")
    val name: InputValueModel?,

    @SerializedName("surname")
    val surname: InputValueModel?,

    @SerializedName("profile-pictures")
    val profile_pictures: ArrayList<String>,

    @SerializedName("phone-number")
    val phone_number: InputValueModel?,

    @SerializedName("interests")
    val interests: InputValueModel?,

    @SerializedName("career")
    val career: InputValueModel?,

    @SerializedName("about")
    val about: InputValueModel?,

    @SerializedName("education")
    val education: InputValueModel?
)
