package com.basekotlin.app.utils

import android.text.format.DateFormat

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by macbook on 17.09.16.
 */
object CalendarUtils {

    val today: String
        get() = calDateToString(Calendar.getInstance())

    fun calDateToString(calendar: Calendar): String {
        return DateFormat.format("yyyy-MM-dd", calendar).toString()
    }

    fun calTimeToString(calendar: Calendar): String {
        return DateFormat.format("HH:mm:ss", calendar).toString()
    }

    fun dateToCal(date: String): Calendar {

        //        "fromDate":"2016-09-23"

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        try {
            cal.time = sdf.parse(date)// all done
        } catch (e: Exception) {
            val exception = Exception("Can't parse date " + date)
            exception.printStackTrace()
        }

        return cal
    }

    fun timeToCal(date: String): Calendar {

        //        "fromTime":"09:00:00"

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        try {
            cal.time = sdf.parse(date)// all done
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return cal

    }

    fun timestampToString(timestamp: Long): String {
        val calendar = timestampToCal(timestamp)
        //        Calendar today = Calendar.getInstance();
        //        Calendar yesterday = Calendar.getInstance();
        //        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        //        if (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
        //            return "Today";
        //        } else if (calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
        //            return "Yesterday";
        //        } else
        return DateFormat.format("yyyy-MMM-dd HH:mm:ss", calendar).toString()
    }

    fun timestampToCal(timestamp: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar
    }

    fun formatServerTime(time: String): String {
        val calendar = timeToCal(time)
        return calTimeToString(calendar)
    }

    fun calculateAge(date: String): Int {
        val calendar = dateToCal(date)
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
    }

    fun getDateOfMessage(isShowTodayText: Boolean, isShowYesterdayText: Boolean, calendarFromMessage: Calendar): String {
        return if (isShowTodayText) {
            "Today"
        } else if (isShowYesterdayText) {
            "Yesterday"
        } else {
            calDateToString(calendarFromMessage)
        }
    }

    fun createTimestamp(): Long {
        return System.currentTimeMillis()
    }

    fun createTimestamp(timetoken: Long): Long {
        return timetoken / 10000L
    }

    fun createCurrentTimetoken(): Long {
        return System.currentTimeMillis() * 10000L
    }

    fun createTimetoken(timestamp: Long): Long {
        return timestamp * 10000L
    }
}
