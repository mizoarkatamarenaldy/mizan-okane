package com.mizoar.mizanokane.data.json

import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.UangSerializer
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

/**
 * Data Transfer Object untuk serialisasi/deserialisasi transaksi pada file JSON cadangan.
 */
@Serializable
data class TransactionDto(
    val id: String,
    val type: String,
    @Serializable(with = UangSerializer::class)
    val amount: Long,
    val date: String,
    val time: String? = null,
    val category: String? = null,
    val subcategory: String? = null,
    val walletId: String? = null,
    val toWalletId: String? = null,
    val note: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val deleted: Boolean = false,
    val dummy: Boolean = false
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id.trim(),
            type = type,
            amount = amount,
            date = date.trim(),
            time = time,
            category = category,
            subcategory = subcategory?.trim(),
            walletId = walletId,
            toWalletId = toWalletId,
            note = note,
            createdAt = if (createdAt > 0L) createdAt else System.currentTimeMillis(),
            updatedAt = if (updatedAt > 0L) updatedAt else createdAt,
            deleted = deleted,
            dummy = dummy
        )
    }

    companion object {
        fun fromEntity(entity: TransactionEntity): TransactionDto {
            return TransactionDto(
                id = entity.id,
                type = entity.type,
                amount = entity.amount,
                date = entity.date,
                time = entity.time,
                category = entity.category,
                subcategory = entity.subcategory,
                walletId = entity.walletId,
                toWalletId = entity.toWalletId,
                note = entity.note,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                deleted = entity.deleted,
                dummy = entity.dummy
            )
        }
    }
}

/**
 * Data Transfer Object untuk serialisasi/deserialisasi dompet pada file JSON cadangan.
 */
@Serializable
data class WalletDto(
    val id: String,
    val name: String,
    val currency: String = "IDR",
    val order: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val deleted: Boolean = false
) {
    fun toEntity(): WalletEntity {
        val cleanCurrency = if (currency == "JPY" || currency == "USD") currency else "IDR"
        return WalletEntity(
            id = id.trim(),
            name = name.trim(),
            currency = cleanCurrency,
            order = order,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deleted = deleted
        )
    }

    companion object {
        fun fromEntity(entity: WalletEntity): WalletDto {
            return WalletDto(
                id = entity.id,
                name = entity.name,
                currency = entity.currency,
                order = entity.order,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                deleted = entity.deleted
            )
        }
    }
}

/**
 * Serializer kustom untuk [BudgetDto] agar mendukung konversi uang 1/100
 * dan output angka unquoted literal di JSON.
 */
object BudgetDtoSerializer : KSerializer<BudgetDto> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BudgetDto")

    override fun deserialize(decoder: Decoder): BudgetDto {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("BudgetDto hanya mendukung deserialisasi JSON")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        val itemsMap = HashMap<String, Long>()
        jsonObject["items"]?.jsonObject?.forEach { (key, elem) ->
            if (elem is JsonPrimitive) {
                itemsMap[key] = Uang.dariJsonPrimitive(elem)
            }
        }

        val subsMap = HashMap<String, Map<String, Long>>()
        jsonObject["subcategories"]?.jsonObject?.forEach { (parentCat, subGroup) ->
            if (subGroup is JsonObject) {
                val subItems = HashMap<String, Long>()
                subGroup.forEach { (subKey, elem) ->
                    if (elem is JsonPrimitive) {
                        subItems[subKey] = Uang.dariJsonPrimitive(elem)
                    }
                }
                if (subItems.isNotEmpty()) {
                    subsMap[parentCat] = subItems
                }
            }
        }

        val updatedAt = jsonObject["updatedAt"]?.jsonPrimitive?.longOrNull ?: 0L
        return BudgetDto(items = itemsMap, subcategories = subsMap, updatedAt = updatedAt)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: BudgetDto) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw IllegalStateException("BudgetDto hanya mendukung serialisasi JSON")

        val itemsObject = JsonObject(value.items.mapValues { Uang.keJsonLiteral(it.value) })
        val subsObject = JsonObject(value.subcategories.mapValues { (_, subs) ->
            JsonObject(subs.mapValues { Uang.keJsonLiteral(it.value) })
        })

        val rootMap = linkedMapOf<String, kotlinx.serialization.json.JsonElement>(
            "items" to itemsObject,
            "subcategories" to subsObject,
            "updatedAt" to JsonPrimitive(value.updatedAt)
        )
        jsonEncoder.encodeJsonElement(JsonObject(rootMap))
    }
}

/**
 * Data Transfer Object untuk serialisasi/deserialisasi budget pada file JSON cadangan.
 */
@Serializable(with = BudgetDtoSerializer::class)
data class BudgetDto(
    val items: Map<String, Long> = emptyMap(),
    val subcategories: Map<String, Map<String, Long>> = emptyMap(),
    val updatedAt: Long = 0L
) {
    fun toBudgetsData(): BudgetsData {
        return BudgetsData(
            items = items,
            subcategories = subcategories,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromBudgetsData(data: BudgetsData): BudgetDto {
            return BudgetDto(
                items = data.items,
                subcategories = data.subcategories,
                updatedAt = data.updatedAt
            )
        }
    }
}

/**
 * Data Transfer Object untuk serialisasi/deserialisasi pengaturan pada file JSON cadangan.
 */
@Serializable
data class SettingsDto(
    val theme: String = "system",
    val timeFormat: String = "24",
    val updatedAt: Long = 0L
) {
    fun toEntity(): SettingsEntity {
        val cleanTheme = if (theme == "light" || theme == "dark" || theme == "system") theme else "system"
        val cleanTimeFormat = if (timeFormat == "12") "12" else "24"
        return SettingsEntity(
            id = 1,
            theme = cleanTheme,
            timeFormat = cleanTimeFormat,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromEntity(entity: SettingsEntity): SettingsDto {
            return SettingsDto(
                theme = entity.theme,
                timeFormat = entity.timeFormat,
                updatedAt = entity.updatedAt
            )
        }
    }
}

/**
 * Objek root dokumen cadangan JSON versi 2 sesuai dev/SCHEMA.md bagian 3.
 */
@Serializable
data class BackupRootDto(
    val app: String = "mizan-okane",
    val version: Int = 2,
    val schemaVersion: Int = 2,
    val exportedAt: String,
    val transactions: List<TransactionDto> = emptyList(),
    val wallets: List<WalletDto> = emptyList(),
    val categories: CategoriesData? = null,
    val budgets: BudgetDto? = null,
    val settings: SettingsDto? = null
)
