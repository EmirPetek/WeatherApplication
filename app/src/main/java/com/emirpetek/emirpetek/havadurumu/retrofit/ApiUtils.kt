package com.emirpetek.emirpetek.havadurumu.retrofit

class ApiUtils {

    companion object{

        val BASE_URL = "http://api.weatherapi.com/v1/"

        fun getWeatherForecastDaoInterface():WeatherForecastDao{
            return RetrofitClient.getClient(BASE_URL).create(WeatherForecastDao::class.java)
        }
    }
}