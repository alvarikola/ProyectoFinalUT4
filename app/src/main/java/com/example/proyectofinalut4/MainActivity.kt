package com.example.proyectofinalut4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalut4.data.AppDatabase
import com.example.proyectofinalut4.ui.theme.ProyectoFinalUT4Theme
import com.example.proyectofinalut4.view.TareaApp
import com.example.proyectofinalut4.viewModel.MyViewModel
import com.example.proyectofinalut4.viewModel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val tareaDao = database.tareaDao()
        val tipoTareaDao = database.tipoTareaDao()
        val prioridadDao = database.prioridadDao()

        enableEdgeToEdge()
        setContent {
            ProyectoFinalUT4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val myViewModel: MyViewModel = viewModel(factory = ViewModelFactory(tareaDao, tipoTareaDao, prioridadDao))


                    TareaApp(
                        modifier = Modifier.padding(innerPadding),
                        myViewModel = myViewModel
                    )
                }
            }
        }
    }
}