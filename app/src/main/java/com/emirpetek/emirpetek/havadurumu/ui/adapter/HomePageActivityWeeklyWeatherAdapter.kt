package com.emirpetek.emirpetek.havadurumu.ui.adapter

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeather
import com.emirpetek.emirpetek.havadurumu.R
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeatherChild
import kotlin.time.Duration.Companion.days

class HomePageActivityWeeklyWeatherAdapter(val mContext:Context, val weeklyWeatherInfoList:List<rvWeeklyWeather>)
    : RecyclerView.Adapter<HomePageActivityWeeklyWeatherAdapter.WeeklyTempCardObjHolder>(){

        private var childWeatherList:List<rvWeeklyWeatherChild> = ArrayList()
        private lateinit var adapterChildWeather:HomePageActivityWeeklyWeatherChildAdapter

        inner class WeeklyTempCardObjHolder(view: View):RecyclerView.ViewHolder(view){
            val imageViewWeeklyWeatherDropdown:ImageView = view.findViewById(R.id.imageViewWeeklyWeatherDropdown)
            val imageViewWeeklyWeatherIcon:ImageView = view.findViewById(R.id.imageViewWeeklyWeatherIcon)
            val textViewWeeklyWeatherDayName:TextView = view.findViewById(R.id.textViewWeeklyWeatherDayName)
            val textViewWeeklyWeatherMinTemp:TextView = view.findViewById(R.id.textViewWeeklyWeatherMinTemp)
            val textViewWeeklyWeatherMaxTemp:TextView = view.findViewById(R.id.textViewWeeklyWeatherMaxTemp)
            val recyclerViewWeeklyWeatherChild:RecyclerView = view.findViewById(R.id.recyclerViewWeeklyWeatherChild)
            val cardViewWeeklyWeather:CardView = view.findViewById(R.id.cardViewWeeklyWeather)
            val constraintLayoutCardWeeklyWeather:ConstraintLayout = view.findViewById(R.id.constraintLayoutCardWeeklyWeather)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyTempCardObjHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_weekly_weather,parent,false)
            return WeeklyTempCardObjHolder(view)

    }


       private fun bindItems(holder:WeeklyTempCardObjHolder, position: Int){
           val pos = weeklyWeatherInfoList[position]
           if (position == 0) holder.textViewWeeklyWeatherDayName.text = "Bugün"
           else holder.textViewWeeklyWeatherDayName.text = pos.day

           val imgURL = pos.icon
           Glide.with(mContext)
               .load(imgURL)
               .skipMemoryCache(true)
               .into(holder.imageViewWeeklyWeatherIcon)
           holder.textViewWeeklyWeatherMinTemp.text = pos.min_temp
           holder.textViewWeeklyWeatherMaxTemp.text = pos.max_temp

           holder.recyclerViewWeeklyWeatherChild.visibility = View.GONE

           holder.imageViewWeeklyWeatherDropdown.setOnClickListener {
               // Tıklanma durumuna göre alt liste görünürlüğü değiştirir
               if (holder.recyclerViewWeeklyWeatherChild.visibility == View.GONE) {
                   // Görünmezse açılır
                   holder.recyclerViewWeeklyWeatherChild.visibility = View.VISIBLE
                   holder.imageViewWeeklyWeatherDropdown.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                   holder.constraintLayoutCardWeeklyWeather.setBackgroundColor(Color.parseColor("#404146"))
                   childWeather(holder, pos.childWeatherForDay, pos.dayID, position)
               } else {
                   // Görünürse kapanır
                   holder.recyclerViewWeeklyWeatherChild.visibility = View.GONE
                   holder.imageViewWeeklyWeatherDropdown.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                   holder.constraintLayoutCardWeeklyWeather.setBackgroundColor(Color.parseColor("#24242a"))
               }
           }

       }

       private fun childWeather(holder:WeeklyTempCardObjHolder, childList:List<rvWeeklyWeatherChild>, dayID:Int, position: Int){
           childWeatherList = childList.subList((dayID - 1) * 24, (dayID) * 24)
           holder.recyclerViewWeeklyWeatherChild.setHasFixedSize(true)
           holder.recyclerViewWeeklyWeatherChild.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
           adapterChildWeather = HomePageActivityWeeklyWeatherChildAdapter(mContext, childWeatherList)
           holder.recyclerViewWeeklyWeatherChild.adapter = adapterChildWeather

}


        override fun getItemViewType(position: Int): Int {

                if (!weeklyWeatherInfoList[position].isExpandable){
                    return 0
                }else{
                    return 1
                }
        }

        override fun getItemCount(): Int {
            return weeklyWeatherInfoList.size
        }

        override fun onBindViewHolder(holder: WeeklyTempCardObjHolder, position: Int) {
                bindItems(holder,position)
        }

}