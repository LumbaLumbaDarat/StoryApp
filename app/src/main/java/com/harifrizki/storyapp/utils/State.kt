package com.harifrizki.storyapp.utils

enum class ResponseStatus {
    SUCCESS,
    EMPTY,
    ERROR,
    LOADING
}

enum class ErrorState {
    IS_NO_NETWORK,
    IS_ERROR_RESPONSE_API
}

enum class MenuCode {
    MENU_NONE,
    MENU_CAMERA,
    MENU_GALLERY
}

enum class NotificationType {
    NOTIFICATION_ERROR,
    NOTIFICATION_SUCCESS,
    NOTIFICATION_INFORMATION
}