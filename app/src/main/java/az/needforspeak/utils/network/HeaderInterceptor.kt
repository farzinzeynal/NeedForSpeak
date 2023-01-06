package az.needforspeak.utils.network

import az.needforspeak.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

internal class HeaderInterceptor : Interceptor {

    companion object{
        const val authorization = "Authorization"
        const val bearer = "Bearer"
        const val acceptLang = "Accept-Language"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().addHeader(authorization, "$bearer ${SessionManager.authToken}")
            .addHeader(acceptLang, SessionManager.getApplicationLanguage())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
