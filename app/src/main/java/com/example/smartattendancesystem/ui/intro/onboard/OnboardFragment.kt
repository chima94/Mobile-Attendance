package com.example.smartattendancesystem.ui.intro.onboard

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        onBackButtonPressed()

        if(!allPermissionsGranted()){
            runtimePermissions
        }

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



    private fun onBackButtonPressed(){
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }


    private val requiredPermission : Array<String?>
        get() = try{
            val info = requireContext().packageManager
                .getPackageInfo(requireContext().packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if(ps != null && ps.isNotEmpty()){
                ps
            }else{
                arrayOfNulls(0)
            }
        }catch (e : Exception){
            arrayOfNulls(0)
        }

    private fun allPermissionsGranted() : Boolean{
        for(permission in requiredPermission){
            if(!isPermissionGranted(requireContext(), permission)){
                return false
            }
        }
        return true
    }

    private fun isPermissionGranted(
        context : Context,
        permission : String?
    ) : Boolean{
        if(ContextCompat.checkSelfPermission(context, permission!!) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }


    private val runtimePermissions : Unit
        get(){
            val allNeededPermissions : MutableList<String?> = ArrayList()
            for(permission in requiredPermission){
                if(!isPermissionGranted(requireContext(), permission)){
                    allNeededPermissions.add(permission)
                }
            }

            if(allNeededPermissions.isNotEmpty()){
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    allNeededPermissions.toTypedArray(),
                    PERMISSION_REQUESTS
                )
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object{
        private const val PERMISSION_REQUESTS = 1
    }
}




