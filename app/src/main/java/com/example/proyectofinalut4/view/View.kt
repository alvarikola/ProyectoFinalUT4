package com.example.proyectofinalut4.view

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.proyectofinalut4.MainActivity
import com.example.proyectofinalut4.R
import com.example.proyectofinalut4.data.Tarea
import com.example.proyectofinalut4.viewModel.MyViewModel
import com.example.proyectofinalut4.data.TipoTarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FormularioTipos(myViewModel: MyViewModel) {
    val scope = rememberCoroutineScope()

    // Estados del ViewModel
    val newTituloTipo by myViewModel.newTituloTipo.collectAsState()
    val tiposList by myViewModel.tiposList.collectAsState()

    // Estados locales para los diálogos
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }

    // Estados locales para seleccionar un tipo
    var tipoSeleccionado by remember { mutableStateOf("") }
    var idTipoSeleccionado by remember { mutableStateOf(0) }
    var expandedDesplegableTipo by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = newTituloTipo,
        onValueChange = { myViewModel.newTituloTipo.value = it },
        label = { Text("Tipo") },
        modifier = Modifier.fillMaxWidth()
    )

    Row {
        Button(
            onClick = {
                scope.launch {
                    myViewModel.agregarTipo()
                }
            }
        ) {
            Text(text = "Añadir tipo")
        }

        Button(
            onClick = {
                mostrarDialogoEditar = true
            }
        ) {
            Text(text = "Editar tipo")
        }

        if (mostrarDialogoEditar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoEditar = false },
                title = { Text(text = "Editar") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = newTituloTipo,
                            onValueChange = { myViewModel.newTituloTipo.value = it },
                            label = { Text("Nuevo tipo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = tipoSeleccionado,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Tipo seleccionado") },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp)
                            )
                            TextButton(
                                onClick = { expandedDesplegableTipo = true }
                            ) {
                                Text("Seleccionar tipo")
                            }
                        }

                        DropdownMenu(
                            expanded = expandedDesplegableTipo,
                            onDismissRequest = { expandedDesplegableTipo = false }
                        ) {
                            tiposList.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.tituloTipoTarea) },
                                    onClick = {
                                        idTipoSeleccionado = tipo.idTipoTarea
                                        tipoSeleccionado = tipo.tituloTipoTarea
                                        expandedDesplegableTipo = false
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            myViewModel.actualizarTipo(
                                TipoTarea(idTipoTarea = idTipoSeleccionado, tituloTipoTarea = newTituloTipo)
                            )
                            mostrarDialogoEditar = false
                        }
                    }) {
                        Text("Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoEditar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Button(
            onClick = {
                mostrarDialogoBorrar = true
            }
        ) {
            Text(text = "Borrar tipo")
        }

        if (mostrarDialogoBorrar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrar = false },
                title = { Text(text = "Borrar") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = tipoSeleccionado,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Tipo seleccionado") },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp)
                            )
                            TextButton(
                                onClick = { expandedDesplegableTipo = true }
                            ) {
                                Text("Seleccionar tipo")
                            }
                        }

                        DropdownMenu(
                            expanded = expandedDesplegableTipo,
                            onDismissRequest = { expandedDesplegableTipo = false }
                        ) {
                            tiposList.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.tituloTipoTarea) },
                                    onClick = {
                                        idTipoSeleccionado = tipo.idTipoTarea
                                        tipoSeleccionado = tipo.tituloTipoTarea
                                        expandedDesplegableTipo = false
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            myViewModel.borrarTipo(
                                TipoTarea(idTipoTarea = idTipoSeleccionado, tituloTipoTarea = tipoSeleccionado)
                            )
                            mostrarDialogoBorrar = false
                        }
                    }) {
                        Text("Borrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun FormularioTareas(myViewModel: MyViewModel) {

    val context = LocalContext.current

    // Obtener los valores desde el ViewModel
    val newTituloTarea by myViewModel.newTituloTarea.collectAsState()
    val newDescription by myViewModel.newDescription.collectAsState()
    val tipoSeleccionado by myViewModel.tipoSeleccionado.collectAsState()
    val prioridadSeleccionada by myViewModel.prioridadSeleccionada.collectAsState()

    // Estados locales para manejar la visibilidad del DropdownMenu
    var expandedDesplegableTipo by remember { mutableStateOf(false) }
    var expandedDesplegablePrio by remember { mutableStateOf(false) }

    // Lista de tipos de tarea desde el ViewModel
    val tiposList by myViewModel.tiposList.collectAsState()
    val prioridadesList by myViewModel.prioridadesList.collectAsState()

    // Campos para seleccionar el tipo de tarea
    var tipoSeleccionadoLocal by remember { mutableStateOf("") }
    var idTipoSeleccionadoLocal by remember { mutableStateOf(0) }

    var prioridadSeleccionadaLocal by remember { mutableStateOf("") }
    var idPrioridadSeleccionadaLocal by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Campo para el título de la tarea
        OutlinedTextField(
            value = newTituloTarea,
            onValueChange = { myViewModel.newTituloTarea.value = it },
            label = { Text("Nueva Tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para la descripción de la tarea
        OutlinedTextField(
            value = newDescription,
            onValueChange = { myViewModel.newDescription.value = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        // Fila para seleccionar el tipo de tarea
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = tipoSeleccionado?.tituloTipoTarea ?: "", // Mostrar una cadena vacía si es nulo
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            )
            TextButton(
                onClick = { expandedDesplegableTipo = true }
            ) {
                Text("Seleccionar tipo")
            }
        }

        // Dropdown para seleccionar el tipo de tarea
        DropdownMenu(
            expanded = expandedDesplegableTipo,
            onDismissRequest = { expandedDesplegableTipo = false }
        ) {
            tiposList.forEach { tipo ->
                DropdownMenuItem(
                    text = { Text(tipo.tituloTipoTarea) },
                    onClick = {
                        tipoSeleccionadoLocal = tipo.tituloTipoTarea
                        idTipoSeleccionadoLocal = tipo.idTipoTarea
                        expandedDesplegableTipo = false

                        // Actualizar el estado en el ViewModel
                        myViewModel.tipoSeleccionado.value = tipo
                    }
                )
            }
        }
    }

    // Fila para seleccionar la prioridad de tarea
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = prioridadSeleccionada?.tituloPrioridad ?: "", // Mostrar una cadena vacía si es nulo
            onValueChange = {},
            readOnly = true,
            label = { Text("Prioridad") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 5.dp)
        )
        TextButton(
            onClick = { expandedDesplegablePrio = true }
        ) {
            Text("Seleccionar prioridad")
        }
    }

    // Dropdown para seleccionar la prioridad de tarea
    DropdownMenu(
        expanded = expandedDesplegablePrio,
        onDismissRequest = { expandedDesplegablePrio = false }
    ) {
        prioridadesList.forEach { prioridad ->
            DropdownMenuItem(
                text = { Text(prioridad.tituloPrioridad) },
                onClick = {
                    prioridadSeleccionadaLocal = prioridad.tituloPrioridad
                    idPrioridadSeleccionadaLocal = prioridad.idPrioridad
                    expandedDesplegablePrio = false

                    // Actualizar el estado en el ViewModel
                    myViewModel.prioridadSeleccionada.value = prioridad
                }
            )
        }
    }


    // Botón para añadir tarea
    Button(
        onClick = {
            myViewModel.agregarTarea()  // Llamar al metodo en el ViewModel para agregar tarea
            // Limpiar los campos después de agregar la tarea
            myViewModel.newTituloTarea.value = ""
            myViewModel.newDescription.value = ""
            myViewModel.tipoSeleccionado.value = null
            myViewModel.prioridadSeleccionada.value = null
            // Verificar si se tiene el permiso antes de mostrar la notificación
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                (context as MainActivity).showNotification(context, Tarea(tituloTarea = newTituloTarea, descripcionTarea = newDescription, idTipoTareaOwner = idTipoSeleccionadoLocal, idPrioridadOwner = idPrioridadSeleccionadaLocal))
            } else {
                Toast.makeText(context, "Se necesita permiso para notificaciones", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Añadir tarea")
    }
}


@Composable
fun ListaTareas(myViewModel: MyViewModel) {

    // Observamos los flujos del ViewModel
    val tareasWithTipo by myViewModel.tareasList.collectAsState()
    val tiposList by myViewModel.tiposList.collectAsState()
    val prioridadesList by myViewModel.prioridadesList.collectAsState()


    // Estados para gestionar el diálogo y la selección actual
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var expandedDesplegableTipo by remember { mutableStateOf(false) }
    var expandedDesplegablePrio by remember { mutableStateOf(false) }

    // Estados para editar la tarea seleccionada
    var newTareaName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf<String?>("") }
    var newTipoTarea by remember { mutableStateOf(0) }
    var newPrioridadTarea by remember { mutableStateOf(0) }

    var tipoSeleccionado by remember { mutableStateOf("") }
    var idTareaSeleccionada by remember { mutableStateOf(0) }

    var prioridadSeleccionada by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Mostrar lista de tareas
        if (!mostrarDialogoEditar) {
            tareasWithTipo.forEach { tareaWithTipo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color(0xFFb7bbff))
                        .padding(10.dp)
                ) {
                    // Información de la tarea
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tarea:", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("Tipo: ${tareaWithTipo.tiposTareas.tituloTipoTarea}", fontSize = 20.sp)
                        Text("${tareaWithTipo.tarea.idTarea} - ${tareaWithTipo.tarea.tituloTarea}", fontSize = 20.sp)
                        tareaWithTipo.tarea.descripcionTarea?.let {
                            Text(it, fontSize = 20.sp)
                        }
                    }

                    // Botones para editar y borrar
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Button(
                            onClick = {
                                idTareaSeleccionada = tareaWithTipo.tarea.idTarea
                                newTareaName = tareaWithTipo.tarea.tituloTarea
                                newDescription = tareaWithTipo.tarea.descripcionTarea
                                newTipoTarea = tareaWithTipo.tarea.idTipoTareaOwner
                                tipoSeleccionado = tareaWithTipo.tiposTareas.tituloTipoTarea
                                mostrarDialogoEditar = true
                            }
                        ) {
                            Text("Editar tarea")
                        }

                        Button(
                            onClick = {
                                myViewModel.borrarTarea(tareaWithTipo.tarea)
                            }
                        ) {
                            Text("Borrar tarea")
                        }
                    }
                }
            }
        } else {
            // Diálogo para editar tarea
            AlertDialog(
                onDismissRequest = { mostrarDialogoEditar = false },
                title = { Text("Editar tarea") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo para editar el nombre de la tarea
                        OutlinedTextField(
                            value = newTareaName,
                            onValueChange = { newTareaName = it },
                            label = { Text("Editar Tarea") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Campo para editar la descripción
                        OutlinedTextField(
                            value = newDescription ?: "",
                            onValueChange = { newDescription = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Selección de tipo
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = tipoSeleccionado,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tipo") },
                                modifier = Modifier.weight(1f).padding(end = 5.dp)
                            )
                            TextButton(onClick = { expandedDesplegableTipo = true }) {
                                Text("Seleccionar tipo")
                            }
                        }

                        // Menú desplegable para seleccionar tipo
                        DropdownMenu(
                            expanded = expandedDesplegableTipo,
                            onDismissRequest = { expandedDesplegableTipo = false }
                        ) {
                            tiposList.forEach { tipo ->
                                DropdownMenuItem(
                                    text = {
                                        Text(tipo.tituloTipoTarea)
                                    },
                                    onClick = {
                                        newTipoTarea = tipo.idTipoTarea
                                        tipoSeleccionado = tipo.tituloTipoTarea
                                        expandedDesplegableTipo = false
                                    }
                                )
                            }
                        }

                        // Fila para seleccionar la prioridad de tarea
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = prioridadSeleccionada,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Prioridad") },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp)
                            )
                            TextButton(
                                onClick = { expandedDesplegablePrio = true }
                            ) {
                                Text("Seleccionar prioridad")
                            }
                        }

                        // Dropdown para seleccionar la prioridad de tarea
                        DropdownMenu(
                            expanded = expandedDesplegablePrio,
                            onDismissRequest = { expandedDesplegablePrio = false }
                        ) {
                            prioridadesList.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.tituloPrioridad) },
                                    onClick = {
                                        newPrioridadTarea = prioridad.idPrioridad
                                        prioridadSeleccionada = prioridad.tituloPrioridad
                                        expandedDesplegablePrio = false

                                        // Actualizar el estado en el ViewModel
                                        myViewModel.prioridadSeleccionada.value = prioridad
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        // Usamos el ViewModel para actualizar la tarea
                        val tareaActualizada = Tarea(
                            idTarea = idTareaSeleccionada,
                            tituloTarea = newTareaName,
                            descripcionTarea = newDescription,
                            idTipoTareaOwner = newTipoTarea,
                            idPrioridadOwner = newPrioridadTarea
                        )
                        myViewModel.actualizarTarea(tareaActualizada)
                        mostrarDialogoEditar = false
                    }) {
                        Text("Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoEditar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}



@Composable
fun TareaApp(modifier: Modifier = Modifier, myViewModel: MyViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestor de tareas")

        FormularioTipos(myViewModel)
        FormularioTareas(myViewModel)
        ListaTareas(myViewModel)
    }
}

