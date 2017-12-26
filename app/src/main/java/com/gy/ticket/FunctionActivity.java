package com.gy.ticket;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gy.ticket.adapter.FunctionAdapter;
import com.gy.ticket.java.InitString;
import com.loopj.android.image.SmartImageView;

public class FunctionActivity extends Fragment {

    ListView lv_function;
    View view;
    FunctionAdapter functionAdapter;
    ImageView iv_function_account;

    public FunctionActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_function, container, false);
        init();
        apply();
        return view;
    }


    private void init() {
        lv_function = (ListView) view.findViewById(R.id.lv_function);
        iv_function_account = (ImageView) view.findViewById(R.id.iv_function_account);
        iv_function_account.setOnClickListener(new Click());
    }

    private void apply() {

        functionAdapter = new FunctionAdapter(getActivity());
        lv_function.setAdapter(functionAdapter);
        lv_function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        startActivity(intent = new Intent(getActivity(), TravelActivity.class));
                        break;
                    case 1:
                        startActivity(intent = new Intent(getActivity(), AmusementActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private class Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_function_account:
                    startActivity(new Intent(getActivity(), InformationActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}
