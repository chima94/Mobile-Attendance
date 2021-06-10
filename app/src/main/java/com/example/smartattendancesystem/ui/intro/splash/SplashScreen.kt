package com.example.smartattendancesystem.ui.intro.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.smartattendancesystem.ui.theme.green700

@Composable
fun SplashScreen(){

    Scaffold {

        Column(
            modifier = Modifier.fillMaxSize().background(green700),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Smart Attendance", color = Color.White, style = MaterialTheme.typography.h6)
        }
    }
}