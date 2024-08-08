package demo.layout.com.testapplicatiion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import demo.layout.com.testapplicatiion.aidldemo.ClientActivity;
import demo.layout.com.testapplicatiion.base.BaseFragment;
import demo.layout.com.testapplicatiion.boardcastTest.BroadCastTestActivity;
import demo.layout.com.testapplicatiion.servicetest.ServiceTestActivity;
import demo.layout.com.testapplicatiion.workmanager.WorkTestActivity;


/**
 * Created by wangzheng on 8/17/21 8:26 PM.
 * E-mail : ivring11@163.com
 **/
public class Fragment1 extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_1, null);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoundIndicatorActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClientActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceTestActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BroadCastTestActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.bt7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkTestActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
