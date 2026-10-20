package com.mizoar.mizanokane.domain

import java.util.Calendar
import java.util.Locale

/**
 * Format nama bulan dalam bahasa Indonesia sesuai web (desktop/src/js/app.js).
 */
val INDO_MONTHS_SHORT = listOf(
    "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
    "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
)

val INDO_MONTHS_FULL = listOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
)

fun getCurrentDateString(): String {
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH) + 1
    val d = cal.get(Calendar.DAY_OF_MONTH)
    return String.format(Locale.ROOT, "%04d-%02d-%02d", y, m, d)
}

fun getCurrentYearMonthString(): String {
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH) + 1
    return String.format(Locale.ROOT, "%04d-%02d", y, m)
}

fun getCurrentTimeString(): String {
    val cal = Calendar.getInstance()
    val h = cal.get(Calendar.HOUR_OF_DAY)
    val m = cal.get(Calendar.MINUTE)
    return String.format(Locale.ROOT, "%02d:%02d", h, m)
}

/**
 * Mengubah string "YYYY-MM" menjadi nama lengkap, misal "Oktober 2026".
 */
fun formatMonthYear(yearMonth: String): String {
    val parts = yearMonth.split('-')
    if (parts.size < 2) return yearMonth
    val y = parts[0]
    val m = parts[1].toIntOrNull() ?: return yearMonth
    val monthName = INDO_MONTHS_FULL.getOrNull(m - 1) ?: return yearMonth
    return "$monthName $y"
}

/**
 * Mengubah string "YYYY-MM" menjadi nama pendek, misal "Okt 2026".
 */
fun formatMonthYearShort(yearMonth: String): String {
    val parts = yearMonth.split('-')
    if (parts.size < 2) return yearMonth
    val y = parts[0]
    val m = parts[1].toIntOrNull() ?: return yearMonth
    val monthName = INDO_MONTHS_SHORT.getOrNull(m - 1) ?: return yearMonth
    return "$monthName $y"
}

/**
 * Format tanggal lengkap tampilan, misal "20 Okt 2026".
 */
fun formatDateDisplay(dateString: String): String {
    val parts = dateString.split('-')
    if (parts.size < 3) return dateString
    val y = parts[0]
    val m = parts[1].toIntOrNull() ?: return dateString
    val d = parts[2].toIntOrNull() ?: return dateString
    val mShort = INDO_MONTHS_SHORT.getOrNull(m - 1) ?: return dateString
    return "$d $mShort $y"
}

/**
 * Format judul grup tanggal di Riwayat sesuai web (lines 2682-2710 desktop/src/js/app.js).
 * - "Hari ini"
 * - "Kemarin"
 * - "12 Okt" (jika tahun berjalan)
 * - "12 Okt 2025" (jika tahun berbeda)
 */
fun formatGroupDate(dateString: String, todayStr: String = getCurrentDateString()): String {
    if (dateString.isBlank()) return "Lainnya"
    val parts = dateString.split('-')
    if (parts.size < 3) return dateString
    val y = parts[0].toIntOrNull() ?: return dateString
    val m = parts[1].toIntOrNull() ?: return dateString
    val d = parts[2].toIntOrNull() ?: return dateString

    val todayParts = todayStr.split('-')
    if (todayParts.size < 3) return formatDateDisplay(dateString)
    val todayY = todayParts[0].toIntOrNull() ?: return formatDateDisplay(dateString)
    val todayM = todayParts[1].toIntOrNull() ?: return formatDateDisplay(dateString)
    val todayD = todayParts[2].toIntOrNull() ?: return formatDateDisplay(dateString)

    if (y == todayY && m == todayM && d == todayD) {
        return "Hari ini"
    }

    val cal = Calendar.getInstance()
    cal.set(todayY, todayM - 1, todayD)
    cal.add(Calendar.DAY_OF_MONTH, -1)
    val yestY = cal.get(Calendar.YEAR)
    val yestM = cal.get(Calendar.MONTH) + 1
    val yestD = cal.get(Calendar.DAY_OF_MONTH)

    if (y == yestY && m == yestM && d == yestD) {
        return "Kemarin"
    }

    val mShort = INDO_MONTHS_SHORT.getOrNull(m - 1) ?: ""
    return if (y == todayY) {
        "$d $mShort"
    } else {
        "$d $mShort $y"
    }
}

/**
 * Format waktu tampilan 12 jam atau 24 jam sesuai web (lines 2723-2736 desktop/src/js/app.js).
 */
fun formatTimeDisplay(timeStr: String?, is24Hour: Boolean = true): String {
    if (timeStr.isNullOrBlank()) return ""
    val parts = timeStr.trim().split(':')
    if (parts.size < 2) return timeStr
    val h = parts[0].toIntOrNull() ?: return timeStr
    val m = parts[1].take(2).padStart(2, '0')

    return if (is24Hour) {
        String.format(Locale.ROOT, "%02d:%s", h, m)
    } else {
        val ampm = if (h >= 12) "PM" else "AM"
        val h12 = if (h % 12 == 0) 12 else h % 12
        String.format(Locale.ROOT, "%02d:%s %s", h12, m, ampm)
    }
}

/**
 * Format waktu tampilan berdasarkan nilai setting format jam ("24" atau "12").
 */
fun formatTimeDisplay(timeStr: String?, timeFormat: String?): String {
    return formatTimeDisplay(timeStr, timeFormat != "12")
}

/**
 * Menggeser bulan sebanyak [delta], tidak boleh melewati [maxMonth] jika [delta] > 0.
 */
fun shiftMonth(yearMonth: String, delta: Int, maxMonth: String? = getCurrentYearMonthString()): String {
    val parts = yearMonth.split('-')
    if (parts.size < 2) return yearMonth
    var y = parts[0].toIntOrNull() ?: return yearMonth
    var m = parts[1].toIntOrNull() ?: return yearMonth

    m += delta
    if (m < 1) {
        m = 12
        y--
    } else if (m > 12) {
        m = 1
        y++
    }
    val nextStr = String.format(Locale.ROOT, "%04d-%02d", y, m)
    return if (delta > 0 && maxMonth != null && nextStr > maxMonth) {
        maxMonth
    } else {
        nextStr
    }
}
