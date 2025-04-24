package com.piypriy.demoEkaagra.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.piypriy.demoEkaagra.ReminderList
import java.io.InputStream
import java.io.OutputStream

// Serializer to handle read/write from file
object ReminderListSerializer : Serializer<ReminderList> {
    override val defaultValue: ReminderList = ReminderList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ReminderList {
        try {
            return ReminderList.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ReminderList, output: OutputStream) {
        t.writeTo(output)
    }
}

// Extension property to access DataStore<ReminderList>
val Context.reminderDataStore by dataStore(
    fileName = "reminders.pb",
    serializer = ReminderListSerializer
)
