package com.lxn.gdghanoicheckin.repository

import com.lxn.gdghanoicheckin.core.Either
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import com.lxn.gdghanoicheckin.network.model.response.Response
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {

    fun checkInQRCode(qrCode : String)  : Flow<Either<CheckInResponse, Response<CheckInResponse?>>>

}