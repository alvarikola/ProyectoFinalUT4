package com.example.proyectofinalut4.data

import androidx.room.Embedded
import androidx.room.Relation

data class TareasWithPrioridad(
    @Embedded val tarea: Tarea,
    @Relation(
        parentColumn = "idPrioridadOwner",
        entityColumn = "idPrioridad"
    )
    val prioridadTarea: Prioridad
)