package com.harifrizki.storyapp.utils

import android.Manifest

const val PREF_NAME = ".preferences"

const val EMPTY_STRING = ""
const val SPACE_STRING = " "
const val UNDER_LINE_STRING = "_"
const val HYPHEN_STRING = "-"
const val SPAN_REGEX = "\\[.*?\\]"

const val IMAGE_FORMAT_JPG = ".jpg"
const val IMAGE_FORMAT_GALLERY = "image/*"
const val FILENAME_FORMAT = "dd-MMM-yyyy"

const val WAIT_FOR_2000 = 2000L

val APP_PERMISSION_GET_IMAGE: Array<String> = arrayOf(
    Manifest.permission.CAMERA
)

const val ZERO = 0

const val ERROR_STATE = "ERROR_STATE"
const val GENERAL_RESPONSE = "GENERAL_RESPONSE"

const val LOTTIE_ERROR_JSON = "error.json"
const val LOTTIE_WARNING_JSON = "warning.json"
const val LOTTIE_INFORMATION_JSON = "information.json"
const val LOTTIE_SUCCESS_JSON = "success.json"
const val LOTTIE_QUESTION_JSON = "question.json"
const val LOTTIE_SEARCH_NOT_FOUND_JSON = "search_not_found.json"

const val MAX_ITEM_LIST_SHIMMER = 4

const val WAS_REGISTRATION = "WAS_REGISTRATION"
const val WAS_SUCCESS_GET_IMAGE = "WAS_SUCCESS_GET_IMAGE"
const val RESULT_CAPTURE_IMAGE = "RESULT_CAPTURE_IMAGE"
const val IS_BACK_FROM_CAMERA = "IS_BACK_FROM_CAMERA"

const val MODEL_LOGIN = "MODEL_LOGIN"