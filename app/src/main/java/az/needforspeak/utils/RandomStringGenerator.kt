package az.needforspeak.utils

import java.util.*

object RandomStringGenerator {
    fun getRandomString(length: Int = 20): String {
        val upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return StringBuilder().apply {
            val random = Random()
            for (i in 0 until length) {
                val index = random.nextInt(upperAlphabet.length)
                val randomChar = upperAlphabet[index]
                append(randomChar)
            }
        }.toString()
    }
}