package az.needforspeak.model.remote.auth

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

    @SerializedName("name")
    val name: InputValueModel?,

    @SerializedName("surname")
    val surname: InputValueModel?,

    @SerializedName("profile-pictures")
    val profile_pictures: ArrayList<String>,

)
