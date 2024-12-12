package com.example.proyectofinalut4.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prioridades")
data class Prioridad (
    @PrimaryKey(autoGenerate = true) val idPrioridad: Int = 0,
    @ColumnInfo(name = "Título") val tituloPrioridad: String,
)