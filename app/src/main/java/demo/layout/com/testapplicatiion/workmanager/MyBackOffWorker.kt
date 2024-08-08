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
class MyBackOffWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.e("wzzzzzzzzzzzz", "worker Start")
        //模拟处理任务耗时
        delay(5000)
        Log.e("wzzzzzzzzzzzz", "worker retry")
        //返回重试
        return Result.retry()
    }
}