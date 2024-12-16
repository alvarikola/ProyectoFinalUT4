package com.example.proyectofinalut4.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    var expandedDesplegable by remember { mutableStateOf(false) }

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
                                onClick = { expandedDesplegable = true }
                            ) {
                                Text("Seleccionar tipo")
                            }
                        }

                        DropdownMenu(
                            expanded = expandedDesplegable,
                            onDismissRequest = { expandedDesplegable = false }
                        ) {
                            tiposList.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.tituloTipoTarea) },
                                    onClick = {
                                        idTipoSeleccionado = tipo.idTipoTarea
                                        tipoSeleccionado = tipo.tituloTipoTarea
                                        expandedDesplegable = false
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
                                onClick = { expandedDesplegable = true }
                            ) {
                                Text("Seleccionar tipo")
                            }
                        }

                        DropdownMenu(
                            expanded = expandedDesplegable,
                            onDismissRequest = { expandedDesplegable = false }
                        ) {
                            tiposList.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo.tituloTipoTarea) },
                                    onClick = {
                                        idTipoSeleccionado = tipo.idTipoTarea
                                        tipoSeleccionado = tipo.tituloTipoTarea
                                        expandedDesplegable = false
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
    val newTituloTarea by myViewModel.newTituloTarea.collectAsState()
    val newDescription by myViewModel.newDescription.collectAsState()
    val tipoSeleccionado by myViewModel.tipoSeleccionado.collectAsState()


    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = newTituloTarea,
            onValueChange = { myViewModel.newTituloTarea.value = it },
            label = { Text("Nueva Tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newDescription,
            onValueChange = { myViewModel.newDescription.value = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                myViewModel.agregarTarea()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Añadir tarea")
        }
    }
}

//@Composable
//fun ListaTareas(viewModel: ViewModel) {
//    val tareasList by remember { viewModel.tareasList }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        tareasList.forEach { tarea ->
//            Text("Tarea: ${tarea.tarea.tituloTarea}")
//        }
//    }
//}

@Composable
fun TareaApp(modifier: Modifier = Modifier, myViewModel: MyViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestor de tareas")

        FormularioTipos(myViewModel)
//        FormularioTareas(myViewModel)
//        ListaTareas(myViewModel)
    }
}

