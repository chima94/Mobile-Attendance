package com.example.smartattendancesystem.ui.intro.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.smartattendancesystem.ui.BaseView
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SystemUIController
import com.example.smartattendancesystem.util.Screen
import com.example.smartattendancesystem.util.navigate

class OnboardFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //subscribeOnNavigation()
        onBackButtonPressed()

        return ComposeView(requireContext()).apply {
            setContent {
                val systemUIController = activity?.let { SystemUIController(it.window) }
                val appThemeState = AppThemeState()
                BaseView(appThemeState = appThemeState, systemUIController = systemUIController) {
                    OnBoardScreen{navigation ->
                        when(navigation){
                            Navigation.LOGIN ->{
                               navigate(to = Screen.LOGIN, Screen.ONBOARD)
                            }
                            Navigation.REGISTER ->{
                                navigate(to = Screen.REGISTER, Screen.ONBOARD)
                            }
                        }
                    }
                }
            }
        }
    }

   /* private fun subscribeOnNavigation(){
        viewModel.navigate.observe(viewLifecycleOwner, Observer { screen ->
            when(screen){
                Screen.ONBOARD -> this.navigate(Screen.ONBOARD, Screen.SPLASH)
                Screen.HOME -> this.navigate(Screen.HOME, Screen.SPLASH)
                else -> this.navigate(Screen.SPLASH, Screen.SPLASH)
            }
        })
    }*/

    private fun onBackButtonPressed(){
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }
}