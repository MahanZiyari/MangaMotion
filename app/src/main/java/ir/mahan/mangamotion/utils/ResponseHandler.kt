package ir.mahan.mangamotion.utils

import retrofit2.Response

open class ResponseHandler<T>(private val response: Response<T>) {

    fun handleResponseCodes(): Wrapper<T> {
        return when {
            response.message().contains("timeout") -> Wrapper.Error("Timeout")
            response.code() == 401 -> Wrapper.Error("You are not authorized")
            response.code() == 402 -> Wrapper.Error("Your free plan finished")
            response.code() == 422 -> Wrapper.Error("Api key not found!")
            response.code() == 500 -> Wrapper.Error("Try again")
            response.isSuccessful -> Wrapper.Success(response.body()!!)
            else -> Wrapper.Error(response.message())
        }
    }
}