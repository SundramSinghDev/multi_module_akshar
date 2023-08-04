package com.pronted.dto



sealed class ApiResponse<T>{
    class Success<T>(val data: T): ApiResponse<T>()
    class Error<T>(val errorResponse: ErrorResponse): ApiResponse<T>()
    class Exception<T>(val e: Throwable): ApiResponse<T>()
}
