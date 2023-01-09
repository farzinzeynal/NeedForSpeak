package az.needforspeak.utils

class Endpoints {
    companion object {
        const val LOGIN_ENDPOINT = "login"
        const val GET_PROFILE = "profile/{userId}"
        const val UPDATE_PROFILE = "profile/{userId}/{key}"
        const val REGISTER_REQUEST = "request"
        const val CHECK_REGISTER_STATUS = "request/{requestId}"
        const val SEND_OTP = "request-otp"
        const val VERIFY_OTP = "check-otp"
        const val REGISTER_USER = "register"
    }
}