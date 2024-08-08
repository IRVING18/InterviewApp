package demo.layout.com.testapplicatiion.workmanager

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import demo.layout.com.testapplicatiion.R
import kotlinx.coroutines.delay

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/8 11:34
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class MyWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    /**
     * Create ForegroundInfo required to run a Worker in a foreground service.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForegroundInfo(): ForegroundInfo {
        // For a real world app you might want to use a different id for each Notification.
        val notificationId = 1
        return ForegroundInfo(notificationId, createNotification())
    }

    /**
     * Create the notification and required channel (O+) for running work in a foreground service.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val channelId = "channelId"
        val title = "title"
        val cancel = "cancel"
        val name = "channelName"
        // This PendingIntent can be used to cancel the Worker.
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        val builder = Notification.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
            .setOngoing(true)
            .addAction(R.drawable.abc_vector_test, cancel, intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, name).also {
                builder.setChannelId(it.id)
            }
        }
        return builder.build()
    }

    /**
     * Create the required notification channel for O+ devices.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            channelId, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }


    override suspend fun doWork(): Result {
        Log.e("wzzzzzzzzzzzz", "worker Start")
        val inputUrl = inputData.getString("url")
        Log.e("wzzzzzzzzzzzz", "worker 入参 $inputUrl")
        //模拟处理任务耗时
        delay(5000)

        Log.e("wzzzzzzzzzzzz", "worker End")
        //返回成功
        return Result.success()
    }
}