package com.example.proyectofinalut4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiposTareas")
data class TipoTarea(
    @PrimaryKey(autoGenerate = true) val idTipoTarea: Int = 0,
    val tituloTipoTarea: String
)