package com.example.smartattendancesystem.ui.main

import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.example.smartattendancesystem.util.rememberFlowWithLifecycle
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@AndroidEntryPoint
class MainActivity :ComponentActivity() {
    private lateinit var viewModel : MainViewModel
    private lateinit var fuseLocationProviderClient : FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            viewModel = hiltViewModel()
            viewModel.locationState(isLocationEnabled())
            val systemUIController =  SystemUIController(window)
            val appThemeState = AppThemeState()
            ProvideWindowInsets(consumeWindowInsets = false) {
                BaseView(appThemeState = appThemeState, systemUIController = systemUIController) {
                    Home()
                }
            }

        }
    }




    private fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


}



