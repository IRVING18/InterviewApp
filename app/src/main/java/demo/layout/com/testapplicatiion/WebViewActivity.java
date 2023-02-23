package demo.layout.com.testapplicatiion;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.webkit.WebView;

import androidx.annotation.Nullable;

/**
 * Created by wangzheng on 2022/8/25 2:35 下午.
 * E-mail : ivring11@163.com
 **/
class WebViewActivity extends Activity {

    private WebView mMWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mMWebView = findViewById(R.id.webView);

        Intent intent = new Intent();
        intent.setAction("demo.layout.com.testapplicatiion.webtest");
        intent.setPackage("demo.layout.com.testapplicatiion");
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IWebServicelInterface iWebServicelInterface = IWebServicelInterface.Stub.asInterface(service);
            if (iWebServicelInterface != null) {
                try {
                    String url = iWebServicelInterface.getUrl();
                    mMWebView.loadUrl(url);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
        System.exit(0);
    }
}
