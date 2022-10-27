package com.lamti.mermigkas

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamti.mermigkas.domain.GameEngine
import com.lamti.mermigkas.domain.Grid
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val gameEngine: GameEngine = GameEngine(GRID_SIZE),
    private val savedState: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    val board: SharedFlow<Grid> = gameEngine.gridState

    init {
        viewModelScope.launch {
            gameEngine.start(700)
        }
    }

}
