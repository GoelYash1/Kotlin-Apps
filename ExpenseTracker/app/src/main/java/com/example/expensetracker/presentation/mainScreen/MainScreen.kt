package com.example.expensetracker.presentation.mainScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Home
import com.example.expensetracker.MainBottomNavigation
import com.example.expensetracker.MainNavigation
import com.example.expensetracker.MainTopBar
import com.example.expensetracker.Transactions
import com.example.expensetracker.presentation.mainScreen.homeScreen.HomeScreen
import com.example.expensetracker.presentation.mainScreen.transactions.TransactionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val homeNavController  = rememberNavController()
    Scaffold(
        topBar = {
            MainTopBar()
        },
        bottomBar = {
            MainBottomNavigation(navController = homeNavController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeNavigation(homeNavController)
        }
    }
}

@Composable
fun HomeNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Home.route){
        composable(Home.route){
            HomeScreen()
        }
        composable(Transactions.route){
            TransactionScreen()
        }
    }
}
