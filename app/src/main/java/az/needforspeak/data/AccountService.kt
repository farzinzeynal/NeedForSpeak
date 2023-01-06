package az.needforspeak.data

import az.needforspeak.model.remote.auth.InputValueModel
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AccountService {

    @GET(Endpoints.GET_PROFILE)
    suspend fun getProfile(@Path("userId") userId: String): Response<ProfileResponseModel>

    @PATCH(Endpoints.UPDATE_PROFILE)
    suspend fun updateProfileData(@Path("userId") userId: String,@Path("key") key: String, @Body inputValueModel: InputValueModel): Response<String>

}