package ir.mahan.mangamotion.utils.constants

const val EMAIL_REGEX =
    "^(?=.{1,256})(?=.{1,64}@)[a-zA-Z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#\$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+\$"

const val DEBUG_TAG = "Debug"
const val BASE_URL = "https://api.jikan.moe/v4/"
const val BASE_AVATAR_URL = "https://api.dicebear.com/9.x/fun-emoji/png?seed="
const val CONNECTION_TIMEOUT = 60L
const val LIMIT_NUMBER = 9

// Data Store
const val USER_STORE_NAME = "user_store_name"
const val USER_UID_KEY = "user_uid_key"
const val USER_EMAIL_KEY = "USER_STORE_NAME"