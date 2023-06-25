package com.lxn.gdghanoicheckin.network.cache

import com.lxn.gdghanoicheckin.utils.logError

object EmailCaching {
    private val emailsScanned: ArrayList<String> = ArrayList()
    val listEmailScanned : List<String> get() = emailsScanned

    fun appendListEmail(list: List<String>) {
        logError("Data new -> $list")
        emailsScanned.clear()
        emailsScanned.addAll(list)
        emailsScanned.distinct()
    }

    fun findEmailInList(email: String): Boolean {
        return emailsScanned.contains(email)
    }
}