package com.example.proyectofinalut4.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinalut4.data.TareaDao
import com.example.proyectofinalut4.data.TipoTareaDao

class ViewModelFactory(private val tareaDao: TareaDao, private val tipoTareaDao: TipoTareaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(tareaDao, tipoTareaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}