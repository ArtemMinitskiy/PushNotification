package com.example.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notification.Constants.CHANNEL_ID_1
import com.example.notification.Constants.CHANNEL_ID_2
import com.example.notification.Constants.CHANNEL_ID_3
import com.example.notification.Constants.SCREEN_TYPE
import com.example.notification.databinding.ActivityMainBinding
import com.example.notification.notification.NotificationAlarms

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRandomNotification()
        transitionByNotification()
    }

    private fun setRandomNotification() {
        val randomNotificationList = mutableListOf(CHANNEL_ID_1, CHANNEL_ID_2, CHANNEL_ID_3)
        randomNotificationList.shuffle()

        NotificationAlarms.NotificationsHelper.enableNotificationAlarms(this, 1, 1, randomNotificationList[0])
        NotificationAlarms.NotificationsHelper.enableNotificationAlarms(this, 2, 5, randomNotificationList[1])
        NotificationAlarms.NotificationsHelper.enableNotificationAlarms(this, 3, 10, randomNotificationList[2])
    }

    //Define what screen open by Notification
    private fun transitionByNotification() {
//        NotificationManagerCompat.from(this).cancelAll()
        if (intent.hasExtra(SCREEN_TYPE)) {
            intent.getStringExtra(SCREEN_TYPE)?.let {
                binding.notificationText.text = it
            }
        }
    }
}