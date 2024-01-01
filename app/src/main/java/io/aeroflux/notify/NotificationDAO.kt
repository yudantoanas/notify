package io.aeroflux.notify

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDAO {
    @Query("SELECT * FROM notification")
    fun getAll(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Notification)
}