package com.aleksej.makaji.listopia.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.aleksej.makaji.listopia.HomeActivity
import com.aleksej.makaji.listopia.R
import java.lang.Long

/**
 * Created by Aleksej Makaji on 2019-12-07.
 */
object NotificationBarHandler {

    private const val SHOPPING_LIST_CHANNLE_ID = "shopping_list_channel_id"
    private const val SHOPPING_LIST_CHANNLE_TITLE = "Offer Channel title"
    private const val INTERNT_CONNECTION_CHANNLE_ID = "internet_connection_channel_id"
    private const val INTERNT_CONNECTION_CHANNLE_TITLE = "Internet connection channel title"
    const val ACTION_SHOPPING_LIST = "ACTION_SHOPPING_LIST"
    const val ACTION_SHOPPING_LIST_EXTRA_ID = "ACTION_SHOPPING_LIST_EXTRA_ID"
    private const val GROUP_SHOPPING_LIST_ID = 1000
    private const val GROUP_INTERNET_CONNECTION_ID = 2000
    private const val INTERNET_CONNECTION_ID = 1
    private const val SHOPPING_LIST_GROUP = "SHOPPING_LIST_GROUP"
    private const val INTERNET_CONNECTION_GROUP = "INTERNET_CONNECTION_GROUP"

    fun showShoppingListMesssageNotification(title: String?, body: String, context: Context, shoppingListId: String) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(ACTION_SHOPPING_LIST_EXTRA_ID, shoppingListId)
        intent.action = ACTION_SHOPPING_LIST
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, IdParser.parse(shoppingListId), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notificationManager?.getNotificationChannel(SHOPPING_LIST_CHANNLE_ID)
            if (mChannel == null) {
                mChannel = NotificationChannel(SHOPPING_LIST_CHANNLE_ID, SHOPPING_LIST_CHANNLE_TITLE, importance)
                notificationManager?.createNotificationChannel(mChannel)
            }
        }

        val mBuilder = NotificationCompat.Builder(context, SHOPPING_LIST_CHANNLE_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(SHOPPING_LIST_GROUP)
                .setGroupSummary(false)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)

        val builderSummary: NotificationCompat.Builder =
                NotificationCompat.Builder(context, SHOPPING_LIST_CHANNLE_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setGroup(SHOPPING_LIST_GROUP)
                        .setSound(null)
                        .setGroupSummary(true)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)

        notificationManager?.notify(IdParser.parse(shoppingListId), mBuilder?.build())
        notificationManager?.notify(GROUP_SHOPPING_LIST_ID, builderSummary?.build())

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
        powerManager?.run {
            val screenLock = newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.aleksej.makaji.listopia:wakelock")
            screenLock.acquire(10)
        }
    }

    fun showNotificationNoInternetConnection(title: String?, body: String, context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, INTERNET_CONNECTION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notificationManager?.getNotificationChannel(INTERNT_CONNECTION_CHANNLE_ID)
            if (mChannel == null) {
                mChannel = NotificationChannel(INTERNT_CONNECTION_CHANNLE_ID, INTERNT_CONNECTION_CHANNLE_TITLE, importance)
                notificationManager?.createNotificationChannel(mChannel)
            }
        }

        val mBuilder = NotificationCompat.Builder(context, INTERNT_CONNECTION_CHANNLE_ID)
                .setSmallIcon(R.drawable.ic_no_network)
                .setGroup(INTERNET_CONNECTION_GROUP)
                .setGroupSummary(false)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)

        val builderSummary: NotificationCompat.Builder =
                NotificationCompat.Builder(context, INTERNT_CONNECTION_CHANNLE_ID)
                        .setSmallIcon(R.drawable.ic_no_network)
                        .setGroup(INTERNET_CONNECTION_GROUP)
                        .setSound(null)
                        .setGroupSummary(true)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)

        notificationManager?.notify(INTERNET_CONNECTION_ID, mBuilder?.build())
        notificationManager?.notify(GROUP_INTERNET_CONNECTION_ID, builderSummary?.build())

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
        powerManager?.run {
            val screenLock = newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.aleksej.makaji.listopia:wakelock")
            screenLock.acquire(10)
        }
    }

    fun clearNoInternetConnection(context: Context) {
        clearNotification(context, INTERNET_CONNECTION_ID, GROUP_INTERNET_CONNECTION_ID)
    }

    fun clearShoppingList(context: Context, shoppingListId: String) {
        clearNotification(context, IdParser.parse(shoppingListId), GROUP_SHOPPING_LIST_ID)
    }

    private fun clearNotification(context: Context, notificationId: Int, notificationGroupId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var groupKey: String? = null

        val statusBarNotifications = notificationManager.activeNotifications

        for (statusBarNotification in statusBarNotifications) {
            if (notificationId == statusBarNotification.id) {
                groupKey = statusBarNotification.groupKey
                break
            }
        }

        var counter = 0

        for (statusBarNotification in statusBarNotifications) {
            if (statusBarNotification.groupKey == groupKey) {
                counter++
            }
        }

        if (counter == 2) {
            //if it is last notification in a group, delete group as well
            notificationManager.cancel(notificationGroupId)
        } else {
            notificationManager.cancel(notificationId)
        }
    }

    private object IdParser {
        fun parse(value: String): Int {
            var firstPart = value.split("-")[0]
            if (firstPart.length > 7) firstPart = firstPart.substring(0, 7)
            return Long.parseLong(firstPart.toUpperCase(), 16).toInt()
        }
    }
}