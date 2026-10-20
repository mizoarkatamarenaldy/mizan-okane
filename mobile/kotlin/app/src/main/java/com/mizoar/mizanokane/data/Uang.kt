package com.mizoar.mizanokane.data

import java.math.BigDecimal
import java.math.RoundingMode
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
