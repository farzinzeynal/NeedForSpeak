package az.needforspeak.model.remote.auth.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequestModel (
    @SerializedName("user_id")
    val user_id: String,

    @SerializedName("phone_number")
    val phone_number: String,

    @SerializedName("documents")
    val documentPaths: List<String>,
)