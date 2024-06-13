package demo.layout.com.testapplicatiion;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import demo.layout.com.testapplicatiion.base.BaseActivity;


public class Activity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager     fm        = getFragmentManager();
        FragmentTransaction ft        = fm.beginTransaction();
        Fragment2           fragment2 = new Fragment2();
        ft.add(R.id.container, fragment2);
        ft.commit();

    }
}