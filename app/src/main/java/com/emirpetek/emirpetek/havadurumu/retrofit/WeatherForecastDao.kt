package com.emirpetek.emirpetek.havadurumu.retrofit

import com.emirpetek.emirpetek.havadurumu.data.model.Location
import com.emirpetek.emirpetek.havadurumu.data.model.WeatherForecast
import com.emirpetek.emirpetek.havadurumu.data.model.listCityNames
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherForecastDao {

    val APIKey: String
        get() = "d2a9ec1a3827412fa19160940240903"
    @POST("forecast.json?key=d2a9ec1a3827412fa19160940240903&days=7&aqi=no&alerts=no&lang=tr")
   // fun getWeatherForecast(): Call<WeatherForecast>
    fun getWeatherForecast(@Query("q") place: String): Call<WeatherForecast>

    @POST("search.json?key=d2a9ec1a3827412fa19160940240903")
    fun getCityNames(@Query("q") place: String):Call<List<listCityNames>>
}