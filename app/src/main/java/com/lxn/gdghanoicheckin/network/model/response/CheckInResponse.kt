package com.lxn.gdghanoicheckin.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckInResponse(
    @SerializedName("qrCodeUrl")
    @Expose
    val qrCodeUrl: String? = "",
    @SerializedName("toAddress")
    @Expose
    val toAddress: String? = "",
    @SerializedName("titleEvent")
    @Expose
    val titleEvent: String? = ""

) : Parcelable