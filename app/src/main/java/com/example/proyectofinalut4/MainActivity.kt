package com.example.proyectofinalut4

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalut4.data.AppDatabase
import com.example.proyectofinalut4.ui.theme.ProyectoFinalUT4Theme
import com.example.proyectofinalut4.view.TareaApp
import com.example.proyectofinalut4.viewModel.MyViewModel
import com.example.proyectofinalut4.viewModel.ViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.proyectofinalut4.data.Tarea


class MainActivity : ComponentActivity() {

    // Activar la solicitud de permisos
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // El permiso fue concedido, ya puedes enviar la notificación
                Toast.makeText(this, "Permiso de notificación concedido", Toast.LENGTH_SHORT).show()
            } else {
                // El permiso fue denegado
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val tareaDao = database.tareaDao()
        val tipoTareaDao = database.tipoTareaDao()
        val prioridadDao = database.prioridadDao()

        enableEdgeToEdge()


        // Crear canal de notificación si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "tarea_channel",
                "Tareas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de tareas"
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Verificar si el permiso ya ha sido concedido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Si ya se tiene el permiso, puedes enviar la notificación
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
            } else {
                // Si no se tiene el permiso, solicitarlo
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
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

    // Método para mostrar la notificación
    fun showNotification(context: android.content.Context, tarea: Tarea) {
        var icono = R.drawable.ic_launcher_background
        var texto = "La tarea '${tarea.tituloTarea}' ha sido añadida"
        if (tarea.idPrioridadOwner == 1) {
            icono = R.drawable.prioridadbaja
            texto = "La tarea '${tarea.tituloTarea}' ha sido añadida con poca prioridad."
        }
        if (tarea.idPrioridadOwner == 2) {
            icono = R.drawable.prioridadmedia
            texto = "La tarea '${tarea.tituloTarea}' ha sido añadida con prioridad media."
        }
        if (tarea.idPrioridadOwner == 3){
            icono = R.drawable.prioridadalta
            texto = "La tarea '${tarea.tituloTarea}' ha sido añadida con alta prioridad."
        }
        val builder = NotificationCompat.Builder(context, "tarea_channel")
            .setSmallIcon(icono)
            .setContentTitle("Tarea añadida")
            .setContentText(texto)
            .setPriority(PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}