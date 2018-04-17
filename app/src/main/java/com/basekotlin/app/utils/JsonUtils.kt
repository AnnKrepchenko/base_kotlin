package com.basekotlin.app.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object JsonUtils {

    fun toJson(jsonObject: JSONObject): JsonObject {
        val jsonParser = JsonParser()
        return jsonParser.parse(jsonObject.toString()) as JsonObject
    }

    fun parseString(`object`: JSONObject?, key: String): String? {
        var `val`: String? = null
        if (`object` != null) {
            try {
                if (`object`.has(key))
                    `val` = `object`.getString(key)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return `val`
    }

    fun parseBoolean(`object`: JSONObject?, key: String): Boolean {
        var `val` = java.lang.Boolean.FALSE
        if (`object` != null) {
            try {
                if (`object`.has(key))
                    `val` = `object`.getBoolean(key)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return `val`
    }


    fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }

        }

        return true
    }
}
