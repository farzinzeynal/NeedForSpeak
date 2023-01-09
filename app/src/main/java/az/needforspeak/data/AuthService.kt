package az.needforspeak.data

import az.needforspeak.model.remote.auth.request.RegisteNewUserModel
import az.needforspeak.model.remote.auth.request.RegistrationRequestModel
import az.needforspeak.model.remote.auth.request.VerifyOtpModel
import az.needforspeak.model.remote.auth.response.LoginResponseModel
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.model.remote.auth.response.RegStatusModel
import az.needforspeak.model.remote.auth.response.RegisterResponseModel
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST(Endpoints.LOGIN_ENDPOINT)
    suspend fun login(): Response<LoginResponseModel>

    @GET(Endpoints.GET_PROFILE)
    suspend fun getProfile(@Path("userId") userId: String): Response<ProfileResponseModel>

    @POST(Endpoints.REGISTER_REQUEST)
    suspend fun sendRegisterRequest(@Body registerRequest: RegistrationRequestModel): Response<RegisterResponseModel>

    @GET(Endpoints.CHECK_REGISTER_STATUS)
    suspend fun checkRegistrationStatus(@Path("requestId") requestId: Int): Response<RegStatusModel>

    @POST(Endpoints.SEND_OTP)
    suspend fun sendOtp(@Body registerResponseModel: RegisterResponseModel): Response<RegisterResponseModel>

    @POST(Endpoints.VERIFY_OTP)
    suspend fun verifyOtp(@Body verifyOtpModel: VerifyOtpModel): Response<RegisterResponseModel>

    @POST(Endpoints.REGISTER_USER)
    suspend fun registerNewUser(@Body registeNewUserModel: RegisteNewUserModel): Response<String>
}