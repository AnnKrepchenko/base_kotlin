package com.basekotlin.app.domain.net.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ann on 2/27/18.
 */
class User {
    var uuid: String? = null
    var email: String? = null
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    @SerializedName("phone_number")
    var phoneNumber: String? = null
}