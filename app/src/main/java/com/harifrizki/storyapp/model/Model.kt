package com.harifrizki.storyapp.model

import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.harifrizki.storyapp.utils.EMPTY_STRING
import com.harifrizki.storyapp.utils.MenuCode
import com.harifrizki.storyapp.utils.MenuCode.MENU_NONE
import com.harifrizki.storyapp.utils.ZERO
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Registration(
    @field:SerializedName("name") var name: String? = EMPTY_STRING,
    @field:SerializedName("email") var email: String? = EMPTY_STRING,
    @field:SerializedName("password") var password: String? = EMPTY_STRING
) : Parcelable {
    companion object {
        fun jsonObject(
            registration: Registration
        ): JsonObject {
            return JsonObject().apply {
                addProperty(registration::name.name, registration.name)
                addProperty(registration::email.name, registration.email)
                addProperty(registration::password.name, registration.password)
            }
        }
    }
}

@Parcelize
data class Login(
    @field:SerializedName("email") var email: String? = EMPTY_STRING,
    @field:SerializedName("password") var password: String? = EMPTY_STRING
) : Parcelable {
    companion object {
        fun jsonObject(
            login: Login
        ): JsonObject {
            return JsonObject().apply {
                addProperty(login::email.name, login.email)
                addProperty(login::password.name, login.password)
            }
        }
    }
}

@Parcelize
data class AddNewStory(
    @field:SerializedName("description") var description: String? = EMPTY_STRING,
    @field:SerializedName("photo") var photo: File? = null,
    @field:SerializedName("lat") var lat: Float? = 0.0F,
    @field:SerializedName("long") var long: Float? = 0.0F
) : Parcelable {
    companion object {
        fun jsonObject(
            addNewStory: AddNewStory
        ): JsonObject {
            return JsonObject().apply {
                addProperty(addNewStory::description.name, addNewStory.description)
                addProperty(addNewStory::lat.name, addNewStory.lat)
                addProperty(addNewStory::long.name, addNewStory.long)
            }
        }
    }
}

@Parcelize
data class Story(
    @field:SerializedName("id") var id: String? = EMPTY_STRING,
    @field:SerializedName("name") var name: String? = EMPTY_STRING,
    @field:SerializedName("description") var description: String? = EMPTY_STRING,
    @field:SerializedName("photoUrl") var photoUrl: String? = EMPTY_STRING,
    var photo: File? = null,
    @field:SerializedName("createdAt") var createdAt: String? = EMPTY_STRING,
    @field:SerializedName("lat") var lat: Float? = 0.0F,
    @field:SerializedName("lon") var long: Float? = 0.0F
) : Parcelable

@Parcelize
data class Menu(
    var menuCode: MenuCode?,
    var name: String? = null,
    var useIcon: Int? = null,
    var visibility: Boolean? = null,
    var iconColor: Int? = null,
    var nameColor: Int? = null
) : Parcelable {
    constructor() : this(
        MENU_NONE, EMPTY_STRING, ZERO, false, ZERO, ZERO
    )
}