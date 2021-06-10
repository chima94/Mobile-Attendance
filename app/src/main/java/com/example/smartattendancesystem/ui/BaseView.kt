package com.example.smartattendancesystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartattendancesystem.ui.intro.TextFieldState
import com.example.smartattendancesystem.ui.theme.AppThemeState
import com.example.smartattendancesystem.ui.theme.SmartAttendanceSystemTheme
import com.example.smartattendancesystem.ui.theme.SystemUIController
import timber.log.Timber



@Composable
fun BaseView(
    appThemeState : AppThemeState,
    systemUIController: SystemUIController?,
    content : @Composable () -> Unit
){
    val color = appThemeState.color
    systemUIController?.setStatusBarColor(color = color, darkIcons = appThemeState.darkTheme)
    SmartAttendanceSystemTheme(darkTheme = appThemeState.darkTheme) {
        content()
    }
}


