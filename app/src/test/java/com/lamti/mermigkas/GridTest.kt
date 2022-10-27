package com.lamti.mermigkas

import com.lamti.mermigkas.domain.Ant
import com.lamti.mermigkas.domain.Cell
import com.lamti.mermigkas.domain.CellColor
import com.lamti.mermigkas.domain.Coordinates
import com.lamti.mermigkas.domain.Either
import com.lamti.mermigkas.domain.EvenNumberError
import com.lamti.mermigkas.domain.Grid
import org.junit.Test

import org.junit.Assert.*

class GridTest {

    @Test
    fun `grid should be odd sized`() {
        assertEquals(Either.Error(EvenNumberError), Grid.build(12))
    }

    @Test
    fun `initial grid should have all squares white except the center which is ant`() {
        val n = 11

        Grid.build(n).fold { grid ->
            assertEquals(
                Cell(color = CellColor.WHITE, ant = Ant(), coordinates = Coordinates(x = 5, y = 5)),
                grid.findAntPosition()
            )
            assertEquals(true, grid.flatten().all { it.color == CellColor.WHITE })
        }
    }
}

