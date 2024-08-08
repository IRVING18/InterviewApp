package demo.layout.com.testapplicatiion.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ProgressWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        //回调进度
        setProgress(workDataOf("process" to 0))
        delay(500)
        setProgress(workDataOf("process" to 50))
        delay(500)
        setProgress(workDataOf("process" to 100))
        return Result.success()
    }
}