package com.example.smartattendancesystem.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartattendancesystem.ui.intro.login.LoginFragmentDirections
import com.example.smartattendancesystem.ui.intro.onboard.OnboardFragmentDirections
import com.example.smartattendancesystem.ui.intro.register.RegisterFragmentDirections
import com.example.smartattendancesystem.ui.intro.splash.SplashFragmentDirections
import java.security.InvalidParameterException

enum class Screen{
    SPLASH, ONBOARD, LOGIN, REGISTER, REGISTER_TO_ONBOARD, LOGIN_TO_ONBOARD, MAIN
}

fun Fragment.navigate(to : Screen, from : Screen){

    if(to == from){
        throw InvalidParameterException("Cannot navigate to $to")
    }

    when(to){
        Screen.ONBOARD ->{
            val action = SplashFragmentDirections.actionSplashFragmentToOnboardFragment()
            findNavController().navigate(action)
        }

        Screen.LOGIN ->{
            val action = OnboardFragmentDirections.actionOnboardFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        Screen.REGISTER ->{
            val action = OnboardFragmentDirections.actionOnboardFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        Screen.REGISTER_TO_ONBOARD ->{
            val action = RegisterFragmentDirections.actionRegisterFragmentToOnboardFragment()
            findNavController().navigate(action)
        }

        Screen.LOGIN_TO_ONBOARD ->{
            val action = LoginFragmentDirections.actionLoginFragmentToOnboardFragment()
            findNavController().navigate(action)
        }
        else -> throw InvalidParameterException("Cannot navigate to $to")
    }
}