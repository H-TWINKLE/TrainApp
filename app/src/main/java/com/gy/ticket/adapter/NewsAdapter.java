package com.gy.ticket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gy.ticket.R;
import com.gy.ticket.user.Film;
import com.gy.ticket.user.Play;
import com.gy.ticket.user.Sing;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TWINKLE on 2017/12/25.
 */

public  class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<Film> list = new ArrayList<>();
    private List<Sing> list2 = new ArrayList<>();
    private List<Play> list3 = new ArrayList<>();


    public NewsAdapter(Context context, List<Film> list, List<Sing> list2,List<Play> list3) {
        this.context = context;
        this.list = list;
        this.list2 = list2;
        this.list3 = list3;

    }

    @Override
    public int getCount() {
        return list.size()+list2.get(0).getSuggest().size()+list3.get(0).getSuggest().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.content_lv_news,null);
        SmartImageView smartImageView = (SmartImageView)view.findViewById(R.id.siv_icon);
        TextView title = (TextView)view.findViewById(R.id.tv_news_item_title);
        TextView content = (TextView)view.findViewById(R.id.tv_news_item_content);
        TextView nature = (TextView)view.findViewById(R.id.tv_news_item_nature);
        if(list.size()>position){
            smartImageView.setImageUrl(list.get(position).getImg());
            title.setText(list.get(position).getName());
            content.setText(list.get(position).getInfo());
            nature.setText(list.get(position).getDate());
        }
       else if(position<(list.size()+list2.size())){
            smartImageView.setImageUrl("https://pimg.damai.cn/perform/project/"+list2.get(0).getSuggest().get(position-list.size()).getFold()
                            +"/"+list2.get(0).getSuggest().get(position-list.size()).getProjectId()+"_n.jpg");
            title.setText(list2.get(0).getSuggest().get(position-list.size()).getProjectName());
            content.setText(list2.get(0).getSuggest().get(position-list.size()).getVenue());
            nature.setText(list2.get(0).getSuggest().get(position-list.size()).getShowTime());
        }else{
            smartImageView.setImageUrl("https://pimg.damai.cn/perform/project/"+list3.get(0).getSuggest().get(position-list.size()-list2.size()).getFold()
                    +"/"+list3.get(0).getSuggest().get(position-list.size()-list2.size()).getProjectId()+"_n.jpg");
            title.setText(list3.get(0).getSuggest().get(position-list.size()-list2.size()).getProjectName());
            content.setText(list3.get(0).getSuggest().get(position-list.size()-list2.size()).getVenue());
            nature.setText(list3.get(0).getSuggest().get(position-list.size()-list2.size()).getShowTime());
        }



        return view;
    }
}
