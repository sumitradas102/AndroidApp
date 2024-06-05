package com.example.communitystay

import com.example.communitystay.data.ApiResponse
import com.example.communitystay.data.RequestParameters
import com.example.communitystay.data.VerifyParameters
import com.example.communitystay.data.OtpVerifyRespone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MyApiService {
    @POST("nazmul/subscription/otp/request")
    fun requestOtp(@Body requestParameters: RequestParameters): Call<ApiResponse>

    @POST("nazmul/subscription/otp/verify")
    fun verifyOtp(@Body verifyParameters: VerifyParameters): Call<OtpVerifyRespone>
}