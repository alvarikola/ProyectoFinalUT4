package com.example.proyectofinalut4.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalut4.data.Prioridad
import com.example.proyectofinalut4.data.PrioridadDao
import com.example.proyectofinalut4.data.Tarea
import com.example.proyectofinalut4.data.TareaDao
import com.example.proyectofinalut4.data.TipoTarea
import com.example.proyectofinalut4.data.TipoTareaDao
import com.example.proyectofinalut4.data.TareasWithTipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel(private val tareaDao: TareaDao, private val tipoTareaDao: TipoTareaDao, private val prioridadDao: PrioridadDao) : ViewModel() {

    // Estado observable con StateFlow
    private val _tiposList = MutableStateFlow<List<TipoTarea>>(emptyList())
    val tiposList: StateFlow<List<TipoTarea>> get() = _tiposList

    private val _tareasList = MutableStateFlow<List<TareasWithTipo>>(emptyList())
    val tareasList: StateFlow<List<TareasWithTipo>> get() = _tareasList

    private val _prioridadesList = MutableStateFlow<List<Prioridad>>(emptyList())
    val prioridadesList: StateFlow<List<Prioridad>> get() = _prioridadesList

    val newTituloTipo = MutableStateFlow("")

    val newTituloTarea = MutableStateFlow("")
    val newDescription = MutableStateFlow("")
    val tipoSeleccionado = MutableStateFlow<TipoTarea?>(null)

    init {
        obtenerTipos()
        obtenerTareas()
    }

    // Obtener la lista de tipos
    fun obtenerTipos() {
        viewModelScope.launch(Dispatchers.IO) {
            val tipos = tipoTareaDao.getAllTipos()
            _tiposList.emit(tipos) // Actualizar el flujo con la nueva lista
        }
    }

    // Obtener la lista de tareas con sus tipos
    fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            val tareas = tareaDao.getAllTareasAndTipos()
            _tareasList.emit(tareas) // Actualizar el flujo con la nueva lista
        }
    }

    fun obtenerPrioridades() {
        viewModelScope.launch(Dispatchers.IO) {
            val prioridades = prioridadDao.getAllPrioridades()
            _tareasList.emit(prioridades) // Actualizar el flujo con la nueva lista
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


    // Crear una nueva tarea
    fun agregarTarea() {
        val titulo = newTituloTarea.value
        val descripcion = newDescription.value
        val tipoId = tipoSeleccionado.value?.idTipoTarea ?: 0

        if (titulo.isNotEmpty() && tipoId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                // Crear la nueva tarea
                val nuevaTarea = Tarea(
                    tituloTarea = titulo,
                    descripcionTarea = if (descripcion.isEmpty()) null else descripcion,
                    idTipoTareaOwner = tipoId,
                    idPrioridadOwner = 1 // Aquí asignamos una prioridad, en este caso como ejemplo.
                )

                // Insertar la nueva tarea en la base de datos
                tareaDao.insertTarea(nuevaTarea)

                // Limpiar los campos de entrada
                newTituloTarea.value = ""
                newDescription.value = ""
                tipoSeleccionado.value = null

                // Actualizar la lista de tareas
                obtenerTareas()
            }
        }
    }

    // Actualizar una tarea existente
    fun actualizarTarea(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tareaDao.update(tarea) // Actualiza la tarea en la base de datos
            obtenerTareas() // Refrescar la lista de tareas
        }
    }

    // Eliminar una tarea existente
    fun borrarTarea(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            tareaDao.delete(tarea) // Elimina la tarea de la base de datos
            obtenerTareas() // Refrescar la lista de tareas
        }
    }

}
