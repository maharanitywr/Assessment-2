package org.d3if3086.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3086.mobpro1.database.BaksoDao
import org.d3if3086.mobpro1.model.Bakso

class MainViewModel(dao: BaksoDao) : ViewModel() {

    val data: StateFlow<List<Bakso>> = dao.getBakso().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}