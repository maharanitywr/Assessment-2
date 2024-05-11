package org.d3if3086.mobpro1.navigation

import org.d3if3086.mobpro1.ui.screen.KEY_ID_BAKSO

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_BAKSO}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
    data object About: Screen("aboutScreen")
}