package com.twinkle.train;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.twinkle.train.com.twinkle.java.Spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ToolActivity extends AppCompatActivity {

    ListView listView;
    Toolbar tbr_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);

        listView = (ListView) findViewById(R.id.listview_tool);
        tbr_tool = (Toolbar) findViewById(R.id.tbr_tool);
        setSupportActionBar(tbr_tool);
        tbr_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toollist();

    }

    private void toollist() {

        final String[] strs = new String[]{
                "热水卡", "汽车票", "电脑维修", "智能小华", "每日电影"
        };
        final List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int x = 0; x < strs.length; x++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemsetting", strs[x]);
            mylist.add(map);
        }
        //配置适配器
        SimpleAdapter adapter = new SimpleAdapter(this,
                mylist,//数据源
                R.layout.listview_tool,//显示布局
                new String[]{"itemsetting"}, //数据源的属性字段
                new int[]{R.id.textView_list_tool}); //布局里的控件id
        //添加并且显示
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //校园网
                    case 0:
                        setrsk();
                        break;
                    case 1:
                        setqcp();
                        break;
                    case 2:
                        joinQQGroup("jk47iUs7l-6Nza-HQoPNdiyY6qGkjfQX");
                        break;
                    case 3:
                        Intent intent = new Intent(ToolActivity.this, TuringRobotActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        get_film();
                        break;
                    default:
                        break;

                }
            }
        });
    }

    public void setqcp() {
        Uri uri = Uri.parse("http://www.scqcp.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void setrsk() {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "热水卡充值：\n" + "http://wlkji.top/app/index.php?i=4&t=4");//注意：这里只是分享文本内容
            sendIntent.setType("text/plain");
            startActivityForResult(sendIntent, 1);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "你好像没有安装微信喔", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "您好像没有安装QQ喔", Toast.LENGTH_SHORT).show();
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    private void get_film() {
        try {
            new Spider(ToolActivity.this).get_film();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
