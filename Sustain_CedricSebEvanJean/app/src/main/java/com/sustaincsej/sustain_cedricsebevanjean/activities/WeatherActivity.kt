package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.models.CurrentWeather
import com.sustaincsej.sustain_cedricsebevanjean.models.UVI
import okhttp3.*
import java.io.IOException
import kotlin.math.roundToInt

/**
 * Displays the current weather and weather information
 */
class WeatherActivity : AppCompatActivity() {

    //To get current location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //API
    private val client = OkHttpClient()
    private val apiKey = "8c11d5bd636c3d9b3afe38145afc23f2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        //Get Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                val currentLocation = location ?: Location("empty")
                updateLocationUI(currentLocation)
                getWeatherFromLocation(currentLocation)
                getUVFromLocation(currentLocation)
            }.addOnFailureListener {
                Log.i(TAG, "Error: $it")
            }
    }

    /**
     * Updates the latitude and longitude views
     */
    private fun updateLocationUI(location: Location) {
        findViewById<TextView>(R.id.weather_current_lat).text = location.latitude.toString()
        findViewById<TextView>(R.id.weather_current_lon).text = location.longitude.toString()
    }

    /**
     * Makes an API call to get the current weather at the location
     */
    private fun getWeatherFromLocation(location: Location) {
        val lat = location.latitude.toString()
        val lon = location.longitude.toString()

        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&APPID=${apiKey}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Error in weather API call: $e")
            }
            override fun onResponse(call: Call, response: Response) {
                val json: String = response.body()!!.string()
                val currentWeather = Gson().fromJson(json, CurrentWeather::class.java)

                runOnUiThread {
                    updateWeatherUI(currentWeather)
                }

            }
        })
    }

    /**
     * Makes an API call to get the current uv index at the location
     */
    private fun getUVFromLocation(location: Location) {
        val lat = location.latitude.toString()
        val lon = location.longitude.toString()

        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/uvi?lat=${lat}&lon=${lon}&APPID=${apiKey}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Error in weather API call: $e")
            }
            override fun onResponse(call: Call, response: Response) {
                val json: String = response.body()!!.string()
                val uvi = Gson().fromJson(json, UVI::class.java)

                runOnUiThread {
                    updateUVUI(uvi)
                }

            }
        })
    }

    /**
     * Updates all fields in the weather view
     */
    private fun updateWeatherUI(weather: CurrentWeather) {
        findViewById<TextView>(R.id.weather_temp).text = "${weather.main.temp} C"
        findViewById<TextView>(R.id.weather_recommendation_text).text = when {
            (weather.main.temp<-5 || weather.weather[0].main=="Snow" || weather.weather[0].main=="Rain") -> resources.getString(R.string.weather_recommendation_badweather)
            (weather.main.temp<15) -> resources.getString(R.string.weather_recommendation_okweather)
            (weather.main.temp>17) -> resources.getString(R.string.weather_recommendation_goodweather)
            else -> "" //Unreachable
        }
        findViewById<ImageView>(R.id.weather_image).setImageResource(when (weather.weather[0].main) {
            "Clear" -> R.drawable.clear
            "Rain" -> R.drawable.rain
            "Snow" -> R.drawable.snow
            "Storm" -> R.drawable.storm
            "Clouds" -> R.drawable.clouds
            else -> R.drawable.weather
        })
        findViewById<TextView>(R.id.weather_description).text = weather.weather[0].description
    }

    /**
     * Updates all fields in the uv view
     */
    private fun updateUVUI(uvi: UVI) {
        findViewById<TextView>(R.id.weather_uv).text = when (uvi.value.roundToInt()) {
            in 0..2 -> resources.getString(R.string.weather_uv_low)
            in 3..5 -> resources.getString(R.string.weather_uv_moderate)
            in 6..7 -> resources.getString(R.string.weather_uv_high)
            in 8..10 -> resources.getString(R.string.weather_uv_veryhigh)
            else -> resources.getString(R.string.weather_uv_extreme)
        }
    }

    companion object {
        private val TAG = "WeatherActivity"
    }


}
