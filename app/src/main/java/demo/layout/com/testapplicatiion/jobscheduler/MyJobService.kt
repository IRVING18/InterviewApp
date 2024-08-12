package demo.layout.com.testapplicatiion.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/12 11:39
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyJobService : JobService() {
    val MESSAGE_ID: Int = 100

    private val mJobHandler: Handler = Handler(object : Handler.Callback{
        override fun handleMessage(msg: Message): Boolean {
            Log.e("wzzzzzzzzzzz", "handle message!!!!")
            //请注意，我们手动调用了jobFinished方法。
            //当onStartJob返回true的时候，我们必须在合适时机手动调用jobFinished方法
            //否则该应用中的其他job将不会被执行
            jobFinished(msg.obj as JobParameters, false)
            //第一个参数JobParameter来自于onStartJob(JobParameters params)中的params，
            // 这也说明了如果我们想要在onStartJob中执行异步操作，必须要保存下来这个JobParameter。
            return true
        }
    })


    override fun onStartJob(params: JobParameters?): Boolean {
        Log.e("wzzzzzzzzzzz", "onStartJob");
        // 注意到我们在使用Hanlder的时候把传进来的JobParameters保存下来了
        mJobHandler.sendMessage(Message.obtain(mJobHandler, MESSAGE_ID, params));

        // 返回false说明job已经完成  不是个耗时的任务
        // 返回true说明job在异步执行  需要手动调用jobFinished告诉系统job完成
        // 这里我们返回了true,因为我们要做耗时操作。
        // 返回true意味着耗时操作花费的事件比onStartJob执行的事件更长
        // 并且意味着我们会手动的调用jobFinished方法
        return true;
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.e("wzzzzzzzzzzz", "onStopJob");
        mJobHandler.removeMessages(MESSAGE_ID);

        // 当系统收到一个cancel job的请求时，并且这个job仍然在执行(onStartJob返回true)，系统就会调用onStopJob方法。
        // 但不管是否调用onStopJob，系统只要收到取消请求，都会取消该job

        // true 需要重试
        // false 不再重试 丢弃job
        return false;
    }
}