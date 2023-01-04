package az.needforspeak.data

import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountService {

    @GET(Endpoints.GET_PROFILE)
    suspend fun getProfile(@Path("userId") userId: String): Response<ProfileResponseModel>

}