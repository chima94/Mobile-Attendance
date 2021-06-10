package com.example.smartattendancesystem.ui.intro.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.example.smartattendancesystem.util.Screen
import com.example.smartattendancesystem.util.navigate

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            val systemUIController = activity?.let { SystemUIController(it.window) }
            val appThemeState = AppThemeState()
            setContent {
                BaseView(appThemeState = appThemeState, systemUIController = systemUIController) {
                    RegisterScreen{navigateBack ->
                        if(navigateBack){
                            navigate(Screen.REGISTER_TO_ONBOARD, Screen.REGISTER)
                        }
                    }
                }
            }
        }
    }
}

