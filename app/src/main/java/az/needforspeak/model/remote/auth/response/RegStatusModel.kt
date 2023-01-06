package az.needforspeak.model.remote.auth.response

import az.needforspeak.model.remote.auth.InputValueModel

data class RegStatusModel(
    val id: Int?,
    val user_id: String?,
    val phone_number: String?,
    val document_paths: String?,
    val is_approved: Int?,
    val is_otp_verified: Int?,
    val created: String?,
)
