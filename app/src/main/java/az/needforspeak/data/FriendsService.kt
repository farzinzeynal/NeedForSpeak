package az.needforspeak.data

import az.needforspeak.model.remote.auth.response.UserSearchResponse
import az.needforspeak.utils.Endpoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FriendsService {

    @GET(Endpoints.GET_PROFILE)
    suspend fun searchUser(@Path("userId") userId: String): Response<UserSearchResponse>


}