package com.mizoar.mizanokane.ui.screens

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.auth.AuthPreferences
import com.mizoar.mizanokane.data.auth.GoogleAuthConfig
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

/**
 * Layar Submenu Akun dan Sinkron aplikasi Mizan Okane.
 * Sesuai spek Kotlin 8A:
 * - Menampilkan status akun Google Drive
 * - Belum login: tombol "Login Google" dan status "belum login"
 * - Sudah login: menampilkan email akun dan tombol "Logout", status "belum pernah sinkron"
 * - Credential Manager (androidx.credentials) + googleid dengan OAuth Client ID Web
 * - Otorisasi scope drive.appdata lewat Identity.getAuthorizationClient
 * - Status akun disimpan di DataStore Preferences (terpisah dari Room)
 * - Keluar: hapus status akun lokal dan panggil clearCredentialState (data lokal aman)
 */
@Composable
fun SubmenuAkunScreen(
    authPreferences: AuthPreferences,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val activity = remember(context) { findActivity(context) }

    val accountEmail by authPreferences.accountEmailFlow.collectAsState(initial = null)

    var isLoading by remember { mutableStateOf(false) }
    var pendingEmail by remember { mutableStateOf<String?>(null) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    val authResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        isLoading = false
        if (activityResult.resultCode == Activity.RESULT_OK) {
            coroutineScope.launch {
                val email = pendingEmail
                if (email != null) {
                    authPreferences.setAccountEmail(email)
                    Toast.makeText(context, context.getString(R.string.msg_login_google_success), Toast.LENGTH_SHORT).show()
                }
                pendingEmail = null
            }
        } else {
            pendingEmail = null
            Toast.makeText(context, context.getString(R.string.msg_auth_cancelled), Toast.LENGTH_SHORT).show()
        }
    }

    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            containerColor = MizanTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_logout_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        coroutineScope.launch {
                            try {
                                val credentialManager = CredentialManager.create(context)
                                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                            } catch (_: Exception) {
                            }
                            authPreferences.clearAccount()
                            Toast.makeText(context, context.getString(R.string.msg_logout_success), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.accent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = stringResource(id = R.string.action_logout_google))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        color = MizanTheme.colors.textSecondary
                    )
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.colors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // === HEADER BAR ATAS ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MizanTheme.colors.surface)
                .border(width = 0.5.dp, color = MizanTheme.colors.border)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.desc_kembali_ke_pengaturan),
                    tint = MizanTheme.colors.text
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = R.string.title_akun_sinkron),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN SUBMENU AKUN (SCROLLABLE) ===
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_akun_sinkronisasi),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.desc_akun_sinkronisasi),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MizanTheme.colors.textSecondary,
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                val currentEmail = accountEmail
                if (currentEmail.isNullOrBlank()) {
                    // Keadaan belum login
                    Text(
                        text = stringResource(id = R.string.sync_status_signed_out),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (isLoading) return@Button
                            val act = activity ?: run {
                                handleAuthError(
                                    context = context,
                                    stage = "kredensial",
                                    exception = IllegalStateException("Activity tidak ditemukan"),
                                    formatResId = R.string.msg_login_failed_credential
                                )
                                return@Button
                            }
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val credentialManager = CredentialManager.create(act)
                                    val signInOption = GetSignInWithGoogleOption.Builder(
                                        serverClientId = GoogleAuthConfig.WEB_CLIENT_ID
                                    ).build()

                                    val credentialRequest = GetCredentialRequest.Builder()
                                        .addCredentialOption(signInOption)
                                        .build()

                                    val result = credentialManager.getCredential(
                                        request = credentialRequest,
                                        context = act
                                    )

                                    val credential = result.credential
                                    if (credential is CustomCredential &&
                                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                    ) {
                                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                        val email = googleIdTokenCredential.id

                                        // Minta otorisasi scope drive.appdata lewat Identity.getAuthorizationClient
                                        val requestedScopes = listOf(Scope(GoogleAuthConfig.DRIVE_APPDATA_SCOPE))
                                        val authRequest = AuthorizationRequest.builder()
                                            .setRequestedScopes(requestedScopes)
                                            .setAccount(Account(email, "com.google"))
                                            .build()

                                        Identity.getAuthorizationClient(act)
                                            .authorize(authRequest)
                                            .addOnSuccessListener { authResult ->
                                                if (authResult.hasResolution()) {
                                                    val pendingIntent = authResult.pendingIntent
                                                    if (pendingIntent != null) {
                                                        pendingEmail = email
                                                        val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                                                        authResultLauncher.launch(intentSenderRequest)
                                                    } else {
                                                        isLoading = false
                                                        handleAuthError(
                                                            context = context,
                                                            stage = "otorisasi",
                                                            exception = IllegalStateException("PendingIntent resolusi otorisasi null"),
                                                            formatResId = R.string.msg_login_failed_authorization
                                                        )
                                                    }
                                                } else {
                                                    // Izin sudah pernah diberikan sebelumnya
                                                    isLoading = false
                                                    coroutineScope.launch {
                                                        authPreferences.setAccountEmail(email)
                                                        Toast.makeText(context, context.getString(R.string.msg_login_google_success), Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                handleAuthError(
                                                    context = context,
                                                    stage = "otorisasi",
                                                    exception = e,
                                                    formatResId = R.string.msg_login_failed_authorization
                                                )
                                            }
                                    } else {
                                        isLoading = false
                                        val ex = IllegalArgumentException("Tipe kredensial tidak sesuai: ${credential.type}")
                                        handleAuthError(
                                            context = context,
                                            stage = "identitas",
                                            exception = ex,
                                            formatResId = R.string.msg_login_failed_identity
                                        )
                                    }
                                } catch (_: GetCredentialCancellationException) {
                                    isLoading = false
                                    Toast.makeText(context, context.getString(R.string.msg_login_cancelled), Toast.LENGTH_SHORT).show()
                                } catch (e: GoogleIdTokenParsingException) {
                                    isLoading = false
                                    handleAuthError(
                                        context = context,
                                        stage = "identitas",
                                        exception = e,
                                        formatResId = R.string.msg_login_failed_identity
                                    )
                                } catch (e: GetCredentialException) {
                                    isLoading = false
                                    handleAuthError(
                                        context = context,
                                        stage = "kredensial",
                                        exception = e,
                                        formatResId = R.string.msg_login_failed_credential
                                    )
                                } catch (e: Exception) {
                                    isLoading = false
                                    handleAuthError(
                                        context = context,
                                        stage = "login",
                                        exception = e,
                                        formatResId = R.string.msg_login_failed_credential
                                    )
                                }
                            }
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MizanTheme.colors.accent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_login_google),
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // Keadaan sudah login
                    Text(
                        text = currentEmail,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(id = R.string.sync_status_never_synced),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showLogoutConfirmDialog = true },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MizanTheme.colors.surface,
                            contentColor = MizanTheme.colors.text
                        ),
                        border = BorderStroke(1.dp, MizanTheme.colors.border),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_logout_google),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun findActivity(context: Context): Activity? {
    var ctx = context
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

private const val TAG_AUTH = "MizanAuth"

/**
 * Menangani error login autentikasi:
 * - Menentukan kelas exception dan kode status (jika ApiException)
 * - Menulis ke Logcat dengan tag "MizanAuth" (berisi tahap, kelas exception, dan pesan)
 * - Memformat pesan dari strings.xml dan memotong maksimal 200 karakter
 * - Menampilkan Toast
 */
private fun handleAuthError(
    context: Context,
    stage: String,
    exception: Throwable,
    formatResId: Int
) {
    val apiEx = (exception as? ApiException) ?: (exception.cause as? ApiException)
    val className = if (apiEx != null && exception !is ApiException) {
        "${exception.javaClass.simpleName} (status ${apiEx.statusCode})"
    } else if (exception is ApiException) {
        "${exception.javaClass.simpleName} (status ${exception.statusCode})"
    } else {
        exception.javaClass.simpleName.ifEmpty { exception.javaClass.name }
    }

    val rawMsg = exception.message?.trim()?.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }
        ?: exception.cause?.message?.trim()?.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }
    val msg = rawMsg ?: "-"

    // Tulis ke Logcat tag MizanAuth
    Log.e(TAG_AUTH, "Tahap $stage: $className - $msg", exception)

    // Format pesan dan potong maksimal 200 karakter
    val formatted = context.getString(formatResId, className, msg)
    val displayMessage = if (formatted.length > 200) {
        formatted.take(197) + "..."
    } else {
        formatted
    }

    Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show()
}
