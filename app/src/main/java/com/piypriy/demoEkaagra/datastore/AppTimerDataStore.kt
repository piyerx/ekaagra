package com.piypriy.demoEkaagra.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.piypriy.demoEkaagra.AppTimerList
import java.io.InputStream
import java.io.OutputStream

object AppTimerSerializer : Serializer<AppTimerList> {
    override val defaultValue: AppTimerList = AppTimerList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppTimerList {
        return try {
            AppTimerList.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read AppTimerList", e)
        }
    }

    override suspend fun writeTo(t: AppTimerList, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.appTimerDataStore by dataStore(
    fileName = "app_timer.pb",
    serializer = AppTimerSerializer
)
