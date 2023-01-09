package az.needforspeak.model.remote.auth.request

import com.google.gson.annotations.SerializedName

data class RegisteNewUserModel (
    val request_id: Int,
    val firstname: String,
    val lastname: String,
    val password: String,
)