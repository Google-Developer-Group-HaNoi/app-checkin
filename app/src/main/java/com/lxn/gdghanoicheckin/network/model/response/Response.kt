package com.lxn.gdghanoicheckin.network.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("data")
    @Expose
    val `data`: T? = null,
    @SerializedName("message")
    @Expose
    val message: String? = null,
    @SerializedName("status")
    @Expose
    val status: Int? = null
)