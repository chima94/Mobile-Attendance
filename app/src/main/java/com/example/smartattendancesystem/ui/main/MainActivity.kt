package com.example.smartattendancesystem.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUIController =  SystemUIController(window)
            val appThemeState = AppThemeState()
            ProvideWindowInsets(consumeWindowInsets = false) {
                BaseView(appThemeState = appThemeState, systemUIController = systemUIController) {
                    Home()
                }
            }

        }
    }

}


