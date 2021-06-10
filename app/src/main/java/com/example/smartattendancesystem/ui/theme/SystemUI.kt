package com.example.smartattendancesystem.ui.theme

import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

class SystemUIController(private val window : Window) {

   fun setStatusBarColor(
       color : Color,
        darkIcons : Boolean = color.luminance() > 0.5f,
       transformColorForLightContent : (Color) -> Color = BlackScrimmed
       ) {
        val statusBarColor = when{
            darkIcons && Build.VERSION.SDK_INT < 23 -> transformColorForLightContent(color)
            else -> color
        }
       window.statusBarColor = statusBarColor.toArgb()
       if(Build.VERSION.SDK_INT >= 23){
           @Suppress("DEPRECATION")
           if(darkIcons){
               window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                      View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
           }else{
               window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
           }
       }
   }

    fun setNavigationBarColor(
        color: Color,
        darkIcons: Boolean = color.luminance() > 0.5f,
        transformColorForLightContent: (Color) -> Color = BlackScrimmed
    ) {
        val navBarColor = when {
            Build.VERSION.SDK_INT >= 29 -> Color.Transparent // For gesture nav
            darkIcons && Build.VERSION.SDK_INT < 26 -> transformColorForLightContent(color)
            else -> color
        }
        window.navigationBarColor = navBarColor.toArgb()
        if (Build.VERSION.SDK_INT >= 26) {
            @Suppress("DEPRECATION")
            if (darkIcons) {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }
    }

    fun setSystemBarsColor(
        color: Color,
        darkIcons: Boolean = color.luminance() > 0.5f,
        transformColorForLightContent: (Color) -> Color = BlackScrimmed
    ) {
        setStatusBarColor(color, darkIcons, transformColorForLightContent)
        setNavigationBarColor(color, darkIcons, transformColorForLightContent)
    }


}

private val BlackScrim = Color(0f, 0f, 0f, 0.2f) // 20% opaque black
private val BlackScrimmed: (Color) -> Color = { original ->
    BlackScrim.compositeOver(original)
}