package com.matthias.breakout.common

import de.eskalon.commons.screen.ManagedScreen
import de.eskalon.commons.screen.ScreenManager
import de.eskalon.commons.screen.transition.ScreenTransition

typealias BasicScreenManager = ScreenManager<ManagedScreen, ScreenTransition>

inline fun <reified Screen : ManagedScreen> BasicScreenManager.addScreen(screen: Screen) =
    addScreen(Screen::class.simpleName, screen)

inline fun <reified TransitionType : ScreenTransition> BasicScreenManager.addScreenTransition(transition: TransitionType) =
    addScreenTransition(TransitionType::class.simpleName, transition)

inline fun <reified ScreenType : ManagedScreen, reified TransitionType : ScreenTransition> BasicScreenManager.pushScreen() =
    pushScreen(ScreenType::class.simpleName, TransitionType::class.simpleName)

inline fun <reified ScreenType : ManagedScreen> BasicScreenManager.pushScreen(transitionName: String? = null) =
    pushScreen(ScreenType::class.simpleName, transitionName)