package com.lxn.gdghanoicheckin.network.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckInUserRequest(
    @SerializedName("qrCodeUrl")
    @Expose
    val qrCodeUrl: String? = "",
)