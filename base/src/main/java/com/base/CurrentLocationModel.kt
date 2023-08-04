package com.base

data class CurrentLocationModel(
    val address: String?,
    val postalCode: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float

)