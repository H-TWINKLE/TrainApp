package com.twinkle.train;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.twinkle.train.com.twinkle.java.Jwgl_Dialog;
import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.Util;
import com.yalantis.phoenix.PullToRefreshView;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Jwgl_ScoreActivity extends AppCompatActivity implements View.OnClickListener {


    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ImageView ivw_jwgl_score_fsh, ivw_jwgl_score_login;
    String admin, stu_id, jwgl_pass;
    SharedPreferences preferences;
    Jwgl_Dialog jwgl_dialog;
    PullToRefreshView ptr_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwgl__score);

        init();


    }

    public void init() {


        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        stu_id = preferences.getString("stu_id", "学号");
        jwgl_pass = preferences.getString("jwgl_pass", "教务管理系统密码");

        ivw_jwgl_score_login = (ImageView) findViewById(R.id.ivw_jwgl_score_login);
        ivw_jwgl_score_login.setOnClickListener(this);

        ivw_jwgl_score_fsh = (ImageView) findViewById(R.id.ivw_jwgl_score_fsh);
        ivw_jwgl_score_fsh.setOnClickListener(this);

        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
        if(bm!=null){
            ViewGroup.LayoutParams lp = ivw_jwgl_score_login.getLayoutParams();
            lp.width = 60;
            lp.height = 60;
            ivw_jwgl_score_login.setLayoutParams(lp);
            x.image().bind(ivw_jwgl_score_login,Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build());
        }
        else {
            ivw_jwgl_score_login.setImageResource(R.drawable.pic);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        ptr_score = (PullToRefreshView) findViewById(R.id.ptr_score);
        ptr_score.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptr_score.setRefreshing(false);
                jwgl_login_Dialog();

            }
        });
    }


    public void jwgl_login_Dialog() {


        jwgl_dialog = new Jwgl_Dialog(Jwgl_ScoreActivity.this, stu_id, jwgl_pass, new Jwgl_Dialog.OnDismissListener() {
            @Override
            public void onDismissListener() {
                finish();
            }
        });
        jwgl_dialog.setCancelable(false);
        jwgl_dialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivw_jwgl_score_login:
                Intent intent = new Intent(Jwgl_ScoreActivity.this, AdminInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ivw_jwgl_score_fsh:
                finish();
                break;
            default:
                break;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        SimpleAdapter setAdapter;
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        TabLayout tlt_score;
        MyApp myApp;
        DbManager db;
        int count;
        ListView lvw_score;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_jwgl__score, container, false);
            lvw_score = (ListView) rootView.findViewById(R.id.lvw_jwgl_score);
            tlt_score = (TabLayout) getActivity().findViewById(R.id.tlt_score);
            tlt_score.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            load_data("xuenian");
                            break;
                        case 1:
                            load_data("xueqi");
                            break;
                        case 2:
                            load_data("mingcheng");
                            break;
                        case 3:
                            load_data("xingzhi");
                            break;
                        case 4:
                            load_data("xuefen");
                            break;
                        case 5:
                            load_data("jidian");
                            break;
                        case 6:
                            load_data("chengji");
                            break;
                        case 7:
                            load_data("xueyuan");
                            break;
                        case 8:
                            load_data("chongxiu");
                            break;
                        default:
                            break;
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            lvw_score = (ListView) rootView.findViewById(R.id.lvw_jwgl_score);
            load_data("xuenian");
            return rootView;
        }

        public void load_data(String string) {
            try {

                list.clear();
                myApp = new MyApp();
                db = x.getDb(myApp.daoConfig);
                //  List<Jwgl_exam> info = db.selector(Jwgl_exam.class).where("type", "=", "info").findAll();
                List<DbModel> info = db.findDbModelAll(new SqlInfo("select " + string + " from jwgl_score where type = 'info'"));
                count = info.size();

                for (int x = 0; x < count; x++) {
                    map = new HashMap<>();
                    map.put(string, info.get(x).getString(string));
                    list.add(map);
                }
                //    db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setAdapter = new SimpleAdapter(getActivity(), list
                    ,//数据源
                    R.layout.listview_base,//显示布局
                    new String[]{string}, //数据源的属性字段
                    new int[]{R.id.tvw_base}); //布局里的控件id
            //添加并且显示
            lvw_score.setAdapter(setAdapter);
            setAdapter.notifyDataSetChanged();
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "学年";
                case 1:
                    return "学期";
                case 2:
                    return "课程名称";
                case 3:
                    return "课程性质";
                case 4:
                    return "学分";
                case 5:
                    return "绩点";
                case 6:
                    return "成绩";
                case 7:
                    return "学院名称";
                case 8:
                    return "重修标记";

            }
            return null;
        }
    }


}