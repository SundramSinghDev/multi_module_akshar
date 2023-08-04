package com.pronted.domain.userapps

import com.pronted.dto.login.BoardType
import com.pronted.dto.login.UserAppList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

/**
 * Login api
 */
interface UserAppsApi {
    @GET("auth/user-access")
    suspend fun fetchUserAccessInfo(
        @Query("username") mobileNumber: String,
        @Query("appName") appName: String
    ): Response<ArrayList<UserAppList>>

    @GET("/spectrum-api/school/profile-type")
    suspend fun fetchClassRoomType(
    ): Response<BoardType>
}