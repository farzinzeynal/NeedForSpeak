package az.needforspeak.data

import az.needforspeak.model.remote.auth.LoginResponseModel
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST(Endpoints.LOGIN_ENDPOINT)
    suspend fun login(): Response<LoginResponseModel>

    @GET(Endpoints.GET_PROFILE)
    suspend fun getProfile(@Path("userId") userId: String): Response<ProfileResponseModel>
}