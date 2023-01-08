package com.example.afinal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    val selectedCityWeather = MutableLiveData<CityWeather>()

    fun sendRetrofitRequest(location: String){
        viewModelScope.launch{
            try{
                val result =
                    GetService.retrofitService.getAppData(location, "metric", "en", WeatherViewModel.API_KEY)
                Log.d("Main", "${result}")
                val cityWeather = CityWeather(
                    result.name,
                    result.main.temp,
                    result.weather[0].description,
                    result.weather[0].icon,
                    result.main.temp_min,
                    result.main.temp_max
                )
                selectedCityWeather.value = cityWeather
                Log.d("Main", cityWeather.toString())
            }catch (e: Exception){
                Log.d("Main", "Fail due to ${e.message}")
            }
        }
    }

    companion object{
        const val API_URL = "https://api.openweathermap.org"
        const val API_KEY = "71308d654f79d5e9595eb0d57b311504"
    }
}