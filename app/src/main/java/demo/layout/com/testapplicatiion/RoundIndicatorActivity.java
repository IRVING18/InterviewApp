package demo.layout.com.testapplicatiion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import demo.layout.com.testapplicatiion.view.RoundIndicatorView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RoundIndicatorActivity extends AppCompatActivity {

    private Handler            mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int num = msg.what;
            mRiv.setCurrentNumAnim(num);

            return false;
        }
    });
    private RoundIndicatorView mRiv;
    int[] arr = {4,50,200,60,90,1,75,93,120};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_indicator);

        mRiv = findViewById(R.id.riv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr.length; i++) {
                    try {
                        mHandler.sendEmptyMessage(arr[i]);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}