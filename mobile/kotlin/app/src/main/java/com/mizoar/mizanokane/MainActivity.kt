package com.mizoar.mizanokane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.local.AppDatabase
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.data.repository.RoomMizanRepository
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.ui.components.AppHeader
import com.mizoar.mizanokane.ui.components.BottomNavigationBar
import com.mizoar.mizanokane.ui.navigation.Screen
import com.mizoar.mizanokane.ui.screens.BerandaScreen
import com.mizoar.mizanokane.ui.screens.EmptyScreen
import com.mizoar.mizanokane.ui.screens.KebijakanPrivasiScreen
import com.mizoar.mizanokane.ui.screens.KelolaBudgetScreen
import com.mizoar.mizanokane.ui.screens.KelolaDompetScreen
import com.mizoar.mizanokane.ui.screens.KelolaKategoriScreen
import com.mizoar.mizanokane.ui.screens.PengaturanScreen
import com.mizoar.mizanokane.ui.screens.RiwayatScreen
import com.mizoar.mizanokane.ui.screens.StatistikScreen
import com.mizoar.mizanokane.ui.screens.SubmenuBahayaScreen
import com.mizoar.mizanokane.ui.screens.SubmenuDataScreen
import com.mizoar.mizanokane.ui.screens.SubmenuTampilanScreen
import com.mizoar.mizanokane.ui.screens.SubmenuTentangScreen
import com.mizoar.mizanokane.ui.screens.SyaratKetentuanScreen
import com.mizoar.mizanokane.ui.screens.TimbanganScreen
import com.mizoar.mizanokane.ui.screens.TransactionFormScreen
import com.mizoar.mizanokane.ui.theme.MizanOkaneTheme
import com.mizoar.mizanokane.ui.theme.MizanTheme
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getInstance(applicationContext)
        val repository = RoomMizanRepository(db)
        setContent {
            val settings by repository.getSettingsFlow().collectAsState(initial = DefaultData.DEFAULT_SETTINGS)
            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = when (settings.theme) {
                "light" -> false
                "dark" -> true
                else -> isSystemDark
            }
            MizanOkaneTheme(darkTheme = darkTheme) {
                MizanApp(repository = repository)
            }
        }
    }
}

@Composable
fun MizanApp(
    repository: MizanRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Beranda.route

    val isFullScreen = currentRoute.startsWith(Screen.TransactionForm.route) ||
            currentRoute == Screen.KelolaDompet.route ||
            currentRoute == Screen.KelolaKategori.route ||
            currentRoute == Screen.KelolaBudget.route ||
            currentRoute == Screen.SubmenuTampilan.route ||
            currentRoute == Screen.SubmenuBahaya.route ||
            currentRoute == Screen.SubmenuData.route ||
            currentRoute == Screen.SubmenuTentang.route ||
            currentRoute == Screen.KebijakanPrivasi.route ||
            currentRoute == Screen.SyaratKetentuan.route

    val isDemo by repository.isDemoModeFlow().collectAsState(initial = false)
    var showBannerExitConfirmDialog by remember { mutableStateOf(false) }

    if (showBannerExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBannerExitConfirmDialog = false },
            containerColor = MizanTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = stringResource(id = R.string.dialog_exit_dummy_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBannerExitConfirmDialog = false
                        coroutineScope.launch {
                            repository.clearSampleData()
                            Toast.makeText(context, context.getString(R.string.msg_dummy_exit_success), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.accent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = stringResource(id = R.string.action_keluar))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBannerExitConfirmDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        color = MizanTheme.colors.textSecondary
                    )
                }
            }
        )
    }

    // State bulan & filter dompet yang dibagikan antara Beranda dan Riwayat
    val currentYearMonth = remember { getCurrentYearMonthString() }
    var berandaMonth by remember { mutableStateOf(currentYearMonth) }
    var riwayatMonthFilter by remember { mutableStateOf(currentYearMonth) }
    var selectedWalletFilter by remember { mutableStateOf("all") }

    BackHandler(enabled = currentRoute != Screen.Beranda.route) {
        if (isFullScreen) {
            navController.popBackStack()
        } else {
            navController.navigate(Screen.Beranda.route) {
                popUpTo(Screen.Beranda.route) {
                    inclusive = false
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val onTabSelected: (Screen) -> Unit = { screen ->
        if (screen.route != currentRoute) {
            navController.navigate(screen.route) {
                popUpTo(Screen.Beranda.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isFullScreen) {
                Column {
                    AppHeader()
                    if (isDemo) {
                        DemoModeBanner(onExitClick = { showBannerExitConfirmDialog = true })
                    }
                }
            }
        },
        bottomBar = {
            if (!isFullScreen) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onTabSelected = onTabSelected
                )
            }
        },
        containerColor = MizanTheme.colors.bg
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Beranda.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Beranda.route) {
                BerandaScreen(
                    repository = repository,
                    selectedMonth = berandaMonth,
                    onMonthChange = { newMonth ->
                        berandaMonth = newMonth
                        riwayatMonthFilter = newMonth
                    },
                    selectedWalletFilter = selectedWalletFilter,
                    onWalletFilterSelect = { walletId ->
                        selectedWalletFilter = if (selectedWalletFilter == walletId) "all" else walletId
                        navController.navigate(Screen.Riwayat.route) {
                            popUpTo(Screen.Beranda.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onAddTransactionClick = {
                        navController.navigate(Screen.TransactionForm.route)
                    },
                    onEditTransactionClick = { txId ->
                        navController.navigate("${Screen.TransactionForm.route}/$txId")
                    }
                )
            }
            composable(Screen.Riwayat.route) {
                RiwayatScreen(
                    repository = repository,
                    selectedMonth = riwayatMonthFilter,
                    onMonthChange = { newMonth ->
                        riwayatMonthFilter = newMonth
                        if (newMonth != "all") {
                            berandaMonth = newMonth
                        }
                    },
                    selectedWalletFilter = selectedWalletFilter,
                    onWalletFilterChange = { newWallet ->
                        selectedWalletFilter = newWallet
                    },
                    onAddTransactionClick = {
                        navController.navigate(Screen.TransactionForm.route)
                    },
                    onEditTransactionClick = { txId ->
                        navController.navigate("${Screen.TransactionForm.route}/$txId")
                    }
                )
            }
            composable(Screen.Timbangan.route) {
                TimbanganScreen(
                    repository = repository,
                    onNavigateToTransactionForm = {
                        navController.navigate(Screen.TransactionForm.route)
                    }
                )
            }
            composable(Screen.Statistik.route) {
                StatistikScreen(
                    repository = repository,
                    onNavigateToTransactionForm = {
                        navController.navigate(Screen.TransactionForm.route)
                    }
                )
            }
            composable(Screen.Pengaturan.route) {
                PengaturanScreen(
                    onTampilanClick = {
                        navController.navigate(Screen.SubmenuTampilan.route)
                    },
                    onKelolaKategoriClick = {
                        navController.navigate(Screen.KelolaKategori.route)
                    },
                    onKelolaDompetClick = {
                        navController.navigate(Screen.KelolaDompet.route)
                    },
                    onKelolaBudgetClick = {
                        navController.navigate(Screen.KelolaBudget.route)
                    },
                    onDataClick = {
                        navController.navigate(Screen.SubmenuData.route)
                    },
                    onTentangClick = {
                        navController.navigate(Screen.SubmenuTentang.route)
                    },
                    onZonaBahayaClick = {
                        navController.navigate(Screen.SubmenuBahaya.route)
                    }
                )
            }
            composable(Screen.SubmenuTampilan.route) {
                SubmenuTampilanScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SubmenuBahaya.route) {
                SubmenuBahayaScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SubmenuData.route) {
                SubmenuDataScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SubmenuTentang.route) {
                SubmenuTentangScreen(
                    onKebijakanPrivasiClick = {
                        navController.navigate(Screen.KebijakanPrivasi.route)
                    },
                    onSyaratKetentuanClick = {
                        navController.navigate(Screen.SyaratKetentuan.route)
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.KebijakanPrivasi.route) {
                KebijakanPrivasiScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SyaratKetentuan.route) {
                SyaratKetentuanScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.KelolaDompet.route) {
                KelolaDompetScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.KelolaKategori.route) {
                KelolaKategoriScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.KelolaBudget.route) {
                KelolaBudgetScreen(
                    repository = repository,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.TransactionForm.route) {
                TransactionFormScreen(
                    repository = repository,
                    txId = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "${Screen.TransactionForm.route}/{txId}",
                arguments = listOf(navArgument("txId") { type = NavType.StringType })
            ) { backStackEntry ->
                val txId = backStackEntry.arguments?.getString("txId")
                TransactionFormScreen(
                    repository = repository,
                    txId = txId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun DemoModeBanner(
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x1FF5A623))
            .border(1.dp, Color(0x52F5A623), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = null,
                tint = Color(0xFFF5A623),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = R.string.banner_demo_badge),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF5A623)
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = R.string.banner_demo_desc),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MizanTheme.colors.textSecondary
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                .clickable(onClick = onExitClick)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = stringResource(id = R.string.banner_demo_action_exit),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MizanTheme.colors.text
                )
            )
        }
    }
}
