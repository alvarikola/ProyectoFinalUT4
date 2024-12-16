package com.example.proyectofinalut4.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinalut4.data.PrioridadDao
import com.example.proyectofinalut4.data.TareaDao
import com.example.proyectofinalut4.data.TipoTareaDao

class ViewModelFactory(private val tareaDao: TareaDao, private val tipoTareaDao: TipoTareaDao, private val prioridadDao: PrioridadDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(tareaDao, tipoTareaDao, prioridadDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}