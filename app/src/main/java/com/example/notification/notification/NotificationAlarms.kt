package com.example.notification.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notification.Constants.CHANNEL_ID_1
import com.example.notification.Constants.CHANNEL_ID_2
import com.example.notification.Constants.CHANNEL_ID_3
import com.example.notification.Constants.FIRST
import com.example.notification.Constants.NOTIFICATIONID
import com.example.notification.Constants.SCREEN_TYPE
import com.example.notification.Constants.SECOND
import com.example.notification.Constants.THIRD
import com.example.notification.MainActivity
import com.example.notification.R
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationAlarms : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("NotificationLog", "${intent.getStringExtra(NOTIFICATIONID)}")
        when (intent.getStringExtra(NOTIFICATIONID)) {
            CHANNEL_ID_1 -> setFirstNotification(context)
            CHANNEL_ID_2 -> setSecondNotification(context)
            CHANNEL_ID_3 -> setThirdNotification(context)
        }
    }

    private fun setFirstNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            notify(10, createNotification(context, CHANNEL_ID_1, 100, FIRST))
        }
    }

    private fun setSecondNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            notify(11, createNotification(context, CHANNEL_ID_2, 101, SECOND))
        }
    }

    private fun setThirdNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            notify(12, createNotification(context, CHANNEL_ID_3, 102, THIRD))
        }
    }

    private fun createNotification(context: Context, CHANNEL_ID: String, requestCode: Int, notificationType: String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationName = context.resources.getString(R.string.app_name)
            val notificationImportance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, notificationName, notificationImportance)
            val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val openNotificationIntent = Intent(context, MainActivity::class.java)
        openNotificationIntent.putExtra(SCREEN_TYPE, notificationType)

        val pendingIntent = PendingIntent.getActivity(context, requestCode, openNotificationIntent, 0)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(setNotificationIcon(CHANNEL_ID))
            .setContentTitle(setNotificationTitle(context, CHANNEL_ID))
            .setContentText(setNotificationBody(context, CHANNEL_ID))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun setNotificationIcon(CHANNEL_ID: String): Int = when (CHANNEL_ID) {
        CHANNEL_ID_1 -> R.mipmap.ic_launcher
        else -> R.mipmap.ic_launcher
    }

    private fun setNotificationTitle(context: Context, CHANNEL_ID: String): String = when (CHANNEL_ID) {
        CHANNEL_ID_1 -> context.resources.getString(R.string.first_notification_title)
        CHANNEL_ID_2 -> context.resources.getString(R.string.second_notification_title)
        else -> context.resources.getString(R.string.third_notification_title)
    }

    private fun setNotificationBody(context: Context, CHANNEL_ID: String): String = when (CHANNEL_ID) {
        CHANNEL_ID_1 -> context.resources.getString(R.string.first_notification_body)
        CHANNEL_ID_2 -> context.resources.getString(R.string.second_notification_body)
        else -> context.resources.getString(R.string.third_notification_body)
    }

    object NotificationsHelper {
        fun enableNotificationAlarms(activity: Activity, requestCode: Int, hoursNextPush: Int, CHANNEL_ID: String) {
            val rightNow: Calendar = Calendar.getInstance()
            val currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY)
            val currentMinute = rightNow.get(Calendar.MINUTE)

            Log.i("NotificationLog", "$currentHourIn24Format")

            val alarmsManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(activity, NotificationAlarms::class.java)
            intent.putExtra(NOTIFICATIONID, CHANNEL_ID)

            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(activity, requestCode, intent, 0)

            val calendarFiring = Calendar.getInstance()
//            val calendarCurrent = Calendar.getInstance()

            calendarFiring.set(Calendar.HOUR_OF_DAY, currentHourIn24Format)
            calendarFiring.set(Calendar.MINUTE, currentMinute + hoursNextPush)
            calendarFiring.set(Calendar.SECOND, 0)

//            var intendedNotificationTime = calendarFiring.timeInMillis
//            val currentNotificationTime = calendarCurrent.timeInMillis

            alarmsManager.setRepeating(AlarmManager.RTC, calendarFiring.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

            /*if (intendedNotificationTime >= currentNotificationTime) {
                alarmsManager.setRepeating(AlarmManager.RTC, intendedNotificationTime, AlarmManager.INTERVAL_DAY, pendingIntent)
            } else {
                calendarFiring.add(Calendar.DAY_OF_MONTH, 1)
                intendedNotificationTime = calendarFiring.timeInMillis

                alarmsManager.setRepeating(AlarmManager.RTC, intendedNotificationTime, AlarmManager.INTERVAL_DAY, pendingIntent)
            }*/

        }

        /*private fun millisToDate(millis: Long) {
            val time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            Log.i("NotificationLog", time)
        }*/
    }

}