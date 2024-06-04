package com.example.communitystay

import com.example.communitystay.ApiResponse
import com.example.communitystay.RequestParameters
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MyApiService {
    @POST("otp/request")
    fun requestOtp(@Body requestParameters: RequestParameters): Call<ApiResponse>

    @POST("otp/verify")
    fun verifyOtp(@Body verifyParameters: VerifyParameters): Call<OtpVerifyRespone>
}