package com.example.smartattendancesystem.ui.main.facerecognition

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.util.BackHandler


@Composable
fun FacialRecognitionScreen(
    vieModel : FacialRecognitionViewModel = hiltViewModel(),
    onBack : () -> Unit
){

    BackHandler(onBack = onBack)
}