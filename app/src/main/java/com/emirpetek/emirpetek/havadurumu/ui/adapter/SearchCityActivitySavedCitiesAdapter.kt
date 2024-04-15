package com.emirpetek.emirpetek.havadurumu.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.emirpetek.havadurumu.R
import com.emirpetek.emirpetek.havadurumu.data.model.listCityNames

class SearchCityActivitySavedCitiesAdapter(val mContext: Context, val cityList:MutableSet<String>)
    : RecyclerView.Adapter<SearchCityActivitySavedCitiesAdapter.CityListCardObjHolder>() {


        inner class CityListCardObjHolder(view:View): RecyclerView.ViewHolder(view){
            val textViewLocationName: TextView = view.findViewById(R.id.textViewLocationName)
            val imageViewDeleteCityElement: ImageView = view.findViewById(R.id.imageViewDeleteCityElement)
            val cardViewCityElement: CardView = view.findViewById(R.id.cardViewCityElement)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListCardObjHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_search_city_element,parent,false)
        return CityListCardObjHolder(view)    }

    override fun getItemCount(): Int {
        return  cityList.size
    }

    override fun onBindViewHolder(holder: CityListCardObjHolder, position: Int) {
            val pos = cityList.elementAt(position)

            holder.textViewLocationName.text = pos

    }

}