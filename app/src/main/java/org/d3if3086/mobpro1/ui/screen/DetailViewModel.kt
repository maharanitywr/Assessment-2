package org.d3if3086.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3086.mobpro1.database.BaksoDao
import org.d3if3086.mobpro1.model.Bakso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: BaksoDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun insert(nama: String, deskripsi: String, review: String, rating: String) {
        val bakso = Bakso(
            tanggal = formatter.format(Date()),
            nama = nama,
            deskripsi = deskripsi,
            review = review,
            rating = rating
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(bakso)
        }
    }

    suspend fun getBakso(id: Long): Bakso? {
        return dao.getBaksoById(id)
    }

    fun update(id: Long, nama: String, deskripsi: String, review: String, rating: String) {
        val bakso = Bakso(
            id = id,
            tanggal = formatter.format(Date()),
            nama = nama,
            deskripsi = deskripsi,
            review = review,
            rating = rating
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(bakso)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deteleById(id)
        }
    }
}