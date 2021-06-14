package com.example.smartattendancesystem.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.smartattendancesystem.ui.main.attendance.Attendance
import com.example.smartattendancesystem.ui.main.history.History
import com.example.smartattendancesystem.ui.main.profile.Profile

internal sealed class Screen(val route : String){
    object Attendance : Screen("attendanceroot")
    object History : Screen("historyroot")
    object Profile : Screen("profileroot")
}

private sealed class LeafScreen(val route : String){
    object Attendance : LeafScreen("attendance")
    object History : LeafScreen("history")
    object Profile : LeafScreen("profile")
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
        Attendance()
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