package com.aleksej.makaji.listopia.data.room

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}