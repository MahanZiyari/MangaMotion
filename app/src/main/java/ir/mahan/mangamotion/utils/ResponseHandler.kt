package ir.mahan.mangamotion.utils

import retrofit2.Response

open class ResponseHandler<T>(private val response: Response<T>) {

    fun handleResponseCodes(
        onTooManyRequest: () -> Unit = {},
    ): Wrapper<T> {
        return when {
            response.message().contains("timeout") -> Wrapper.Error("Timeout")
            response.code() == 400 -> Wrapper.Error("400: Bad Request")
            response.code() == 404 -> Wrapper.Error("404: Not Found")
            response.code() == 405 -> Wrapper.Error("405: Method Not Allowed")
            response.code() == 429 -> {
                onTooManyRequest()
                Wrapper.Error("429: Too Many Request")
            }
            response.code() == 500 -> Wrapper.Error("500: Internal Server Error")
            response.code() == 503 -> Wrapper.Error("503: Service Unavailable")
            response.isSuccessful -> Wrapper.Success(response.body()!!)
            else -> Wrapper.Error(response.message())
        }
    }
}