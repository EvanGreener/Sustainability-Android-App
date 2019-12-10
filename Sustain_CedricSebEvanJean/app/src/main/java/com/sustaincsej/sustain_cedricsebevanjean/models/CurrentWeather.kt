package com.sustaincsej.sustain_cedricsebevanjean.models

/**
 * Classes used to convert json to object for the weather api.
 *
 * @author Jean Robatto
 */
data class CurrentWeather(val coord: Coord, val weather: Array<Weather>, val base: String, val main: Main,
                            val wind: Wind, val rain: Rain, val dt: Int, val sys: Sys, val timezone: Int,
                                val id: Int, val name: String, val cod: Int)

data class Coord(val lon: Double, val lat: Double)
data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Main(val temp: Double, val pressure: Double, val humidity: Double, val temp_min: Double,
                    val temp_max: Double, val sea_level: Double, val grnd_level: Double)
data class Wind(val speed: Double, val deg: Double)
data class Rain(val _3h: Double)
data class Clouds(val all: Int)
data class Sys(val sunrise: Int, val sunset: Int)