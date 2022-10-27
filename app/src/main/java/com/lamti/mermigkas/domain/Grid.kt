package com.lamti.mermigkas.domain

import android.util.Log

class Grid private constructor(private val value: MutableList<MutableList<Cell>>) : List<List<Cell>> by value {

    companion object {

        suspend fun build(n: Int): Either<EvenNumberError, Grid> {
            return OddNumber.of(n).mapSuccess { generate(n) }
        }

        private fun generate(n: Int): Grid {
            val centerX: Int = n.div(2)
            val centerY: Int = n.div(2)

            return Grid(
                buildList(n) {
                    repeat(n) { y ->
                        add(
                            buildList {
                                repeat(n) { x ->
                                    add(
                                        Cell(
                                            color = CellColor.WHITE,
                                            ant = if (x == centerX && y == centerY) Ant() else null,
                                            coordinates = Coordinates(x = x, y = y)
                                        )
                                    )
                                }
                            }.toMutableList()
                        )
                    }
                }.toMutableList()
            )
        }
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Grid) false
        else value == other
    }

    fun findAntPosition(): Cell = value.flatten().first { it.ant != null }

    fun findColorAtPosition(x: Int, y: Int): CellColor = value.flatten().first {
        it.coordinates.x == x && it.coordinates.y == y
    }.color

    fun moveAntAtPosition(currentCell: Cell, antCell: Cell): Grid {
        value[currentCell.coordinates] = currentCell
        value[antCell.coordinates] = antCell
        Log.d("TAGARA", "Board: ${value.flatten().map { it.color }}")

        return this
    }
}

private operator fun MutableList<MutableList<Cell>>.set(currentAntPosition: Coordinates, cell: Cell) {
    this[currentAntPosition.y][currentAntPosition.x] = cell
}

private operator fun MutableList<MutableList<Cell>>.get(currentAntPosition: Coordinates): Cell {
    return this[currentAntPosition.y][currentAntPosition.x]
}

