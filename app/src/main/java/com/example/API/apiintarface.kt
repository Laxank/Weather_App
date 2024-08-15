package com.example.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface apiintarface {
    @GET("weather")
    fun getweatherdata(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ) : Call<Weatherapp>
    fun getCurrentData(
        @Query("let") letitude:String,
        @Query("appid") appid:String,
        @Query("lon") lon:String
    ) : Call<Weatherapp>
}