package io.aeroflux.notify

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private val notifViewModel: NotifyViewModel by viewModels {
        NotifyViewModelFactory((application as NotifyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "Device Token: $token")
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotificationListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        notifViewModel.data.observe(this, Observer { list ->
            // Update the cached copy of the words in the adapter.
            list?.let { adapter.submitList(it) }
        })
    }
}