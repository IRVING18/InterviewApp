package demo.layout.com.testapplicatiion.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import demo.layout.com.testapplicatiion.R
import demo.layout.com.testapplicatiion.base.BaseActivity


/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/12 11:50
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class JobActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        findViewById<View>(R.id.bt_start).setOnClickListener {

            val JOB_ID = 100001
            //Builder构造方法接收两个参数，第一个参数是jobId，每个app或者说uid下不同的Job,它的jobId必须是不同的
            //第二个参数是我们自定义的JobService,系统会回调我们自定义的JobService中的onStartJob和onStopJob方法
            val builder = JobInfo.Builder(JOB_ID, ComponentName(this, MyJobService::class.java))
                .setMinimumLatency(5000)// 任务最少延迟时间，5s后执行
                .setOverrideDeadline(60000)// 最晚10s后执行，任务deadline，当到期没达到指定条件也会开始执行
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)// 网络条件，默认值NETWORK_TYPE_NONE
                .setRequiresCharging(true)// 是否充电
                .setRequiresDeviceIdle(false)// 设备是否空闲
                .setPersisted(true) //设备重启后是否继续执行
                .setBackoffCriteria(3000, JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
            val mJobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val result = mJobScheduler.schedule(builder.build())
            if (result <= 0) {
                Log.e("wzzzzzzzzzzzzz", "result is $result Schedule failed")
            }
        }
    }
}