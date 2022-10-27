package com.lamti.mermigkas

import com.lamti.mermigkas.domain.Ant
import com.lamti.mermigkas.domain.Cell
import com.lamti.mermigkas.domain.CellColor
import com.lamti.mermigkas.domain.Coordinates
import com.lamti.mermigkas.domain.Direction
import com.lamti.mermigkas.domain.EvenNumberError
import com.lamti.mermigkas.domain.GameEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test


class GameEngineTest {

    @Test
    fun `first step`() {
        val expectedGrid = stepOneGrid()
        val gameEngine = GameEngine(11)

        gameEngine.start(1).fold(logFail) {
            assertEquals(expectedGrid, it)
        }
    }

    @Test
    fun `two steps`() {
        val expectedGrid = stepTwoGrid()
        val gameEngine = GameEngine(11)

        gameEngine.start(2).fold(logFail) {
            assertEquals(expectedGrid, it)
        }
    }

    private val logFail: (EvenNumberError) -> Unit = {
        fail(it.toString())
    }

    private fun stepOneGrid(n: Int = 11, center: Int = n.div(2)) =
        buildList(n) {
            repeat(n) { y ->
                add(
                    buildList {
                        repeat(n) { x ->
                            when {
                                x == center && y == center -> add(
                                    Cell(
                                        color = CellColor.BLACK,
                                        ant = null,
                                        Coordinates(x, y)
                                    )
                                )
                                x == center && y == center - 1 -> add(
                                    Cell(
                                        color = CellColor.WHITE,
                                        ant = Ant(Direction.North),
                                        Coordinates(x, y)
                                    )
                                )
                                else -> add(Cell(color = CellColor.WHITE, ant = null, Coordinates(x, y)))
                            }
                        }
                    }
                )
            }
        }

    private fun stepTwoGrid(n: Int = 11, center: Int = n.div(2)) =
        buildList(n) {
            repeat(n) { y ->
                add(
                    buildList {
                        repeat(n) { x ->
                            when {
                                x == center && y == center -> add(
                                    Cell(
                                        color = CellColor.BLACK,
                                        ant = null,
                                        Coordinates(x, y)
                                    )
                                )
                                x == center && y == center - 1 -> add(
                                    Cell(
                                        color = CellColor.BLACK,
                                        ant = null,
                                        Coordinates(x, y)
                                    )
                                )
                                x == center + 1 && y == center - 1 -> add(
                                    Cell(
                                        color = CellColor.WHITE,
                                        ant = Ant(Direction.West),
                                        Coordinates(x, y)
                                    )
                                )
                                else -> add(Cell(color = CellColor.WHITE, ant = null, Coordinates(x, y)))
                            }
                        }
                    }
                )
            }
        }
}
