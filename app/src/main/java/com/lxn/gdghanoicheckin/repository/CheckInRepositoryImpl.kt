package com.lxn.gdghanoicheckin.repository

import com.lxn.gdghanoicheckin.core.Either
import com.lxn.gdghanoicheckin.network.model.request.CheckInUserRequest
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import com.lxn.gdghanoicheckin.network.model.response.Response
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : CheckInRepository {
    override fun checkInQRCode(qrCode: String): Flow<Either<CheckInResponse, Response<CheckInResponse?>>> =
        flow {
            val result = apiService.checkInQrCode(CheckInUserRequest(qrCodeUrl = qrCode))
            if (result.status == 1) {
                if (result.data != null) {
                    emit(Either.Left(result.data))
                } else {
                    emit(Either.Right(result))
                }
            } else {
                emit(Either.Right(result))
            }
        }

}