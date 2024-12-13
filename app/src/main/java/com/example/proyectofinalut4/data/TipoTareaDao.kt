package com.example.proyectofinalut4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoTareaDao {

    @Insert
    suspend fun insertTipoTarea(tipoTarea: TipoTarea)

    @Update
    suspend fun update(tipoTarea: TipoTarea)

    @Delete
    suspend fun delete(tipoTarea: TipoTarea)

    @Query("SELECT * FROM TiposTareas")
    suspend fun getAllTipos(): List<TipoTarea>

}