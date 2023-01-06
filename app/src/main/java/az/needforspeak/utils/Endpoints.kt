package az.needforspeak.utils

class Endpoints {
    companion object {
        const val LOGIN_ENDPOINT = "login"
        const val GET_PROFILE = "profile/{userId}"
        const val UPDATE_PROFILE = "profile/{userId}/{key}"
        const val REGISTER_REQUEST = "request"
        const val CHECK_REGISTER_STATUS = "request/{requestId}"
    }
}