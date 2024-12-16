package com.example.proyectofinalut4.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalut4.data.Tarea
import com.example.proyectofinalut4.data.TareaDao
import com.example.proyectofinalut4.data.TipoTarea
import com.example.proyectofinalut4.data.TipoTareaDao
import com.example.proyectofinalut4.data.TareasWithTipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel(private val tareaDao: TareaDao, private val tipoTareaDao: TipoTareaDao) : ViewModel() {

    // Estado observable con StateFlow
    private val _tiposList = MutableStateFlow<List<TipoTarea>>(emptyList())
    val tiposList: StateFlow<List<TipoTarea>> get() = _tiposList

    private val _tareasList = MutableStateFlow<List<TareasWithTipo>>(emptyList())
    val tareasList: StateFlow<List<TareasWithTipo>> get() = _tareasList

    val newTituloTipo = MutableStateFlow("")

    val newTituloTarea = MutableStateFlow("")
    val newDescription = MutableStateFlow("")
    val tipoSeleccionado = MutableStateFlow<TipoTarea?>(null)

    init {
        obtenerTipos()
        obtenerTareas()
    }

    // Obtener la lista de tipos
    private fun obtenerTipos() {
        viewModelScope.launch(Dispatchers.IO) {
            val tipos = tipoTareaDao.getAllTipos()
            _tiposList.emit(tipos) // Actualizar el flujo con la nueva lista
        }
    }

    // Obtener la lista de tareas con sus tipos
    private fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            val tareas = tareaDao.getAllTareasAndTipos()
            _tareasList.emit(tareas) // Actualizar el flujo con la nueva lista
        }
    }

    // Añadir un tipo nuevo
    fun agregarTipo() {
        val titulo = newTituloTipo.value
        if (titulo.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                // Crear un nuevo tipo
                val nuevoTipo = TipoTarea(tituloTipoTarea = titulo)

                // Insertar el nuevo tipo en la base de datos
                tipoTareaDao.insertTipoTarea(nuevoTipo)

                // Limpiar el estado de la entrada del usuario
                newTituloTipo.value = ""

                // Actualizar la lista de tipos desde la base de datos
                obtenerTipos()
            }
        }
    }

    // Actualizar un tipo existente
    fun actualizarTipo(tipoTarea: TipoTarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tipoTareaDao.update(tipoTarea)
            obtenerTipos() // Refrescar la lista
        }
    }

    // Borrar un tipo existente
    fun borrarTipo(tipoTarea: TipoTarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tipoTareaDao.delete(tipoTarea)
            obtenerTipos() // Refrescar la lista
        }
    }


}
