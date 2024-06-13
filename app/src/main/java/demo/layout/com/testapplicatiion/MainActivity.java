package demo.layout.com.testapplicatiion;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageView;

import demo.layout.com.testapplicatiion.base.BaseActivity;
import demo.layout.com.testapplicatiion.view.window.YouthWindowManager;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager     fm        = getFragmentManager();
        FragmentTransaction ft        = fm.beginTransaction();
        Fragment1           fragment1 = new Fragment1();
        ft.add(R.id.container, fragment1);
        ft.commit();

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.abc_vector_test);
        iv.setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary));
        YouthWindowManager.getInstance().addFloatWindowView(this, iv, false, null,100);
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.abc_vector_test);
        iv1.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
        YouthWindowManager.getInstance().addFloatWindowView(this, iv1, false, null,150);

    }
}