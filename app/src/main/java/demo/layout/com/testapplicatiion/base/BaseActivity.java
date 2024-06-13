package demo.layout.com.testapplicatiion.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by wangzheng on 8/10/21 5:56 PM.
 * E-mail : ivring11@163.com
 **/
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "wzzzzzzzzzz Activity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"  onCreate1  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"  onstart  "+this.getClass().getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.e(TAG,"  onCreate2  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"  onResume  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"  onPause  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"  onStop  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"  onRestart  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"  onDestroy  "+this.getClass().getSimpleName());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"  onNewIntent  "+this.getClass().getSimpleName());

    }
}
