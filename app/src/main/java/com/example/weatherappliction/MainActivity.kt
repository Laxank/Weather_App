package com.example.weatherappliction


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.LocaleData
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.API.Weatherapp
import com.example.API.apiintarface


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.thread
import kotlin.contracts.contract


open class MainActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        fetchWeatherData("Visnagar")
//        SearchView()
    nextweek()


    val loccation = findViewById<TextView>(R.id.location)
    loccation.setOnClickListener{
      processwithcurrentlocation()
    }

        val currentlocation = findViewById<ImageView>(R.id.currentState)


        currentlocation.setOnClickListener {

            fetchWeatherData("Visnagar")

        }
SwipeRefreshLayout.OnRefreshListener{
    refreshing()
}

    }


    private fun nextweek() {
       val onclick = findViewById<TextView>(R.id.next7day)
        onclick.setOnClickListener{
val intent = Intent(this,weatherforcast::class.java)
            startActivity(intent)
        }
    }


//    private fun SearchView() {
//        val searchView = findViewById<SearchView>(R.id.search_bar)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//
//                    fetchWeatherData(query)
//                }
//                return true
//
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//        })
//    }


    private fun fetchWeatherData(Cityname: String) {
        refreshing()
        try {
            val retrofit = Retrofit.Builder()

                .addConverterFactory(GsonConverterFactory.create())

                .baseUrl("https://api.openweathermap.org/data/2.5/")

                .build().create(apiintarface::class.java)

            val response = retrofit.getweatherdata(Cityname, "090727b5f89af693e395b63d71119057", "metric")

            response.enqueue(object : retrofit2.Callback<Weatherapp> {
                override fun onResponse(call: Call<Weatherapp>, response: retrofit2.Response<Weatherapp>) {
                    val ResponseBody = response.body()
                    if (response.isSuccessful && ResponseBody != null) {
                        val tampruter: Int = ResponseBody.main.temp.toInt()
                        val address = ResponseBody.sys.country.toString()
                        val updatedAtText: Long = ResponseBody.dt.toLong()
                        val windSpeed = ResponseBody.wind.speed.toString()
                        val pressure = ResponseBody.main.pressure.toString()
                        val humidity = ResponseBody.main.humidity.toString()
                        val airqulity = ResponseBody.main.feels_like.toString()
                        val sunrise = Sunrise(ResponseBody.sys.sunrise.toLong())
                        val sunset = Sunrise(ResponseBody.sys.sunset.toLong())
                        val decription = ResponseBody.weather.firstOrNull()?.description ?: "unknown"


                        val tempMin = ResponseBody.main.temp_min.toInt()
                        val tempMax = ResponseBody.main.temp_max.toInt()



                        findViewById<TextView>(R.id.temp).text = "$tampruter°C"
                        findViewById<TextView>(R.id.address).text = "$Cityname $address"
                        findViewById<TextView>(R.id.updated_at).text = Update_At(updatedAtText)
                        //                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize(Locale.ROOT)
//                findViewById<TextView>(R.id.temp).text = temp



                        findViewById<TextView>(R.id.sunrise).text = "$sunrise am"
                        findViewById<TextView>(R.id.status).text = "$decription"
                        findViewById<TextView>(R.id.sunset).text = "$sunset pm"
                        findViewById<TextView>(R.id.wind).text = "$windSpeed km/h"
                        findViewById<TextView>(R.id.pressure).text = "$pressure hpa"
                        findViewById<TextView>(R.id.humidity).text = "$humidity %"
                        findViewById<TextView>(R.id.temp_max).text = "$tempMax °C "
                        findViewById<TextView>(R.id.temp_min).text = "$tempMin°C /"
                        findViewById<TextView>(R.id.about).text = "$airqulity good"

                        findViewById<TextView>(R.id.day).text = Day(System.currentTimeMillis())
                        /* Views populated, Hiding the loader, Showing the main design */
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<TextView>(R.id.Lodind).visibility = View.GONE
                        findViewById<LinearLayout>(R.id.mainContainer).visibility = View.VISIBLE
                        val refreshing = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                        refreshing.isRefreshing = false



                        changeImagsAccordingToWeaterCondtion(decription)
                    }
                }

                override fun onFailure(call: Call<Weatherapp>, t: Throwable) {

                }

            })



        } catch (e: Exception) {
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            findViewById<TextView>(R.id.Lodind).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
        }

    }

    private fun Sunrise(toString: Long): String {
        val local = toString.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()

        }

        return local.toString()
    }

    fun changeImagsAccordingToWeaterCondtion(decription: String) {
        val lotty = findViewById<LottieAnimationView>(R.id.lotty)
        when (decription) {

            "clear sky", "sunny", "clear" -> {

                val background = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                background.setBackgroundResource(R.drawable.backg)
                lotty.setAnimation(R.raw.sun)


            }

            "haze", "overcast clouds", "broken clouds" -> {

                val background = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                background.setBackgroundResource(R.drawable.brokan)
                lotty.setAnimation(R.raw.clouds)

            }


            "partly clouds", "clouds", "overcast", "mist", "foggy" -> {

                val background = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                background.setBackgroundResource(R.drawable.cloudy1)

                lotty.setAnimation(R.raw.clouds)

            }

            "light rain", "drizzle", "moderate rain", "showers", "heavy rain" -> {

                val background = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                background.setBackgroundResource(R.drawable.rain)

                lotty.setAnimation(R.raw.rain)

            }

            "light snow", "moderate snow", "heavy snow", "blizzard" -> {

                val background = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
                background.setBackgroundResource(R.drawable.snow)
                lotty.setAnimation(R.raw.snow)

            }
        }

        lotty.playAnimation()

    }

    private fun Day(currentTimeMillis: Long): String {
        val sdg = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdg.format((Date()))

    }

    private fun Update_At(updatedAtText: Long): String {
        val sdg = SimpleDateFormat("dd MMMM yyyy  hh:mm a", Locale.getDefault())
        return sdg.format((Date()))

    }
    private fun refreshing(){

            val refreshing = findViewById<SwipeRefreshLayout>(R.id.MainContainer)
            refreshing.isRefreshing = false


    }


    // get location permission frome user

    private val locationpermissionmenifest = registerForActivityResult(
       ActivityResultContracts.RequestPermission()
    ){
        isGranted ->
        if (isGranted){
            getcurrentlocation()
        }else{
            Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show()

        }
    }

    private fun getcurrentlocation() {
        Toast.makeText(this,"current location",Toast.LENGTH_SHORT).show()
    }

    private fun ispermissiongranted():Boolean{
        return ContextCompat.checkSelfPermission(
            this,Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestlocationpermission(){
        locationpermissionmenifest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
private fun processwithcurrentlocation(){
    if (ispermissiongranted()){
        getcurrentlocation()
    }else{
        requestlocationpermission()
    }
}



//    private fun showlocationopction(){
//        val opction = arrayOf("Current location","Search manually")
//        AlertDialog.Builder(this).apply {
//            setTitle("Choose Location Methode")
//            setItems(opction){
//                _,which ->
//                when(which){
//                    0 -> processwithcurrentlocation()
//                }
//            }
//            show()
//        }
//    }

}


//    inner class weatherTask() : AsyncTask<String, Void, String>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
//            findViewById<LinearLayout>(R.id.mainContainer).visibility = View.GONE
//            findViewById<TextView>(R.id.errorText).visibility = View.GONE
//        }
//
//
//
//        override fun onPostExecute(result: String) {
//            super.onPostExecute(result)
//            try {
//                /* Extracting JSON returns from the API */
//                val jsonObj = JSONObject(result)
//                val main = jsonObj.getJSONObject("main")
//                val sys = jsonObj.getJSONObject("sys")
//                val wind = jsonObj.getJSONObject("wind")
//                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
//
//                val updatedAt:Long = jsonObj.getLong("dt")
//                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))

//
//                val temp = main.getString("temp")+"°C"

//                val pressure = main.getString("pressure")
//                val humidity = main.getString("humidity")
//

//                val windSpeed = wind.getString("speed")
//                val weatherDescription = weather.getString("description")
//
//                val address = jsonObj.getString("name")+", "+sys.getString("country")
//
//                /* Populating extracted data into our views */
//                findViewById<TextView>(R.id.address).text = address
//                findViewById<TextView>(R.id.updated_at).text =  updatedAtText
//                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize(Locale.ROOT)
//                findViewById<TextView>(R.id.temp).text = temp
//                findViewById<TextView>(R.id.temp_min).text = tempMin
//                findViewById<TextView>(R.id.temp_max).text = tempMax

//                findViewById<TextView>(R.id.wind).text = windSpeed
//                findViewById<TextView>(R.id.pressure).text = pressure
//                findViewById<TextView>(R.id.humidity).text = humidity
//                findViewById<TextView>(R.id.day).text =Day
//
//                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//                findViewById<LinearLayout>(R.id.mainContainer).visibility = View.VISIBLE
//
//            } catch (e: Exception) {
//                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
//            }
//
//        }
//
//
//    }



