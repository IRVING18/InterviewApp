package demo.layout.com.testapplicatiion.datastore.multiProcess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/8/19 19:26
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
class DataStoreService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            var i = 0
            while (true) {
                withContext(Dispatchers.IO) {
                    MultiDSHelper.dataStore.updateData { myProtoBean ->
                        myProtoBean.toBuilder()
                            .setName("哈哈哈哈${i++}")
                            .build()
                    }
                    delay(2000)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}