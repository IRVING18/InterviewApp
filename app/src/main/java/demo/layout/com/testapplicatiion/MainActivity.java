package demo.layout.com.testapplicatiion;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;



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
    }
}