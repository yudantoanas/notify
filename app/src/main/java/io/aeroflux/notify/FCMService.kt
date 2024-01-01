package io.aeroflux.notify

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room.databaseBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {
    private val TAG = this.javaClass.simpleName

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        getFirebaseMessage(message.notification?.title, message.notification?.body)
    }

    private fun getFirebaseMessage(title: String?, body: String?) {
        val notificationDatabase = AppDatabase.getDatabase(applicationContext)
        val notification = Notification(title = title, body = body)

        runBlocking {
            launch(Dispatchers.IO) {
                notificationDatabase.notificationDAO().insert(notification)
            }
        }

        // Create the NotificationChannel.
        val name = getString(R.string.channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel("notify", name, importance)

        val builder = NotificationCompat.Builder(this, "notify")
            .setSmallIcon(R.drawable.ic_android_blue_24dp)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(this)
        manager.createNotificationChannel(mChannel)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        manager.notify(1, builder.build())
    }
}