package com.gy.ticket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class WelActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    private ViewPager vp_index;
    private List<Fragment> list_frag;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_mess:
                    vp_index.setCurrentItem(0, false);
                    return true;
                case R.id.navigation_func:
                    vp_index.setCurrentItem(1, false);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);
        init();
    }

    private void init() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        vp_index = (ViewPager) findViewById(R.id.vp_index);
        vp_index.addOnPageChangeListener(this);
        set_fragment();
    }

    private void set_fragment() {
        list_frag = new ArrayList<>();
        list_frag.add(new NewsActivity());
        list_frag.add(new FunctionActivity());
        vp_index.setAdapter(new WelActivity.FragAdapter(getSupportFragmentManager()));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                navigation.setSelectedItemId(R.id.navigation_mess);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.navigation_func);
                break;
            default:
                navigation.setSelectedItemId(R.id.navigation_mess);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class FragAdapter extends FragmentPagerAdapter {
        private FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list_frag.get(position);
        }

        @Override
        public int getCount() {
            return list_frag.size();
        }
    }

}
