package io.github.sponzio23.putneyweather

import io.realm.Realm
import io.realm.internal.platform.runBlocking
import io.realm.log.LogLevel
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.sync.SyncConfiguration
import io.realm.mongodb.syncSession
import io.realm.query
import kotlinx.datetime.Instant
import kotlin.time.Duration

class WeatherRepository {

    private var realm: Realm

    private val app: App = App.create("putneyweatherapp-ebbkq")

    init {
        realm = runBlocking {
            val user = app.login(Credentials.anonymous())

            val config = SyncConfiguration.Builder(
                user = user,
                partitionValue = "data",
                schema = setOf(WeatherData::class)
            )
                .log(LogLevel.ALL)
                .build()

            Realm.open(config)
        }
    }

    fun getLatestData(): WeatherData? {
        // update from the server
        runBlocking {
            realm.syncSession.downloadAllServerChanges()
        }

        // query and return
        var queryResult: List<WeatherData>? = null
        runBlocking {
            queryResult = realm.query<WeatherData>().find()
        }

        println("refreshed data $queryResult")

        queryResult ?: return null

        var latestData: WeatherData? = null
        var largestInstant: Instant? = null
        queryResult!!.forEach {
            val itInstant = Instant.fromEpochMilliseconds(it.timestamp.toLong())
            if (largestInstant == null) {
                largestInstant = itInstant
                return@forEach
            }
            if (itInstant > largestInstant!!) {
                latestData = it
            }

        }

        return latestData
    }
}
