package com.example.proyectofinalut4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [Tarea::class, TipoTarea::class, Prioridad::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao
    abstract fun tipoTareaDao(): TipoTareaDao
    abstract fun prioridadDao(): PrioridadDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        @OptIn(DelicateCoroutinesApi::class)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestor_tareas_database" ).build()
                INSTANCE = instance

                // Insertar las prioridades si la base de datos está vacía
                GlobalScope.launch {
                    datosIniciales(instance.prioridadDao())
                }

                instance
            }
        }
        // Función para insertar las prioridades
        private suspend fun datosIniciales(prioridadDao: PrioridadDao) {
            // Insertar prioridades
            val prioridades = listOf(
                Prioridad(tituloPrioridad = "Baja"),
                Prioridad(tituloPrioridad = "Media"),
                Prioridad(tituloPrioridad = "Alta"),
            )

            val prioridadesFromDb = prioridadDao.getAllPrioridades()

            // Si no existen prioridades en la base de datos las insertamos
            if (prioridadesFromDb.isEmpty()) {
                prioridades.forEach {
                    prioridadDao.insertPrioridad(it)
                }
            }
        }
    }
}