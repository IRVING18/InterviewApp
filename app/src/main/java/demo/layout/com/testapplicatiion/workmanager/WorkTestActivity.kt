package demo.layout.com.testapplicatiion.workmanager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.work.ArrayCreatingInputMerger
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity
import java.util.concurrent.TimeUnit

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/8 11:41
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class WorkTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_test)
        findViewById<View>(R.id.bt_one).setOnClickListener {
            //定义Worker一次性请求
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
            //发送一次性任务
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_period).setOnClickListener {
            //定义Worker重复任务请求，每15分钟一次，【这也是重复任务的最小间隔时间，不能小于15分钟的间隔】
//            val workRequest = PeriodicWorkRequest.Builder(
//                MyWorker::class.java,
//                15, TimeUnit.MINUTES //每15分钟重复执行一次
//            ).build()
//            WorkManager.getInstance(this).enqueue(workRequest)

            //重复任务，每1小时执行一次，执行开始时间可在前15分钟任意时间开始；
            val workRequest: WorkRequest = PeriodicWorkRequest.Builder(
                MyWorker::class.java,
                15, TimeUnit.MINUTES,//每1小时执行一次
                5, TimeUnit.MINUTES//每次重复在前15分钟内开始均可；
            ).build()
            WorkManager.getInstance(this).enqueue(workRequest)
        }
        findViewById<View>(R.id.bt_expedited).setOnClickListener {
            //配置加急
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                //配额策略：含义就是配额不够的时候也会优先执行
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
            //执行加急任务
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_constraints).setOnClickListener {
            //配置约束
            val constraints = Constraints.Builder()
                //1、设备空闲状态时运行
                .setRequiresDeviceIdle(true)
                //2、特定的网络状态运行
                //  NOT_REQUIRED	不需要网络
                //  CONNECTED	任何可用网络
                //  UNMETERED	需要不计量网络，如WiFi
                //  NOT_ROAMING	需要非漫游网络
                //  METERED	    需要计量网络，如4G
                .setRequiredNetworkType(NetworkType.CONNECTED)
                //3、电量充足时运行
                .setRequiresBatteryNotLow(true)
                //4、充电时执行
                .setRequiresCharging(true)
                //5、存储空间足够时运行
                .setRequiresStorageNotLow(true)
                //6、指定是否在(Uri指定的)内容更新时执行本次任务
//                .addContentUriTrigger(Uri.EMPTY, true)
                .build()
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_delay).setOnClickListener {
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                //延迟5s后执行
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_set_tag).setOnClickListener {
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                //延迟10s后执行
                .setInitialDelay(10L, TimeUnit.SECONDS)
                .addTag("tag1")
                .build()
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_cancel_tag).setOnClickListener {
            //取消一组带有相同标签的任务
            WorkManager.getInstance(this).cancelAllWorkByTag("tag1")
        }
        findViewById<View>(R.id.bt_backoff).setOnClickListener {
            //当返回Retry()时，每隔5s重试一次
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyBackOffWorker>()
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR, //线性策略，即固定时间间隔
                    OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS, //延迟5s后再重试
                    TimeUnit.SECONDS
                )
                .build()
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_inputdata).setOnClickListener {
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                //添加入参
                .setInputData(
                    workDataOf(
                        "url" to "https://www.baidu.com"
                    )
                )
                .build()
            WorkManager.getInstance(this).enqueue(myWorkRequest)
        }
        findViewById<View>(R.id.bt_call_process).setOnClickListener {
            val myProgressWorkRequest = OneTimeWorkRequestBuilder<ProgressWorker>()
                .build()
            WorkManager.getInstance(this).enqueueUniqueWork(
                "唯一name",
                ExistingWorkPolicy.KEEP,
                myProgressWorkRequest
            )
        }
        findViewById<View>(R.id.bt_cancel_all).setOnClickListener {
            WorkManager.getInstance(this).cancelAllWork()
        }
        findViewById<View>(R.id.bt_link).setOnClickListener {
            val worker1 = OneTimeWorkRequestBuilder<MyLinkWorker1>().build()
            val worker2 = OneTimeWorkRequestBuilder<MyLinkWorker2>().build()
            val worker3 = OneTimeWorkRequestBuilder<MyLinkWorker3>().build()
            WorkManager.getInstance(this)
                .beginWith(listOf(worker1,worker2))//1、2一起执行
                .then(worker3)//然后执行3
                .enqueue()
        }
        findViewById<View>(R.id.bt_link2).setOnClickListener {
            val worker1 = OneTimeWorkRequestBuilder<MyLinkWorker1>().build()
            val worker2 = OneTimeWorkRequestBuilder<MyLinkWorker2>().build()
            val worker3 = OneTimeWorkRequestBuilder<MyLinkWorker3>()
                .setInputMerger(ArrayCreatingInputMerger::class.java)
                .build()
            WorkManager.getInstance(this)
                .beginWith(listOf(worker1,worker2))//1、2一起执行
                .then(worker3)//然后执行3
                .enqueue()
        }

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("唯一name")
            .observe(this) { list: List<WorkInfo?>? ->
                list?.forEach { workInfo ->
                    Log.e("wzzzzzzzzzz", "监听数据：${workInfo?.progress}")
                }
            }
    }
}