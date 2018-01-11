package com.twinkle.train;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twinkle.train.com.twinkle.java.ChatAdapter;
import com.twinkle.train.com.twinkle.user.PersonChat;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class TuringRobotActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private Toolbar tbr_robot;
    /**
     * 声明ListView
     */
    private ListView lv_chat_dialog;

    String url = "http://www.tuling123.com/openapi/api";
    String key = "057c818d3b1b430faa4ee25b638a5b7c";
    String result;
    /**
     * 集合
     */
    private List<PersonChat> personChats = new ArrayList<>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /**
                     * ListView条目控制在最后一行
                     */
                    lv_chat_dialog.setSelection(personChats.size());
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_turing_robot);
        /**
         * 虚拟4条发送方的消息
         */

        tbr_robot = (Toolbar)findViewById(R.id.tbr_robot);
        setSupportActionBar(tbr_robot);
        tbr_robot.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PersonChat personChat = new PersonChat();
        personChat.setMeSend(false);
        personChat.setChatMessage(getString(R.string.first_message));
        personChats.add(personChat);

        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        Button btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        /**
         *setAdapter
         */
        chatAdapter = new ChatAdapter(this, personChats);
        lv_chat_dialog.setAdapter(chatAdapter);
        /**
         * 发送按钮的点击事件
         */
        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
                    Toast.makeText(TuringRobotActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonChat personChat = new PersonChat();
                //代表自己发送
                personChat.setMeSend(true);
                //得到发送内容
                personChat.setChatMessage(et_chat_message.getText().toString());
                //加入集合
                personChats.add(personChat);

                Connect_Turing(et_chat_message.getText().toString());
                //清空输入框
                et_chat_message.setText("");
                //刷新ListView
                chatAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
            }
        });
    }

    public void Connect_Turing(String info) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(10000);
        RequestParams params = new RequestParams();
        params.put("key", key);
        params.put("info", info);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                if (i == 200) {
                    try {
                        result = new String(bytes, "utf-8");
                        System.out.println("输出Turing结果：" + result);
                        JSONObject object = JSON.parseObject(result);         //处理信息
                        PersonChat personChat = new PersonChat();
                        personChat.setMeSend(false);
                        personChat.setChatMessage(object.getString("text"));
                        personChats.add(personChat);
                        chatAdapter.notifyDataSetChanged();
                        handler.sendEmptyMessage(1);

                    } catch (Exception e) {

                        Toast.makeText(TuringRobotActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(TuringRobotActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(TuringRobotActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
