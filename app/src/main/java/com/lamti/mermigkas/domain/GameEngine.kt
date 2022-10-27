package com.lamti.mermigkas.domain

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class GameEngine(private val size: Int) {

    private val _gridState: MutableSharedFlow<Grid> = MutableSharedFlow()
    val gridState: SharedFlow<Grid> = _gridState

    suspend fun start(steps: Int): Either<EvenNumberError, Grid> {
        var index = steps
        var updatedGrid: Grid

        return Grid.build(size).mapSuccess { grid ->
            updatedGrid = grid

            while (index != 0) {
                updatedGrid = move(updatedGrid)
                Log.d("TAGARA", "Engine Grid: ${updatedGrid.flatten().first { it.ant != null }}")

                delay(50)
                _gridState.emit(updatedGrid)
                index -= 1
            }
            return@mapSuccess updatedGrid
        }
    }

    private fun move(currentGrid: Grid): Grid {
        val currentAntPosition: Cell = currentGrid.findAntPosition()

        val newCell: Cell = if (currentAntPosition.color == CellColor.WHITE) {
            when (currentAntPosition.ant?.face) {
                Direction.North -> currentAntPosition.copy(
                    ant = Ant(face = Direction.East),
                    coordinates = currentAntPosition.coordinates.copy(x = currentAntPosition.coordinates.x + 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y,
                        x = currentAntPosition.coordinates.x + 1
                    )
                )
                Direction.East -> currentAntPosition.copy(
                    ant = Ant(face = Direction.South),
                    coordinates = currentAntPosition.coordinates.copy(y = currentAntPosition.coordinates.y - 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y - 1,
                        x = currentAntPosition.coordinates.x
                    )
                )
                Direction.West -> currentAntPosition.copy(
                    ant = Ant(face = Direction.North),
                    coordinates = currentAntPosition.coordinates.copy(y = currentAntPosition.coordinates.y + 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y + 1,
                        x = currentAntPosition.coordinates.x
                    )
                )
                Direction.South -> currentAntPosition.copy(
                    ant = Ant(face = Direction.West),
                    coordinates = currentAntPosition.coordinates.copy(x = currentAntPosition.coordinates.x - 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y,
                        x = currentAntPosition.coordinates.x - 1
                    )
                )
                else -> throw IndexOutOfBoundsException()
            }
        } else {
            when (currentAntPosition.ant?.face) {
                Direction.North -> currentAntPosition.copy(
                    ant = Ant(face = Direction.West),
                    coordinates = currentAntPosition.coordinates.copy(x = currentAntPosition.coordinates.x - 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y,
                        x = currentAntPosition.coordinates.x - 1
                    )
                )
                Direction.East -> currentAntPosition.copy(
                    ant = Ant(face = Direction.North),
                    coordinates = currentAntPosition.coordinates.copy(y = currentAntPosition.coordinates.y + 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y + 1,
                        x = currentAntPosition.coordinates.x
                    )
                )
                Direction.West -> currentAntPosition.copy(
                    ant = Ant(face = Direction.South),
                    coordinates = currentAntPosition.coordinates.copy(y = currentAntPosition.coordinates.y - 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y - 1,
                        x = currentAntPosition.coordinates.x
                    )
                )
                Direction.South -> currentAntPosition.copy(
                    ant = Ant(face = Direction.East),
                    coordinates = currentAntPosition.coordinates.copy(x = currentAntPosition.coordinates.x + 1),
                    color = currentGrid.findColorAtPosition(
                        y = currentAntPosition.coordinates.y,
                        x = currentAntPosition.coordinates.x + 1
                    )
                )
                else -> throw IndexOutOfBoundsException()
            }
        }

        val changeColor = if (currentAntPosition.color == CellColor.WHITE) CellColor.BLACK else CellColor.WHITE
        val newCurrentPosition = currentAntPosition.copy(color = changeColor, ant = null)
        Log.d("TAGARA", "Color: ${newCurrentPosition.color}")

        return currentGrid.moveAntAtPosition(newCurrentPosition, newCell)
    }
}
