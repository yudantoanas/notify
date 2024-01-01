package io.aeroflux.notify

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification (
    @PrimaryKey(autoGenerate = true) val uid:Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "body") val body: String?,
)
