package com.mizoar.mizanokane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.mizoar.mizanokane.data.local.AppDatabase
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.data.repository.RoomMizanRepository
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.ui.components.AppHeader
import com.mizoar.mizanokane.ui.components.BottomNavigationBar
import com.mizoar.mizanokane.ui.navigation.Screen
import com.mizoar.mizanokane.ui.screens.BerandaScreen
import com.mizoar.mizanokane.ui.screens.EmptyScreen
import com.mizoar.mizanokane.ui.screens.KelolaBudgetScreen
import com.mizoar.mizanokane.ui.screens.KelolaDompetScreen
import com.mizoar.mizanokane.ui.screens.KelolaKategoriScreen
import com.mizoar.mizanokane.ui.screens.PengaturanScreen
import com.mizoar.mizanokane.ui.screens.RiwayatScreen
import com.mizoar.mizanokane.ui.screens.TransactionFormScreen
import com.mizoar.mizanokane.ui.theme.MizanOkaneTheme
import com.mizoar.mizanokane.ui.theme.MizanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getInstance(applicationContext)
        val repository = RoomMizanRepository(db)
        setContent {
            MizanOkaneTheme {
                MizanApp(repository = repository)
            }
        }
    }
}

@Composable
fun MizanApp(
    repository: MizanRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Beranda.route

    val isFullScreen = currentRoute.startsWith(Screen.TransactionForm.route) ||
            currentRoute == Screen.KelolaDompet.route ||
            currentRoute == Screen.KelolaKategori.route ||
            currentRoute == Screen.KelolaBudget.route

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
                AppHeader()
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
                EmptyScreen(title = stringResource(id = R.string.nav_timbangan))
            }
            composable(Screen.Statistik.route) {
                EmptyScreen(title = stringResource(id = R.string.nav_statistik))
            }
            composable(Screen.Pengaturan.route) {
                PengaturanScreen(
                    onKelolaDompetClick = {
                        navController.navigate(Screen.KelolaDompet.route)
                    },
                    onKelolaKategoriClick = {
                        navController.navigate(Screen.KelolaKategori.route)
                    },
                    onKelolaBudgetClick = {
                        navController.navigate(Screen.KelolaBudget.route)
                    }
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
