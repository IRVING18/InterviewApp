package demo.layout.com.testapplicatiion;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;


/**
 * Created by wangzheng on 8/17/21 8:19 PM.
 * E-mail : ivring11@163.com
 **/
public class BaseFragment extends Fragment {
    private static final String TAG = "wzzzzzzzzzz Fragment";

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        Log.e(TAG,"  onInflate  "+this.getClass().getSimpleName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"  onAttach  "+this.getClass().getSimpleName());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"  onCreate  "+this.getClass().getSimpleName());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Log.e(TAG,"  onCreateView  "+this.getClass().getSimpleName());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG,"  onViewCreated  "+this.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG,"  onActivityCreated  "+this.getClass().getSimpleName());

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"  onStart  "+this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"  onResume  "+this.getClass().getSimpleName());

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"  onPause  "+this.getClass().getSimpleName());

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"  onStop  "+this.getClass().getSimpleName());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG,"  onDestroyView  "+this.getClass().getSimpleName());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"  onDestroy  "+this.getClass().getSimpleName());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"  onDetach  "+this.getClass().getSimpleName());

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"  onHiddenChanged  "+hidden+"   "+this.getClass().getSimpleName());

    }

}
