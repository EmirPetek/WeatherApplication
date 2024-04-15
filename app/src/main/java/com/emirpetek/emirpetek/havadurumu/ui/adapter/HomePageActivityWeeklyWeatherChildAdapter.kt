package com.emirpetek.emirpetek.havadurumu.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeather
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeatherChild
import com.emirpetek.emirpetek.havadurumu.R

class HomePageActivityWeeklyWeatherChildAdapter(val mContext: Context, val weeklyWeatherInfoList:List<rvWeeklyWeatherChild>)
    : RecyclerView.Adapter<HomePageActivityWeeklyWeatherChildAdapter.WeeklyChildTempCardObjHolder>(){

        inner class WeeklyChildTempCardObjHolder(view:View):RecyclerView.ViewHolder(view){
             val imageViewWeatherChildHourlyIcon: ImageView = view.findViewById(R.id.imageViewWeatherChildHourlyIcon)
             val textViewWeatherChildHourlyTemp: TextView = view.findViewById(R.id.textViewWeatherChildHourlyTemp)
             val textViewWeatherChildHourlyHour:TextView = view.findViewById(R.id.textViewWeatherChildHourlyHour)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeeklyChildTempCardObjHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_weekly_weather_child_hourly,parent,false)
        return WeeklyChildTempCardObjHolder(view)
    }

    override fun getItemCount(): Int {
        return weeklyWeatherInfoList.size    }

    override fun onBindViewHolder(holder: WeeklyChildTempCardObjHolder, position: Int) {
        val pos = weeklyWeatherInfoList[position]

        val hour = pos.hour
        val icon = pos.icon
        val temp = pos.temp + "°C"

        holder.textViewWeatherChildHourlyHour.text = hour
        holder.textViewWeatherChildHourlyTemp.text = temp
        Glide.with(mContext)
            .load(icon)
            .skipMemoryCache(true)
            .into(holder.imageViewWeatherChildHourlyIcon)
    }

}