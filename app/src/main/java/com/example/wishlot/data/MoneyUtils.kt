package com.example.wishlot.data

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object MoneyUtils {

    /** Парсит ввод в рублях (целые или с копейками) → minor units (копейки). */
    fun parseRublesInput(input: String): Long? {
        val normalized = input.trim()
            .replace(" ", "")
            .replace(",", ".")
        if (normalized.isEmpty()) return null
        val value = normalized.toBigDecimalOrNull() ?: return null
        if (value.signum() <= 0) return null
        return value.multiply(java.math.BigDecimal(100)).longValueExact()
    }

    fun formatMinor(minor: Long, locale: Locale = Locale.getDefault()): String {
        val amount = minor / 100.0
        val format = NumberFormat.getCurrencyInstance(locale)
        try {
            format.currency = Currency.getInstance("RUB")
        } catch (_: IllegalArgumentException) {
            // locale without RUB — use default currency
        }
        return format.format(amount)
    }

    fun minorToDisplayInput(minor: Long): String {
        if (minor <= 0) return ""
        val rubles = minor / 100
        return rubles.toString()
    }
}
