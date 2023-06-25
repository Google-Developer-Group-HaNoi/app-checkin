package com.lxn.gdghanoicheckin.utils

import com.google.zxing.Result


object BarcodeParser {

    fun parseResult(result: Result): String {
        return result.text
    }

}