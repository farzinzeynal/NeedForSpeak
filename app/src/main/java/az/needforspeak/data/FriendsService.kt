package az.needforspeak.data

import az.needforspeak.model.remote.auth.InputValueModel
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.model.remote.auth.UserSearchResponse
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface FriendsService {

    @GET(Endpoints.GET_PROFILE)
    suspend fun searchUser(@Path("userId") userId: String): Response<UserSearchResponse>


}