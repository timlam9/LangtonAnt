package com.lamti.mermigkas.domain

data class Cell(
    val color: CellColor,
    val ant: Ant?,
    val coordinates: Coordinates,
)

