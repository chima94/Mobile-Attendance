package com.example.smartattendancesystem.ui.intro.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                    RegisterScreen{navigate ->
                       when(navigate){
                           RegisterAction.NavigateBack ->{ navigate(Screen.REGISTER_TO_ONBOARD, Screen.REGISTER)}
                           RegisterAction.SignIn -> {
                               startActivity(Intent(requireActivity(), MainActivity::class.java))
                           }
                           is RegisterAction.DisplayError ->{
                               Toast.makeText(requireContext(), navigate.message, Toast.LENGTH_LONG).show()
                           }
                       }


                    }
                }
            }
        }
    }
}

