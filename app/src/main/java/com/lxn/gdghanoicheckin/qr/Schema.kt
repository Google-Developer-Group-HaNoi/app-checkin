package com.lxn.gdghanoicheckin.qr

enum class BarcodeSchema {
    APP,
    BOOKMARK,
    CRYPTOCURRENCY,
    EMAIL,
    GEO,
    GOOGLE_MAPS,
    MMS,
    MECARD,
    OTP_AUTH,
    PHONE,
    SMS,
    URL,
    VEVENT,
    VCARD,
    WIFI,
    YOUTUBE,
    OTHER;
}

interface Schema {
    val schema: BarcodeSchema
    fun toFormattedText(): String
    fun toBarcodeText(): String
}