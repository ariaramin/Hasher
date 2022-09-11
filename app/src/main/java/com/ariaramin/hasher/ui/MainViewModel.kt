package com.ariaramin.hasher.ui

import androidx.lifecycle.ViewModel
import java.security.MessageDigest

class MainViewModel : ViewModel() {

    fun getHash(algorithm: String, text: String): String {
        val byteArray = MessageDigest.getInstance(algorithm).digest(text.toByteArray())
        return toHex(byteArray)
    }

    private fun toHex(byteArray: ByteArray): String {
        return byteArray.joinToString("") { "%02x".format(it) }
    }
}