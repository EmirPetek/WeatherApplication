package com.emirpetek.emirpetek.havadurumu.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.emirpetek.emirpetek.havadurumu.databinding.ActivitySearchCityBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.emirpetek.havadurumu.data.model.listCityNames
import com.emirpetek.emirpetek.havadurumu.retrofit.ApiUtils
import com.emirpetek.emirpetek.havadurumu.ui.adapter.SearchCityActivityAdapter
import com.emirpetek.emirpetek.havadurumu.ui.adapter.SearchCityActivitySavedCitiesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCityActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySearchCityBinding
    private lateinit var adapterSearchCity:SearchCityActivityAdapter
    private lateinit var adapterSavedCities:SearchCityActivitySavedCitiesAdapter
    private lateinit var cityList:ArrayList<List<listCityNames>>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearchCity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSearchCity.setNavigationOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }


        city()
        getSavedCities()

    }

    private fun city() {

        cityList = ArrayList()
        binding.rvCityList.setHasFixedSize(true)
        binding.rvCityList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSearchCity = SearchCityActivityAdapter(this, cityList)
        binding.rvCityList.adapter = adapterSearchCity


        binding.searchViewCity.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                getCityResult(p0!!)
              //  binding.cardViewSavedCities.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                getCityResult(p0!!)
             //   binding.cardViewSavedCities.visibility = View.GONE
                return true
            }

        })
    }
    private fun getSavedCities(){/*
        sharedPreferences = getSharedPreferences("saved_city", Context.MODE_PRIVATE)
        val savedList = sharedPreferences.getStringSet("saved_city_list", HashSet()) ?: HashSet()

        binding.rvSavedCityList.setHasFixedSize(true)
        binding.rvSavedCityList.layoutManager =
            LinearLayoutManager(this)
        adapterSavedCities = SearchCityActivitySavedCitiesAdapter(this, savedList)
        binding.rvSavedCityList.adapter = adapterSavedCities


        for (item in savedList) {
            Log.e("saved_city_list", item)

        }*/
    }

    private fun getCityResult(cityName:String){
        val wfd = ApiUtils.getWeatherForecastDaoInterface()

        cityList.clear()
        wfd.getCityNames(cityName).enqueue(object : Callback<List<listCityNames>> {
            override fun onResponse(
                call: Call<List<listCityNames>>?,
                response: Response<List<listCityNames>>?
            ) {

                if (response?.isSuccessful == true) {
                    val cityObj = response.body()
                    cityList.add(cityObj)
                } else {
                    // Hata durumunu ele alma
                    Log.e("Weather Fetch", "Failed to fetch weather data.")
                }
                adapterSearchCity.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<listCityNames>>?, t: Throwable?) {
                // Hata durumunu ele alma
                Log.e("exception error: " , "cityname: " + cityName   + t.toString())
                Log.e("Weather Fetch", "Network call failed", t)
            }
        })
    }




}

