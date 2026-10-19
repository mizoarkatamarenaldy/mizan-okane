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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mizoar.mizanokane.ui.components.AppHeader
import com.mizoar.mizanokane.ui.components.BottomNavigationBar
import com.mizoar.mizanokane.ui.navigation.Screen
import com.mizoar.mizanokane.ui.screens.EmptyScreen
import com.mizoar.mizanokane.ui.theme.MizanOkaneTheme
import com.mizoar.mizanokane.ui.theme.MizanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MizanOkaneTheme {
                MizanApp()
            }
        }
    }
}

@Composable
fun MizanApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Beranda.route

    BackHandler(enabled = currentRoute != Screen.Beranda.route) {
        navController.navigate(Screen.Beranda.route) {
            popUpTo(Screen.Beranda.route) {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
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
            AppHeader()
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
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
                EmptyScreen(title = stringResource(id = R.string.nav_beranda))
            }
            composable(Screen.Riwayat.route) {
                EmptyScreen(title = stringResource(id = R.string.nav_riwayat))
            }
            composable(Screen.Timbangan.route) {
                EmptyScreen(title = stringResource(id = R.string.nav_timbangan))
            }
            composable(Screen.Statistik.route) {
                EmptyScreen(title = stringResource(id = R.string.nav_statistik))
            }
            composable(Screen.Pengaturan.route) {
                EmptyScreen(title = stringResource(id = R.string.nav_pengaturan))
            }
        }
    }
}