package io.github.sponzio23.putneyweather.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.sponzio23.putneyweather.WeatherData
import io.github.sponzio23.putneyweather.WeatherRepository
import io.realm.internal.platform.runBlocking
import java.util.*
import kotlin.concurrent.schedule
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        tv.text = "Welcome to Putney weather! Loading data..."

        val data: WeatherData? = WeatherRepository().getLatestData()

        if (data == null) {
            tv.text = "Unable to find weather data."
        } else {
            tv.text = "Latest weather data as of ${data.timestamp}\n" +
                    "Temperature: ${data.temp}\n" +
                    "Humidity: ${data.humidity}"
        }

        println("created stuff")
    }

    @SuppressLint("SetTextI18n")
    fun refresh(view: View) {
        val tv: TextView = findViewById(R.id.text_view)
        tv.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

        tv.text = "Refreshing..."

        var data: WeatherData? = WeatherRepository().getLatestData()
        runBlocking {
            data = WeatherRepository().getLatestData()
        }

        println("waiting")
        Timer().schedule(2000) {
            println("two seconds have passed")
        }
        if (data == null) {
            tv.text = "Unable to find weather data."
        } else {
            tv.text = "Latest weather data as of ${data?.timestamp}\n" +
                    "Temperature: ${data?.temp}\n" +
                    "Humidity: ${data?.humidity}"
        }
    }
}
