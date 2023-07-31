package com.matthias.breakout.common

import de.eskalon.commons.core.ManagedGame
import de.eskalon.commons.screen.ManagedScreen
import de.eskalon.commons.screen.transition.ScreenTransition

typealias BasicGame = ManagedGame<ManagedScreen, ScreenTransition>

inline fun <reified Screen : ManagedScreen> BasicGame.addScreen(screen: Screen) =
    screenManager.addScreen(Screen::class.simpleName, screen)

inline fun <reified TransitionType : ScreenTransition> BasicGame.addScreenTransition(transition: TransitionType) =
    screenManager.addScreenTransition(TransitionType::class.simpleName, transition)

inline fun <reified ScreenType : ManagedScreen, reified TransitionType : ScreenTransition> BasicGame.pushScreen() =
    screenManager.pushScreen(ScreenType::class.simpleName, TransitionType::class.simpleName)

inline fun <reified ScreenType : ManagedScreen> BasicGame.pushScreen(transitionName: String? = null) =
    screenManager.pushScreen(ScreenType::class.simpleName, transitionName)