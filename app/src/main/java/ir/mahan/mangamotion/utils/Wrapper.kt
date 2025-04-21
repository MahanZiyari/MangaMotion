package ir.mahan.mangamotion.utils

sealed class Wrapper<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>: Wrapper<T>()
    class Success<T>(data: T): Wrapper<T>(data)
    class Error<T>(message: String, data: T? = null): Wrapper<T>(data, message)
}