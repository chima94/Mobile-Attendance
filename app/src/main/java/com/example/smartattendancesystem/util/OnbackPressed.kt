package com.example.smartattendancesystem.util

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner


@Composable
fun BackHandler(
    enable : Boolean = true,
    onBack : () -> Unit
) {
    val currentOnBack by rememberUpdatedState(onBack)

    val backCallback = remember{
        object : OnBackPressedCallback(enable){
            override fun handleOnBackPressed() {
                currentOnBack()
            }

        }
    }
    SideEffect {
        backCallback.isEnabled = enable
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current){
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}