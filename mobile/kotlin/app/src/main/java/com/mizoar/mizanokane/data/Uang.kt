package com.mizoar.mizanokane.data

import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral

/**
 * Helper konversi representasi nilai uang aplikasi Mizan Okane.
 *
 * KONVENSI SATUAN:
 * Di dalam penyimpanan lokal (Room Entity) dan model Kotlin internal,
 * SEMUA nilai uang disimpan sebagai tipe [Long] dalam satuan seperseratus
 * (1/100 unit mata uang atau sen/cents) untuk seluruh mata uang (IDR, USD, JPY).
 *
 * Contoh:
 * - Rp 15.000 disimpan sebagai 1500000L (15.000 x 100)
 * - USD 49,90 disimpan sebagai 4990L (49,90 x 100)
 * - 100 Yen disimpan sebagai 10000L (100 x 100)
 *
 * Pada batas ekspor/impor JSON, format serialisasi tetap berupa angka numerik biasa
 * identik dengan JSON.stringify di versi web (tanpa desimal untuk bilangan bulat,
 * tanpa trailing zero untuk desimal). Dilarang mengonversi melalui Float atau Double.
 */
object Uang {

    private val SERATUS = BigDecimal("100")

    /**
     * Mengonversi teks representasi angka dari JSON ke [Long] satuan seperseratus (1/100).
     *
     * Aturan:
     * - Nilai dikalikan 100 dengan [RoundingMode.HALF_UP].
     * - Menolak angka yang memiliki lebih dari 2 tempat desimal (melempar [IllegalArgumentException]).
     */
    fun dariJsonContent(content: String): Long {
        val trimmed = content.trim()
        require(trimmed.isNotEmpty()) { "Format angka uang tidak boleh kosong" }
        val bd = BigDecimal(trimmed)
        val stripped = bd.stripTrailingZeros()
        if (stripped.scale() > 2) {
            throw IllegalArgumentException("Nominal uang tidak boleh memiliki lebih dari 2 desimal: '$content'")
        }
        val cents = bd.multiply(SERATUS).setScale(0, RoundingMode.HALF_UP)
        return cents.longValueExact()
    }

    /**
     * Membaca [JsonPrimitive] dari JSON dan mengonversinya ke [Long] satuan seperseratus.
     */
    fun dariJsonPrimitive(primitive: JsonPrimitive): Long {
        return dariJsonContent(primitive.content)
    }

    /**
     * Mengonversi [Long] satuan seperseratus ke string desimal polos untuk JSON.
     *
     * Contoh:
     * - 10000L   -> "100" (bukan "100.0")
     * - 4990L    -> "49.9" (bukan "49.90")
     * - 1299L    -> "12.99"
     * - 1L       -> "0.01"
     * - 0L       -> "0"
     * - -500000L -> "-5000"
     */
    fun kePlainString(cents: Long): String {
        val bd = BigDecimal.valueOf(cents).movePointLeft(2).stripTrailingZeros()
        return if (bd.compareTo(BigDecimal.ZERO) == 0) "0" else bd.toPlainString()
    }

    /**
     * Menghasilkan [JsonPrimitive] berupa [JsonUnquotedLiteral] agar tercetak
     * sebagai angka numerik murni tanpa tanda kutip di output JSON kotlinx.serialization.
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun keJsonLiteral(cents: Long): JsonPrimitive {
        return JsonUnquotedLiteral(kePlainString(cents))
    }

    /**
     * Memformat bilangan bulat satuan mata uang dengan titik pemisah ribuan.
     * Contoh: 15000L -> "15.000", -5000L -> "-5.000", 0L -> "0".
     */
    fun formatNumber(units: Long): String {
        val isNeg = units < 0
        val absUnits = if (units == Long.MIN_VALUE) Long.MAX_VALUE else kotlin.math.abs(units)
        val str = absUnits.toString()
        val sb = StringBuilder()
        val len = str.length
        for (i in 0 until len) {
            sb.append(str[i])
            val remaining = len - i - 1
            if (remaining > 0 && remaining % 3 == 0) {
                sb.append('.')
            }
        }
        return if (isNeg) "-$sb" else sb.toString()
    }

    /**
     * Memformat nilai uang ([cents] / 100) lengkap dengan simbol mata uang.
     * - IDR: "Rp 15.000" / "-Rp 15.000"
     * - JPY: "¥ 1.500" / "-¥ 1.500"
     * - USD: "$ 49,90" / "-$ 49,90"
     */
    fun formatCurrency(cents: Long, currency: String = "IDR"): String {
        val isUsd = currency == "USD"
        val isJpy = currency == "JPY"
        val prefix = when {
            isUsd -> "$ "
            isJpy -> "¥ "
            else -> "Rp "
        }
        val isNeg = cents < 0
        val absCents = if (cents == Long.MIN_VALUE) Long.MAX_VALUE else kotlin.math.abs(cents)

        val formattedNumber = if (isUsd) {
            val intPart = absCents / 100
            val decPart = absCents % 100
            val intFormatted = formatNumber(intPart)
            val decFormatted = String.format(Locale.ROOT, "%02d", decPart)
            "$intFormatted,$decFormatted"
        } else {
            val units = absCents / 100
            formatNumber(units)
        }

        return if (isNeg) "-$prefix$formattedNumber" else "$prefix$formattedNumber"
    }

    /**
     * Memformat angka untuk nilai awal di kolom input form transaksi.
     * IDR/JPY: "15.000"
     * USD: "49,90" (atau desimal 2 digit bila ada desimal)
     */
    fun formatAmountForInput(cents: Long, currency: String = "IDR"): String {
        if (cents == 0L) return ""
        val absCents = if (cents == Long.MIN_VALUE) Long.MAX_VALUE else kotlin.math.abs(cents)
        return if (currency == "USD") {
            val intPart = absCents / 100
            val decPart = absCents % 100
            val intFormatted = formatNumber(intPart)
            if (decPart == 0L) {
                intFormatted
            } else {
                val decFormatted = String.format(Locale.ROOT, "%02d", decPart)
                "$intFormatted,$decFormatted"
            }
        } else {
            formatNumber(absCents / 100)
        }
    }

    /**
     * Mengonversi string input dari pengguna ke [Long] dalam satuan seperseratus.
     * Contoh:
     * - "15.000" (IDR) -> 1500000L
     * - "49,90" (USD)  -> 4990L
     * - "49,9" (USD)   -> 4990L
     * - "100" (USD)    -> 10000L
     * - "0,01" (USD)   -> 1L
     */
    fun parseAmountInput(input: String, currency: String = "IDR"): Long {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return 0L

        return if (currency == "USD") {
            val hasComma = trimmed.contains(',')
            val hasDot = trimmed.contains('.')

            val intDigits: String
            val decDigits: String

            if (hasComma && hasDot) {
                if (trimmed.lastIndexOf(',') > trimmed.lastIndexOf('.')) {
                    val parts = trimmed.split(',')
                    intDigits = parts[0].filter { it.isDigit() }
                    decDigits = parts.getOrNull(1)?.filter { it.isDigit() } ?: ""
                } else {
                    val parts = trimmed.split('.')
                    intDigits = parts[0].filter { it.isDigit() }
                    decDigits = parts.getOrNull(1)?.filter { it.isDigit() } ?: ""
                }
            } else if (hasComma) {
                val parts = trimmed.split(',')
                intDigits = parts[0].filter { it.isDigit() }
                decDigits = parts.getOrNull(1)?.filter { it.isDigit() } ?: ""
            } else if (hasDot) {
                val lastDotIdx = trimmed.lastIndexOf('.')
                val afterDot = trimmed.substring(lastDotIdx + 1).filter { it.isDigit() }
                if (afterDot.length in 1..2 && trimmed.count { it == '.' } == 1) {
                    intDigits = trimmed.substring(0, lastDotIdx).filter { it.isDigit() }
                    decDigits = afterDot
                } else {
                    intDigits = trimmed.filter { it.isDigit() }
                    decDigits = ""
                }
            } else {
                intDigits = trimmed.filter { it.isDigit() }
                decDigits = ""
            }

            val intVal = intDigits.toLongOrNull() ?: 0L
            val decVal = when (decDigits.length) {
                0 -> 0L
                1 -> (decDigits.toLongOrNull() ?: 0L) * 10L
                else -> decDigits.take(2).toLongOrNull() ?: 0L
            }
            intVal * 100L + decVal
        } else {
            val digits = trimmed.filter { it.isDigit() }
            val units = digits.toLongOrNull() ?: 0L
            units * 100L
        }
    }

    /**
     * Menghitung persentase progres pemakaian budget secara presisi integer.
     */
    fun hitungPersen(terpakaiCents: Long, budgetCents: Long): Int {
        if (budgetCents <= 0L) return 0
        return ((terpakaiCents * 100L + budgetCents / 2) / budgetCents).toInt()
    }

    /**
     * Menghitung fraksi progres antara 0.0f hingga 1.0f untuk bar indikator.
     */
    fun hitungProgressFraction(terpakaiCents: Long, budgetCents: Long): Float {
        if (budgetCents <= 0L) return 0f
        val ratio = terpakaiCents.toDouble() / budgetCents.toDouble()
        return ratio.coerceIn(0.0, 1.0).toFloat()
    }

    fun getWalletCurrency(walletId: String?, wallets: List<WalletEntity>): String {
        val id = walletId ?: DefaultData.DEFAULT_WALLET_ID
        val w = wallets.find { it.id == id }
        return w?.currency?.takeIf { it.isNotBlank() } ?: "IDR"
    }

    fun getTransactionCurrency(tx: TransactionEntity?, wallets: List<WalletEntity>): String {
        if (tx == null) return "IDR"
        return getWalletCurrency(tx.walletId, wallets)
    }
}

/**
 * Serializer kustom kotlinx.serialization untuk field bernilai uang.
 * Mengonversi antara literal angka JSON desimal dan [Long] internal dalam satuan 1/100.
 */
object UangSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Uang", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Long {
        return if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            require(element is JsonPrimitive) { "Nilai uang harus berupa angka primitif" }
            Uang.dariJsonPrimitive(element)
        } else {
            Uang.dariJsonContent(decoder.decodeString())
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Long) {
        if (encoder is JsonEncoder) {
            encoder.encodeJsonElement(Uang.keJsonLiteral(value))
        } else {
            encoder.encodeString(Uang.kePlainString(value))
        }
    }
}
