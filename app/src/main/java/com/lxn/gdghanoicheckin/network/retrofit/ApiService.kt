package com.lxn.gdghanoicheckin.network.retrofit

import com.lxn.gdghanoicheckin.network.model.request.CheckInUserRequest
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import com.lxn.gdghanoicheckin.network.model.response.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Nam Lx
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */
const val POST_CHECK_IN = "api/check-in/checkUser/v2"

interface ApiService {
    @POST(POST_CHECK_IN)
    suspend fun checkInQrCode(@Body checkInUserRequest: CheckInUserRequest): Response<CheckInResponse?>

}