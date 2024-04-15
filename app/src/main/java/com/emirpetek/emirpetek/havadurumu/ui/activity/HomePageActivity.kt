package com.emirpetek.emirpetek.havadurumu.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.emirpetek.havadurumu.R
import com.emirpetek.emirpetek.havadurumu.data.model.WeatherForecast
import com.emirpetek.emirpetek.havadurumu.data.model.rvDailyWeather
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeather
import com.emirpetek.emirpetek.havadurumu.data.model.rvWeeklyWeatherChild
import com.emirpetek.emirpetek.havadurumu.databinding.ActivityHomePageBinding
import com.emirpetek.emirpetek.havadurumu.retrofit.ApiUtils
import com.emirpetek.emirpetek.havadurumu.ui.adapter.HomePageActivityDailyWeatherAdapter
import com.emirpetek.emirpetek.havadurumu.ui.adapter.HomePageActivityWeeklyWeatherAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var arraylistDailyWeather: ArrayList<rvDailyWeather>
    private lateinit var adapterDailyWeather: HomePageActivityDailyWeatherAdapter
    private lateinit var arraylistWeeklyWeather: ArrayList<rvWeeklyWeather>
    private lateinit var arraylistWeeklyWeatherChild: ArrayList<rvWeeklyWeatherChild>
    private lateinit var adapterWeeklyWeather: HomePageActivityWeeklyWeatherAdapter
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    var place = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignPlaceFromSP()
        getWeatherForecast()
        getDailyWeatherRV()
        getWeeklyWeatherRV()
        navigationDrawer()
        getSavedCitiesToNavDrawer()
        setTimeTextView()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTimeTextView(){
        val currentDateTime = LocalDateTime.now()

        // Tarih ve saat bilgisini istediğiniz formatta biçimlendir
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val formattedDateTime = currentDateTime.format(formatter)
        binding.textViewCurrentTime.text = formattedDateTime
    }


    fun assignPlaceFromSP(){
        sharedPreferences = getSharedPreferences("city_saved", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences.getString("city", "Istanbul")
        place = savedCity!!
    }

    private fun navigationDrawer() {
        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setSupportActionBar(binding.toolbarHomePage)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigatonView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_manage_location -> {
                    startActivity(Intent(this, SearchCityActivity::class.java))
                    //finish()
                }
                R.id.action_about_applicaton -> {
                    aboutApp()
                }
                R.id.action_feedback -> Toast.makeText(this,"feedback tıklandı",Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Eğer navigasyon açıksa, kapat
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Eğer navigasyon kapalıysa, varsayılan geri tuşu davranışını devam ettir
            super.onBackPressed()
        }
    }



    fun getWeatherForecast() {
        val wfd = ApiUtils.getWeatherForecastDaoInterface()

        wfd.getWeatherForecast(place).enqueue(object : Callback<WeatherForecast> {
            override fun onResponse(
                call: Call<WeatherForecast>?,
                response: Response<WeatherForecast>?
            ) {
                if (response?.isSuccessful == true) {
                    val forecastday = response.body()?.forecast?.forecastday!!
                    val current = response.body()?.current
                    val location = response.body()?.location!!
                    if (current != null) {
                        binding.toolbarHomePage.title = location.name.toString()
                        binding.textViewCurrentWeather.text = current.tempC.toString() + "°C"
                        binding.textViewCurrentWeatherText.text = current.condition?.text.toString()
                        val imgURL = "https://" + current.condition?.icon.toString().substring(2)
                        Glide.with(this@HomePageActivity)
                            .load(imgURL)
                            .skipMemoryCache(true)
                            .into(binding.imageViewCurrentWeatherIcon)

                        for (i in forecastday) {
                            val currentMin = i.day?.mintempC.toString()
                            val currentMax = i.day?.maxtempC.toString()
                            val currentFeeling = current.feelslikeC.toString()
                            binding.textViewCurrentMaxMinFeelsDegree.text =
                                "Max: $currentMax°C Min: $currentMin°C \n Hissedilen: $currentFeeling°C"
                        }

                        binding.textViewCurrentWindSpeed.text =
                            "Rüzgar: " + current.windKph.toString() + " km/h"
                    }

                } else {
                    // Hata durumunu ele alma
                    Log.e("Weather Fetch", "Failed to fetch weather data.")
                }

            }

            override fun onFailure(call: Call<WeatherForecast>?, t: Throwable?) {
                // Hata durumunu ele alma
                Log.e("Weather Fetch", "Network call failed", t)
            }
        })
    }

    fun getDailyWeatherRV() {
        val wfd = ApiUtils.getWeatherForecastDaoInterface()

        arraylistDailyWeather = ArrayList()
        binding.recyclerViewDailyWeather.setHasFixedSize(true)
        binding.recyclerViewDailyWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterDailyWeather = HomePageActivityDailyWeatherAdapter(this, arraylistDailyWeather)
        binding.recyclerViewDailyWeather.adapter = adapterDailyWeather

        wfd.getWeatherForecast(place).enqueue(object : Callback<WeatherForecast> {
            override fun onResponse(
                call: Call<WeatherForecast>?,
                response: Response<WeatherForecast>?
            ) {
                if (response?.isSuccessful == true) {
                    val forecastday = response.body()?.forecast?.forecastday!!
                    val current = response.body()?.current
                    val location = response.body()?.location!!
                    var hourLoop = 0
                    for (i in forecastday) {
                        val hour = i.hour
                        for (j in hour) {
                            val currentUnixtime =
                                (System.currentTimeMillis().toString().substring(0, 10)).toInt()
                            if (j.timeEpoch!! > currentUnixtime && hourLoop < 24) {
                                val time = j.time?.substring(11)!!
                                val icon = "https:" + j.condition?.icon.toString()
                                val temperature = j.tempC.toString()
                                val obj = rvDailyWeather(time, icon, temperature)
                                arraylistDailyWeather.add(obj)
                                hourLoop++
                            } else {
                                continue
                            }

                        }
                    }
                    adapterDailyWeather.notifyDataSetChanged()

                } else {
                    // Hata durumunu ele alma
                    Log.e("Weather Fetch", "Failed to fetch weather data.")
                }

            }

            override fun onFailure(call: Call<WeatherForecast>?, t: Throwable?) {
                // Hata durumunu ele alma
                Log.e("Weather Fetch", "Network call failed", t)
            }
        })
    }

    fun getWeeklyWeatherRV() {
        val wfd = ApiUtils.getWeatherForecastDaoInterface()

        arraylistWeeklyWeather = ArrayList()
        arraylistWeeklyWeatherChild = ArrayList()
        binding.recyclerViewWeeklyWeather.setHasFixedSize(true)
        binding.recyclerViewWeeklyWeather.layoutManager = LinearLayoutManager(this)
        adapterWeeklyWeather = HomePageActivityWeeklyWeatherAdapter(this, arraylistWeeklyWeather)
        binding.recyclerViewWeeklyWeather.adapter = adapterWeeklyWeather

        wfd.getWeatherForecast(place).enqueue(object : Callback<WeatherForecast> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<WeatherForecast>?,
                response: Response<WeatherForecast>?
            ) {
                if (response?.isSuccessful == true) {
                    val forecastday = response.body()?.forecast?.forecastday!!
                    val current = response.body()?.current
                    val location = response.body()?.location!!
                    var dayCounter = 0

                    for (i in forecastday) {
                        val day = getDayFromDate(i.date!!)
                        val icon = "https:" + i.day?.condition?.icon
                        val min_temp = i.day?.mintempC.toString() + "°C"
                        val max_temp = i.day?.maxtempC.toString() + "°C"
                        var lastHourTimeStamp = i.dateEpoch!! - 14400
                        var hourCounter = 0

                        for (j in i.hour) {
                            if (hourCounter < 24 && lastHourTimeStamp < j.timeEpoch!!){
                                val hourIcon = "https:" + j.condition?.icon
                                var hourTemp = j.tempC.toString()
                                var hourInfo = j.time.toString().substring(11)
                                val objWeeklyWeatherChild = rvWeeklyWeatherChild(hourIcon, hourTemp, hourInfo)
                                arraylistWeeklyWeatherChild.add(objWeeklyWeatherChild)
                                hourCounter++
                            }
                        }
                        dayCounter++

                        val objWeeklyWeather = rvWeeklyWeather(
                            day,
                            icon,
                            min_temp,
                            max_temp,
                            arraylistWeeklyWeatherChild,
                            false,
                            dayCounter
                        )
                        arraylistWeeklyWeather.add(objWeeklyWeather)
                    }

                    adapterWeeklyWeather.notifyDataSetChanged()


                } else {
                    // Hata durumunu ele alma
                    Log.e("Weather Fetch", "Failed to fetch weather data.")
                }

            }

            override fun onFailure(call: Call<WeatherForecast>?, t: Throwable?) {
                // Hata durumunu ele alma
                Log.e("Weather Fetch", "Network call failed", t)
            }
        })



    }

    fun getSavedCitiesToNavDrawer(){
            sharedPreferences = getSharedPreferences("saved_city", Context.MODE_PRIVATE)
            val savedList = sharedPreferences.getStringSet("saved_city_list", HashSet()) ?: HashSet()



            for (item in savedList) {
                Log.e("saved_city_list", item)

            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayFromDate(day:String):String{
        val dateStr = day
        val formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd",
            Locale("tr", "TR")
        ) // Stringi LocalDate'ye dönüştürmek için format belirleme ve Türkçe ay isimlerini kullanma
        val date =
            LocalDate.parse(dateStr, formatter) // Stringi LocalDate'e dönüştürme

        val day = convertToTurkish(date.dayOfWeek)
        return day
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToTurkish(day: DayOfWeek): String {
        return when (day) {
            DayOfWeek.MONDAY -> "Pazartesi"
            DayOfWeek.TUESDAY -> "Salı"
            DayOfWeek.WEDNESDAY -> "Çarşamba"
            DayOfWeek.THURSDAY -> "Perşembe"
            DayOfWeek.FRIDAY -> "Cuma"
            DayOfWeek.SATURDAY -> "Cumartesi"
            DayOfWeek.SUNDAY -> "Pazar"
        }

    }

    fun aboutApp(){
        val ad = AlertDialog.Builder(this)
        ad.setTitle("Uygulama Hakkında")
        ad.setMessage(R.string.about_me)
        ad.setPositiveButton("OK"){ dialog, _ ->
            dialog.dismiss() // AlertDialog'u kapat
        }
        ad.show()

    }
}