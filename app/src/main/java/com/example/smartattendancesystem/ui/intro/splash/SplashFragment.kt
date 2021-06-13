package com.example.smartattendancesystem.ui.intro.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.main.MainActivity
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.example.smartattendancesystem.util.Screen
import com.example.smartattendancesystem.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel : SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        subscribeOnNavigation()

        return ComposeView(requireContext()).apply {
            setContent {
                val systemUiController = activity?.let { SystemUIController(window = it.window) }
                val appThemeState = AppThemeState()
                BaseView(appThemeState = appThemeState, systemUIController = systemUiController) {
                    SplashScreen()
                }
            }
        }
    }

    private fun subscribeOnNavigation(){
        viewModel.navigate.observe(viewLifecycleOwner) { navigateTo ->
            navigateTo.getContentIfNotHandled()?.let { screen ->
                when (screen) {
                    Screen.ONBOARD -> this.navigate(Screen.ONBOARD, Screen.SPLASH)
                    Screen.MAIN ->{
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    }
                    else -> this.navigate(Screen.SPLASH, Screen.SPLASH)
                }
            }
        }
    }

}