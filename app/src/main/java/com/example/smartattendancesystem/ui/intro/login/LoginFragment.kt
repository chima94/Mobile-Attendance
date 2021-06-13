package com.example.smartattendancesystem.ui.intro.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.main.MainActivity
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.example.smartattendancesystem.util.Screen
import com.example.smartattendancesystem.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

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
                    LoginScreen {action ->
                        when(action){
                            LoginAction.NavigateBack ->{navigate(Screen.LOGIN_TO_ONBOARD, Screen.LOGIN)}
                            LoginAction.SignIn ->{
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                activity?.finish()
                            }
                            is LoginAction.DisplayError ->{
                                Toast.makeText(requireContext(), action.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}