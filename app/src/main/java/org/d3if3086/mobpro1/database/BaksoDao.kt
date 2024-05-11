package org.d3if3086.mobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3086.mobpro1.model.Bakso

@Dao
interface BaksoDao {

    @Insert
    suspend fun insert(bakso: Bakso)

    @Update
    suspend fun update(bakso: Bakso)

    @Query("SELECT * FROM bakso ORDER BY rating DESC")
    fun getBakso(): Flow<List<Bakso>>

    @Query("SELECT * FROM bakso WHERE id = :id")
    suspend fun getBaksoById(id: Long): Bakso?

    @Query("DELETE FROM bakso WHERE id = :id")
    suspend fun deteleById(id: Long)
}