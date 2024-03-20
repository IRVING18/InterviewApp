package demo.layout.com.testapplicatiion.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import demo.layout.com.testapplicatiion.R;

/**
 * 有道领世
 * TestApplicatiion
 * Description:
 * Created by wangzheng on 2024/3/20 21:07
 * Copyright @ 2024 网易有道. All rights reserved.
 **/
public class ClientActivity extends AppCompatActivity {
    private IBookInterface mBookInterface = null;
    private int i = 0;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBookInterface = IBookInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidldemo_client);
        Intent intent = new Intent(this, BookLibraryService.class);
        bindService(intent, mConn, BIND_AUTO_CREATE);
        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBookInterface.addBook(new Book("哈哈哈", i++));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        final TextView tv = findViewById(R.id.tv);
        findViewById(R.id.bt_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Book> list = mBookInterface.getBookList();
                    tv.setText(list.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
