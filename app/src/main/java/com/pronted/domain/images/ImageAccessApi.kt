package com.pronted.domain.images

import com.pronted.dto.images.ProfileImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT
import retrofit2.http.Query

interface ImageAccessApi {
    @GET("documents/get")
    suspend fun fetchImage(
        @HeaderMap headers: Map<String, String>?,
        @Query("module") module: String,
        @Query("fileName") profileId: String
    ): Response<ProfileImageResponse>

    @GET("documents/get")
    suspend fun fetchImage(
        @Query("module") module: String,
        @Query("fileName") profileId: String
    ): Response<ProfileImageResponse>

    @GET("security/groups")
    suspend fun fetchSecurityGroupList(
        @Query("module") appName: String
    ): Response<ArrayList<String>>

    @PUT("documents/upload")
    suspend fun uploadImage(
        @HeaderMap headers: Map<String, String>?,
        @Query("module") module: String = "Students",
        @Query("fileName") fileName: String,
        @Query("contentType") contentType: String,
    ): Response<ProfileImageResponse>
}