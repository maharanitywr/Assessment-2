package org.d3if3086.mobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bakso")
data class Bakso(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val deskripsi: String,
    val review: String,
    val rating: String,
    val tanggal: String
)
