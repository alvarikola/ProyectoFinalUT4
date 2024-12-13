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
    var newTituloTipo by remember { myViewModel.newTituloTipo }

    val scope = rememberCoroutineScope()

    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }

    var tipoSeleccionado by remember { mutableStateOf("-") }
    var expandedDesplegable by remember { mutableStateOf(false) }
    var tiposList by remember { myViewModel.tiposList }
    var newTipoTarea by remember { mutableIntStateOf(0) }
    var actualizaEstado by remember { mutableStateOf(false) }



    OutlinedTextField(
        value = newTituloTipo,
        onValueChange = { newTituloTipo = it },
        label = { Text("Tipo") },
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    TipoTarea(tituloTipoTarea = newTituloTipo)
                    myViewModel.agregarTipo()
                    newTituloTipo = ""
                    actualizaEstado = true
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
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = newTituloTipo,
                            onValueChange = { newTituloTipo = it },
                            label = { Text("Tipo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = tipoSeleccionado,
                                onValueChange = { tipoSeleccionado = it },
                                readOnly = true,
                                label = { Text("Tipo") },
                                modifier = Modifier.weight(1f).padding(end = 5.dp)
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
                                    text = {
                                        Text(tipo.tituloTipoTarea)
                                    },
                                    onClick = {
                                        newTipoTarea = tipo.idTipoTarea
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
                        scope.launch(Dispatchers.IO) {
                            val tipoActualizado = TipoTarea(idTipoTarea = newTipoTarea, tituloTipoTarea = newTituloTipo)
                            myViewModel.actualizarTipo(tipoActualizado)
                            newTituloTipo = ""
                            actualizaEstado = true
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
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = tipoSeleccionado,
                                onValueChange = { tipoSeleccionado = it },
                                readOnly = true,
                                label = { Text("Tipo") },
                                modifier = Modifier.weight(1f).padding(end = 5.dp)
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
                                    text = {
                                        Text(tipo.tituloTipoTarea)
                                    },
                                    onClick = {
                                        newTipoTarea = tipo.idTipoTarea
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
                        scope.launch(Dispatchers.IO) {
                            val tipoBorrar = TipoTarea(idTipoTarea = newTipoTarea, tituloTipoTarea = tipoSeleccionado)
                            myViewModel.borrarTipo(tipoBorrar)
                            actualizaEstado = true
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

//@Composable
//fun FormularioTareas(viewModel: ViewModel) {
//    val newTareaName by remember { viewModel.newTareaName }
//    val newDescription by remember { viewModel.newDescription }
//    val tipoSeleccionado by remember { viewModel.tipoSeleccionado }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        OutlinedTextField(
//            value = newTareaName,
//            onValueChange = { viewModel.newTareaName.value = it },
//            label = { Text("Nueva Tarea") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = newDescription,
//            onValueChange = { viewModel.newDescription.value = it },
//            label = { Text("Descripción") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            onClick = {
//                viewModel.agregarTarea()
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text("Añadir tarea")
//        }
//    }
//}
//
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
//        FormularioTareas(viewModel)
//        ListaTareas(viewModel)
    }
}

