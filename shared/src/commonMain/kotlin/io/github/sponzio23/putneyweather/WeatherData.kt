package io.github.sponzio23.putneyweather

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class WeatherData: RealmObject {
    @PrimaryKey
    var _id: String = "primary"
    var _partition: String = "data"
    var humidity: Double = 0.0
    var pressure: Double = 0.0
    var rainfall: Double = 0.0
    var temp: Double = 0.0
    var timestamp: String = ""
    var wind_direction: Double = 0.0
    var wind_gust: Double = 0.0
    var wind_speed: Double = 0.0
}