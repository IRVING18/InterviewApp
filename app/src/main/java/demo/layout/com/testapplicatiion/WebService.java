package demo.layout.com.testapplicatiion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

/**
 * Created by wangzheng on 2022/8/25 2:28 下午.
 * E-mail : ivring11@163.com
 **/
class WebService extends Service {

    private final IWebServicelInterface.Stub mStub = new IWebServicelInterface.Stub() {
        @Override
        public String getUrl() throws RemoteException {
            return "http://www.baidu.com";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }
}
