package io.aeroflux.notify

import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NotifyRepository(private val dao: NotificationDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allNotif: Flow<List<Notification>> = dao.getAll()
}