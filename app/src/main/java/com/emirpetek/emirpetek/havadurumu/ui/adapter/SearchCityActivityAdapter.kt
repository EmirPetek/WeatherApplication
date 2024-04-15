package com.emirpetek.emirpetek.havadurumu.ui.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.emirpetek.emirpetek.havadurumu.R
import com.emirpetek.emirpetek.havadurumu.data.model.Location
import com.emirpetek.emirpetek.havadurumu.data.model.listCityNames
import com.emirpetek.emirpetek.havadurumu.ui.activity.HomePageActivity
import com.emirpetek.emirpetek.havadurumu.ui.activity.SearchCityActivity

class SearchCityActivityAdapter(val mContext:Context, val cityList:List<List<listCityNames>>)
    :RecyclerView.Adapter<SearchCityActivityAdapter.CityListCardObjHolder>(){

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

        inner class CityListCardObjHolder(view: View):RecyclerView.ViewHolder(view){
            val textViewLocationName:TextView = view.findViewById(R.id.textViewLocationName)
            val imageViewDeleteCityElement:ImageView = view.findViewById(R.id.imageViewDeleteCityElement)
            val cardViewCityElement:CardView = view.findViewById(R.id.cardViewCityElement)
            val linearLayoutCityElement:LinearLayout = view.findViewById(R.id.linearLayoutCityElement)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListCardObjHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_search_city_element,parent,false)
        return CityListCardObjHolder(view)
    }

    override fun getItemCount(): Int {
        return  cityList.size
    }

    override fun onBindViewHolder(holder: CityListCardObjHolder, position: Int) {
        val pos = cityList[position]

        //holder.imageViewDeleteCityElement.visibility = View.GONE


        for (c in cityList){
            for (a in c){
                holder.textViewLocationName.text = a.name + " " + a.region + " " + a.country


                holder.imageViewDeleteCityElement.setOnClickListener {
                    goToHomePage(a.name!!)
                }

                holder.cardViewCityElement.setOnClickListener {
                    goToHomePage(a.name!!)

                }

                holder.linearLayoutCityElement.setOnClickListener {
                    goToHomePage(a.name!!)

                }

                holder.cardViewCityElement.setOnClickListener {
                   // saveCitytoSP(a.name.toString())
                    goToHomePage(a.name!!)

                }

            }
        }
    }

    private fun goToHomePage(name:String){
        saveCity(name.toString())
        mContext.startActivity(Intent(mContext,HomePageActivity::class.java))
        (mContext as SearchCityActivity).finish()
        Toast.makeText(mContext,"Şehir bilgisi değiştirildi.",Toast.LENGTH_SHORT).show()
    }
    private fun saveCity(cityName: String){
        sharedPreferences = mContext.getSharedPreferences("city_saved", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("city",cityName)
        editor.apply()
    }


    private fun saveCitytoSP(cityName:String){


        sharedPreferences = mContext.getSharedPreferences("saved_city", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val currentList = sharedPreferences.getStringSet("saved_city_list", HashSet())?.toMutableSet() ?: mutableSetOf()
        currentList.add(cityName)
        editor.putStringSet("saved_city_list", currentList)
        editor.apply()
    }
}