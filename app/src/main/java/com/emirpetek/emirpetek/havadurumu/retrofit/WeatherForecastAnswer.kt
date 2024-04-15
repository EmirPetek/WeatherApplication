package com.emirpetek.emirpetek.havadurumu.retrofit

import com.emirpetek.emirpetek.havadurumu.data.model.WeatherForecast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherForecastAnswer(
        @SerializedName("weatherForecast")
        @Expose
        var weatherForecast:List<WeatherForecast>) {
}