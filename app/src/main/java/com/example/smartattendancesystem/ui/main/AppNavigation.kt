package com.example.smartattendancesystem.ui.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.smartattendancesystem.ui.main.attendance.Attendance
import com.example.smartattendancesystem.ui.main.history.History
import com.example.smartattendancesystem.ui.main.profile.Profile
import com.example.smartattendancesystem.ui.main.update.UpdateScreen

internal sealed class Screen(val route : String){
    object Attendance : Screen("attendanceroot")
    object History : Screen("historyroot")
    object Profile : Screen("profileroot")
}

private sealed class LeafScreen(val route : String){
    object Attendance : LeafScreen("attendance")
    object History : LeafScreen("history")
    object Profile : LeafScreen("profile")

    object Update : LeafScreen("update")
}


@Composable
internal fun AppNavigation(
    navController : NavHostController
){
    NavHost(navController = navController, startDestination = Screen.Attendance.route ){
        addAttendanceTopLevel(navController)
        addHistoryTopLevel(navController)
        addProfileTopLevel(navController)
    }
}



private fun NavGraphBuilder.addAttendanceTopLevel(
    navController: NavController
){
    navigation(
        route = Screen.Attendance.route,
        startDestination = LeafScreen.Attendance.route
    ){
        addAttendance(navController)
        addUpdate(navController)
    }
}


private fun NavGraphBuilder.addHistoryTopLevel(navController: NavController){
    navigation(
        route = Screen.History.route,
        startDestination = LeafScreen.History.route
    ){
        addHistory(navController)
    }
}



private fun NavGraphBuilder.addProfileTopLevel(navController: NavController){
        navigation(
            route = Screen.Profile.route,
            startDestination = LeafScreen.Profile.route
        ){
            addProfile(navController)
        }
}




private fun NavGraphBuilder.addAttendance(navController: NavController){
    composable(LeafScreen.Attendance.route){
        Attendance(
            verify = {
                navController.navigate(LeafScreen.Update.route)
            }
        )
    }
}

private fun NavGraphBuilder.addHistory(navController: NavController){
    composable(LeafScreen.History.route){
        History()
    }
}

private fun NavGraphBuilder.addProfile(navController: NavController){
    composable(LeafScreen.Profile.route){
        Profile()
    }
}


private fun NavGraphBuilder.addUpdate(navController: NavController){
    composable(LeafScreen.Update.route){
        UpdateScreen(onNavigateBack = {
            navController.popBackStack()
        })
    }
}