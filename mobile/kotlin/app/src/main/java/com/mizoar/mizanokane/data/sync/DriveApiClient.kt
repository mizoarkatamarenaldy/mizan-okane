package com.mizoar.mizanokane.data.sync

import com.mizoar.mizanokane.data.json.JsonBackupManager
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.UUID
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

/**
 * Metadata file dari Google Drive REST API v3.
 */
data class DriveFileMeta(
    val id: String,
    val version: Long? = null,
    val modifiedTime: String? = null
)

/**
 * Exception khusus ketika sesi Google Drive kedaluwarsa atau token ditolak (HTTP 401).
 */
class AuthRequiredException(message: String) : Exception(message)

/**
 * Exception khusus ketika server Google Drive menolak permintaan dengan status non-2xx selain 401.
 */
class DriveApiException(val statusCode: Int, message: String) : Exception(message)

/**
 * Klien HTTP untuk berinteraksi dengan Google Drive REST API v3 (appDataFolder).
 * Implementasi murni berbasis [HttpURLConnection] tanpa dependensi library luar.
 * Nama file dan struktur endpoint mengikuti web (desktop/src/js/sync.js).
 */
object DriveApiClient {

    const val DRIVE_API = "https://www.googleapis.com/drive/v3"
    const val DRIVE_UPLOAD = "https://www.googleapis.com/upload/drive/v3"
    const val FILE_NAME = "mizan-okane-data.json"

    /**
     * Mencari file data cadangan [FILE_NAME] di appDataFolder Google Drive.
     * Mengembalikan [DriveFileMeta] berkas teratas jika ada, atau null jika belum pernah dibuat.
     */
    fun findRemoteFile(accessToken: String): DriveFileMeta? {
        val encodedQ = URLEncoder.encode("name = '$FILE_NAME' and trashed = false", "UTF-8")
        val encodedFields = URLEncoder.encode("files(id,version,modifiedTime)", "UTF-8")
        val encodedOrderBy = URLEncoder.encode("modifiedTime desc", "UTF-8")
        val urlString = "$DRIVE_API/files?spaces=appDataFolder&q=$encodedQ&fields=$encodedFields&orderBy=$encodedOrderBy&pageSize=10"

        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer $accessToken")
            conn.setRequestProperty("Accept", "application/json")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            val responseCode = conn.responseCode
            if (responseCode == 401) {
                throw AuthRequiredException("Sesi Google berakhir.")
            }
            if (responseCode !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                val detail = parseErrorMessage(errorBody)
                throw DriveApiException(
                    statusCode = responseCode,
                    message = "Drive menolak permintaan ($responseCode${if (detail.isNotEmpty()) ": $detail" else ""})."
                )
            }

            val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JsonBackupManager.json.parseToJsonElement(responseBody).jsonObject
            val files = root["files"]?.jsonArray
            val first = files?.firstOrNull()?.jsonObject ?: return null
            val id = first["id"]?.jsonPrimitive?.content ?: return null
            val version = first["version"]?.jsonPrimitive?.longOrNull
            val modifiedTime = first["modifiedTime"]?.jsonPrimitive?.content
            return DriveFileMeta(id = id, version = version, modifiedTime = modifiedTime)
        } finally {
            conn.disconnect()
        }
    }

    /**
     * Mengunduh isi mentah file JSON dari Google Drive berdasarkan [fileId].
     */
    fun downloadRemote(fileId: String, accessToken: String): String {
        val url = URL("$DRIVE_API/files/$fileId?alt=media")
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer $accessToken")
            conn.setRequestProperty("Accept", "application/json")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            val responseCode = conn.responseCode
            if (responseCode == 401) {
                throw AuthRequiredException("Sesi Google berakhir.")
            }
            if (responseCode !in 200..299) {
                val errorBody = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                val detail = parseErrorMessage(errorBody)
                throw DriveApiException(
                    statusCode = responseCode,
                    message = "Drive menolak permintaan ($responseCode${if (detail.isNotEmpty()) ": $detail" else ""})."
                )
            }

            return conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }

    /**
     * Mengunggah konten [jsonContent] ke Google Drive.
     * Jika [fileId] ada, melakukan update (PATCH via POST + X-HTTP-Method-Override: PATCH).
     * Jika [fileId] null, membuat file baru di appDataFolder via multipart POST.
     */
    fun uploadRemote(fileId: String?, jsonContent: String, accessToken: String): DriveFileMeta {
        val encodedFields = URLEncoder.encode("id,version,modifiedTime", "UTF-8")

        if (fileId != null) {
            val url = URL("$DRIVE_UPLOAD/files/$fileId?uploadType=media&fields=$encodedFields")
            val conn = url.openConnection() as HttpURLConnection
            try {
                conn.requestMethod = "POST"
                conn.setRequestProperty("X-HTTP-Method-Override", "PATCH")
                conn.setRequestProperty("Authorization", "Bearer $accessToken")
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                conn.doOutput = true

                val bytes = jsonContent.toByteArray(Charsets.UTF_8)
                conn.outputStream.use { it.write(bytes) }

                val responseCode = conn.responseCode
                if (responseCode == 401) {
                    throw AuthRequiredException("Sesi Google berakhir.")
                }
                if (responseCode !in 200..299) {
                    val errorBody = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                    val detail = parseErrorMessage(errorBody)
                    throw DriveApiException(
                        statusCode = responseCode,
                        message = "Drive menolak permintaan ($responseCode${if (detail.isNotEmpty()) ": $detail" else ""})."
                    )
                }

                val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                return parseFileMeta(responseBody)
            } finally {
                conn.disconnect()
            }
        } else {
            val boundary = "mizan-${UUID.randomUUID()}"
            val metadataJson = """{"name":"$FILE_NAME","parents":["appDataFolder"],"mimeType":"application/json"}"""
            val multipartBody = buildString {
                append("--").append(boundary).append("\r\n")
                append("Content-Type: application/json; charset=UTF-8\r\n\r\n")
                append(metadataJson).append("\r\n")
                append("--").append(boundary).append("\r\n")
                append("Content-Type: application/json; charset=UTF-8\r\n\r\n")
                append(jsonContent).append("\r\n")
                append("--").append(boundary).append("--")
            }

            val url = URL("$DRIVE_UPLOAD/files?uploadType=multipart&fields=$encodedFields")
            val conn = url.openConnection() as HttpURLConnection
            try {
                conn.requestMethod = "POST"
                conn.setRequestProperty("Authorization", "Bearer $accessToken")
                conn.setRequestProperty("Content-Type", "multipart/related; boundary=$boundary")
                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                conn.doOutput = true

                val bytes = multipartBody.toByteArray(Charsets.UTF_8)
                conn.outputStream.use { it.write(bytes) }

                val responseCode = conn.responseCode
                if (responseCode == 401) {
                    throw AuthRequiredException("Sesi Google berakhir.")
                }
                if (responseCode !in 200..299) {
                    val errorBody = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                    val detail = parseErrorMessage(errorBody)
                    throw DriveApiException(
                        statusCode = responseCode,
                        message = "Drive menolak permintaan ($responseCode${if (detail.isNotEmpty()) ": $detail" else ""})."
                    )
                }

                val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                return parseFileMeta(responseBody)
            } finally {
                conn.disconnect()
            }
        }
    }

    private fun parseErrorMessage(errorJson: String): String {
        if (errorJson.isBlank()) return ""
        return try {
            val root = JsonBackupManager.json.parseToJsonElement(errorJson).jsonObject
            val errorObj = root["error"]?.jsonObject
            errorObj?.get("message")?.jsonPrimitive?.content ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    private fun parseFileMeta(jsonString: String): DriveFileMeta {
        val root = JsonBackupManager.json.parseToJsonElement(jsonString).jsonObject
        val id = root["id"]?.jsonPrimitive?.content ?: ""
        val version = root["version"]?.jsonPrimitive?.longOrNull
        val modifiedTime = root["modifiedTime"]?.jsonPrimitive?.content
        return DriveFileMeta(id = id, version = version, modifiedTime = modifiedTime)
    }
}
