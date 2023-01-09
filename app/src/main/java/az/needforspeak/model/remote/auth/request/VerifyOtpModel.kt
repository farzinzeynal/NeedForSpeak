package az.needforspeak.model.remote.auth.request

data class VerifyOtpModel (
    val request_id: Int,
    val otp_code: Int,
)