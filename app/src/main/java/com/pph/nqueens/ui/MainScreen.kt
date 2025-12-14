package com.pph.nqueens.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pph.nqueens.ui.navigation.flow.MainNavigationFlow
import com.pph.setboard.SetBoardScreenComposable
import com.pph.uinavigation.CoordinatorNavHostScreen
import com.pph.uinavigation.NavAnimation

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
) {
    val mainNavigationFlow = MainNavigationFlow(navController)
    val screenComposableList = mainNavigationFlow.screenComposables()

    Scaffold { paddingValues ->
        CoordinatorNavHostScreen(
            modifier = Modifier.padding(paddingValues),
            navHostController = navController,
            startDestination = SetBoardScreenComposable::class,
            screenComposableList = screenComposableList,
            navAnimation = NavAnimation.FADE
        )
    }
}