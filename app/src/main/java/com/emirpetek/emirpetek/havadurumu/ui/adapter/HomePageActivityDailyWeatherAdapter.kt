package com.emirpetek.emirpetek.havadurumu.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.emirpetek.havadurumu.data.model.rvDailyWeather
import com.emirpetek.emirpetek.havadurumu.R
import org.w3c.dom.Text


class HomePageActivityDailyWeatherAdapter(val mContext: Context, val tempList:ArrayList<rvDailyWeather>)
    :RecyclerView.Adapter<HomePageActivityDailyWeatherAdapter.DailyTempCardObjHolder>(){

        inner class DailyTempCardObjHolder(view: View):RecyclerView.ViewHolder(view){
            val textViewDailyHour:TextView = view.findViewById(R.id.textViewDailyHour)
            val textViewDailyTemperature:TextView = view.findViewById(R.id.textViewDailyTemperature)
            val imageViewDailyWeatherIcon:ImageView = view.findViewById(R.id.imageViewDailyWeatherIcon)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTempCardObjHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_daily_weather,parent,false)
        return DailyTempCardObjHolder(view)
    }

    override fun getItemCount(): Int {
        return tempList.size    }

    override fun onBindViewHolder(holder: DailyTempCardObjHolder, position: Int) {
        val pos = tempList.get(position)

        holder.textViewDailyHour.text = pos.time
        holder.textViewDailyTemperature.text = pos.temperature + "°C"
        val imgURL = pos.icon
        Glide.with(mContext)
            .load(imgURL)
            .skipMemoryCache(true)
            .into(holder.imageViewDailyWeatherIcon)
    }
}