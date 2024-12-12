package com.example.proyectofinalut4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PrioridadDao {
    @Insert
    suspend fun insertPrioridad(prioridad: Prioridad)

    @Update
    suspend fun update(prioridad: Prioridad)

    @Delete
    suspend fun delete(prioridad: Prioridad)

    @Query("SELECT * FROM Prioridades")
    suspend fun getAllPrioridades(): Flow<List<Prioridad>>
}