package com.pph.nqueens.ui.navigation.flow

import androidx.navigation.NavHostController
import com.pph.game.GameScreenComposable
import com.pph.setboard.SetBoardScreenComposable
import com.pph.uinavigation.flow.NavigationFlow
import com.pph.uinavigation.ScreenComposable
import com.pph.uinavigation.navigateWith

class MainNavigationFlow(
    private val navController: NavHostController
) : NavigationFlow {

    override fun start() {
        navController.navigateWith<SetBoardScreenComposable>(clearStack = true)
    }

    override fun screenComposables(): List<ScreenComposable> = listOf(
        SetBoardScreenComposable(onNavigateNext = { navController.navigateWith<GameScreenComposable>() }),
        GameScreenComposable(onBack = { navController.popBackStack() })
    )
}