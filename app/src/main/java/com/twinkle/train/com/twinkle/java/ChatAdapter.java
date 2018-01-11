package com.twinkle.train.com.twinkle.java;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twinkle.train.R;
import com.twinkle.train.com.twinkle.user.PersonChat;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<PersonChat> lists;

    public ChatAdapter(Context context, List<PersonChat> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    /**
     * 是否是自己发送的消息
     *
     * @author cyf
     */
    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        PersonChat entity = lists.get(position);

        if (entity.isMeSend()) {// 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {// 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        HolderView holderView = null;
        PersonChat entity = lists.get(arg0);
        boolean isMeSend = entity.isMeSend();

        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String admin = preferences.getString("admin", "admin");

        if (holderView == null) {
            holderView = new HolderView();
            if (isMeSend) {
                arg1 = View.inflate(context, R.layout.chat_dialog_right_item,
                        null);
                holderView.tv_chat_me_message = (TextView) arg1
                        .findViewById(R.id.tv_chat_me_message);
                holderView.tv_chat_me_message.setText(entity.getChatMessage());
                holderView.ivw_chat_me = (ImageView) arg1
                        .findViewById(R.id.iv_chat_imagr_right);

                if(!"admin".equals(admin)){
                    Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
                    if(bm!=null){
                        x.image().bind(holderView.ivw_chat_me, Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder().
                                setCircular(true).setUseMemCache(false).setFailureDrawableId(R.drawable.pic).build());
                    }
                    else {
                        holderView.ivw_chat_me.setImageResource(R.drawable.pic);
                    }
                }
                else {
                    holderView.ivw_chat_me.setImageResource(R.drawable.pic);
                }


            } else {
                arg1 = View.inflate(context, R.layout.chat_dialog_left_item,
                        null);
                holderView.tv_chat_robot_message = (TextView) arg1
                        .findViewById(R.id.tv_chat_robot_message);
                holderView.tv_chat_robot_message.setText(entity.getChatMessage());

            }
            arg1.setTag(holderView);
        } else {
            holderView = (HolderView) arg1.getTag();
        }

        return arg1;
    }

    class HolderView {
        TextView tv_chat_me_message;
        ImageView ivw_chat_me;
        TextView tv_chat_robot_message;


    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
