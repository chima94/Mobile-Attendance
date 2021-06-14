package com.example.smartattendancesystem.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.smartattendancesystem.R
import com.example.smartattendancesystem.ui.theme.AppBarAlphas
import com.google.accompanist.insets.navigationBarsPadding


@Composable
internal fun Home(){

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            HomeBottomNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route){
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ){
        Box(Modifier.fillMaxSize()){
            AppNavigation(navController = navController)
        }
    }
}



@Composable
private fun NavController.currentScreenAsState() : State<Screen>{
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Attendance)}

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Attendance.route } -> {
                    selectedItem.value = Screen.Attendance
                }
                destination.hierarchy.any { it.route == Screen.History.route } -> {
                    selectedItem.value = Screen.History
                }
                destination.hierarchy.any { it.route == Screen.Profile.route } -> {
                    selectedItem.value = Screen.Profile
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}





@Composable
internal fun HomeBottomNavigation(
    selectedNavigation : Screen,
    onNavigationSelected : (Screen) -> Unit,
    modifier : Modifier
){
    Surface(
        color = MaterialTheme.colors.surface.copy(alpha = AppBarAlphas.translucentBarAlpha()),
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        elevation = 8.dp,
        modifier = modifier
    ){
        Row(
            Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            HomeBottomNavigationItem(
                label = stringResource(R.string.attendance),
                selected = selectedNavigation == Screen.Attendance,
                onClick = {onNavigationSelected(Screen.Attendance)},
                contentDescription = stringResource(id = R.string.attendance),
                painter = rememberVectorPainter(image = Icons.Default.Create)
            )

            HomeBottomNavigationItem(
                label = stringResource(R.string.history),
                selected = selectedNavigation == Screen.History,
                onClick = {onNavigationSelected(Screen.History)},
                contentDescription = stringResource(id = R.string.history),
                painter = rememberVectorPainter(image = Icons.Default.History),
                selectedPainter = rememberVectorPainter(image = Icons.Filled.HistoryEdu)
            )

            HomeBottomNavigationItem(
                label = stringResource(R.string.profile),
                selected = selectedNavigation == Screen.Profile,
                onClick = {onNavigationSelected(Screen.Profile)},
                contentDescription = stringResource(id = R.string.profile),
                painter = rememberVectorPainter(image = Icons.Default.AccountBox),
                selectedPainter = rememberVectorPainter(image = Icons.Default.AccountCircle)
            )

        }
    }
}





@Composable
private fun RowScope.HomeBottomNavigationItem(
    selected : Boolean,
    selectedPainter : Painter? = null,
    painter : Painter,
    contentDescription : String,
    label : String,
    onClick : () -> Unit
) {
    BottomNavigationItem(
        icon = {
            if (selectedPainter != null) {
                Crossfade(targetState = selected) { selected ->
                    Icon(
                        painter = if (selected) selectedPainter else painter,
                        contentDescription = contentDescription
                    )
                }
            } else {
                Icon(
                    painter = painter,
                    contentDescription = contentDescription
                )
            }
        },
        label = { Text(label) },
        selected = selected,
        onClick = onClick
    )
}