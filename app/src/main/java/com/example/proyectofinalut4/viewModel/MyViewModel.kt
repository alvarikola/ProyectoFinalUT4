package com.example.proyectofinalut4.viewModel

import com.example.proyectofinalut4.data.TareasWithTipo
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalut4.data.TareaDao
import com.example.proyectofinalut4.data.TipoTarea
import com.example.proyectofinalut4.data.TipoTareaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(private val tareaDao: TareaDao, private val tipoTareaDao: TipoTareaDao) : ViewModel() {

    // Estado de la lista de tipos y tareas
    var tiposList = mutableStateOf(listOf<TipoTarea>())
    var tareasList = mutableStateOf(listOf<TareasWithTipo>())

    var newTituloTipo = mutableStateOf("")
    var newTareaName = mutableStateOf("")
    var newDescription = mutableStateOf("")
    var tipoSeleccionado = mutableStateOf("-")
    var expandedDesplegable = mutableStateOf(false)

    init {
        obtenerTipos()
        obtenerTareas()
    }

    // Obtener tipos
    private fun obtenerTipos() {
        viewModelScope.launch(Dispatchers.IO) {
            tiposList.value = tipoTareaDao.getAllTipos()
        }
    }

    // Obtener tareas
    private fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            tareasList.value = tareaDao.getAllTareasAndTipos()
        }
    }

    // Añadir tipo
    fun agregarTipo() {
        viewModelScope.launch(Dispatchers.IO) {
            val tipo = TipoTarea(tituloTipoTarea = newTituloTipo.value)
            tipoTareaDao.insertTipoTarea(tipo)
            obtenerTipos() // Actualiza la lista después de añadir
        }
    }

    // Añadir tarea

//    fun agregarTarea() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val tarea = Tarea(
//                tituloTarea = newTareaName.value,
//                descripcionTarea = newDescription.value,
//                idTipoTareaOwner = tipoSeleccionado.value.toInt()
//            )
//            tareaDao.insertTarea(tarea)
//            obtenerTareas() // Actualiza la lista de tareas
//        }
//    }

    // Actualizar tipo
    fun actualizarTipo(tipoTarea: TipoTarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tipoTareaDao.update(tipoTarea)
            obtenerTipos() // Actualiza la lista de tipos
        }
    }

    // Borrar tipo
    fun borrarTipo(tipoTarea: TipoTarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tipoTareaDao.delete(tipoTarea)
            obtenerTipos() // Actualiza la lista de tipos
        }
    }

    // Borrar tarea
//    fun borrarTarea(idTarea: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val tarea = Tarea(idTarea = idTarea, tituloTarea = "", descripcionTarea = "", idTipoTareaOwner = 0)
//            tareaDao.delete(tarea)
//            obtenerTareas() // Actualiza la lista de tareas
//        }
//    }
}
