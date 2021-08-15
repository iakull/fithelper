package com.iakull.fithelper.data.local

import androidx.room.TypeConverter
import com.iakull.fithelper.util.toDate
import com.iakull.fithelper.util.toIsoString
import java.util.*

class Converters {

    @TypeConverter
    fun toDate(value: String?) = value?.toDate()

    @TypeConverter
    fun fromDate(date: Date?) = date?.toIsoString()
}