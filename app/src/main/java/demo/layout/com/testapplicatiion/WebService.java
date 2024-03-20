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
public class WebService extends Service {

    private final IWebServicelInterface.Stub mIBinder = new IWebServicelInterface.Stub() {
        @Override
        public String getUrl() throws RemoteException {
            return "https://www.163.com";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }
}
