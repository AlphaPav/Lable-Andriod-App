package com.example.alphapav.lableapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphapav.lableapplication.History_Entity;
import com.example.alphapav.lableapplication.History_Relation;
import com.example.alphapav.lableapplication.LoginActivity;
import com.example.alphapav.lableapplication.R;
import com.example.alphapav.lableapplication.UserExplainActivity;
import com.example.alphapav.lableapplication.base.BaseFragment;
import com.example.alphapav.lableapplication.util.GetData;
import com.example.alphapav.lableapplication.util.SharedHelper;
import com.example.alphapav.lableapplication.util.TimeUtils;

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Objects;

public class UsercenterFragment extends BaseFragment {
    private String logout_url = "http://10.15.82.223:9090/app_get_data/app_logout";
    View view;
    private SharedHelper sh;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usercenter, container, false);

        mContext= Objects.requireNonNull(getActivity()).getApplicationContext();
        sh= new SharedHelper(mContext);
        try {
            bindViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view ;
    }


    public static UsercenterFragment newInstance() {
        return new UsercenterFragment();
    }

    //用于刷新界面和跳转页面
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    Toast.makeText(getActivity(), "登出成功", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"switch to register activity~ ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                case 0x002:
                    Toast.makeText(getActivity(), "尚未登陆", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext, LoginActivity.class));
                case 0x003:
                    Toast.makeText(getActivity(),"Wrong response format", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };


    private void bindViews() throws ParseException {
        Button btn_logout = (Button) view.findViewById(R.id.usercenter_btn_logout);
        Button btn_relation_history = (Button) view.findViewById(R.id.usercenter_btn_history_relation);
        Button btn_entity_history = (Button) view.findViewById(R.id.usercenter_btn_history_entity);
        Button btn_explain = (Button)view.findViewById(R.id.usercenter_btn_explain);

        TextView text_usercenter_name=(TextView)view.findViewById(R.id.usercenter_name);
        text_usercenter_name.setText(" "+sh.readUser().get("username")+" ");
        TextView text_usercenter_time=(TextView)view.findViewById(R.id.usercenter_time);
        long diff = TimeUtils.getMinuteDiff(TimeUtils.getCurrentTime(), sh.readLoginTime());
        text_usercenter_time.setText(" 已登陆"+diff+"分钟 ");
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int msg_what=0x003;
                        //begin request
                        try {
                            HashMap<String,String> map = new HashMap<>();
                            String token= sh.readToken();
                            map.put("token",token);
                            String result= GetData.getFormbodyPostData(logout_url, map);
                            if(result!=null)
                            {
                                JSONObject jsStr= JSONObject.fromObject(result);
                                String str = jsStr.get("msg").toString();
                                //System.out.print("\nparse msg: " +  str);
                                if(str.equals("尚未登录")) msg_what=0x002;
                                else if(str.equals("登出成功")){
                                    msg_what=0x001;
                                }
                                //System.out.print("\nGet response : " + result);
                            }

                        }catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(msg_what);
                    }
                }.start();

            }
        });

        btn_relation_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, History_Relation.class));
            }
        });

        btn_entity_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, History_Entity.class));
            }
        });
        btn_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, UserExplainActivity.class));
            }
        });
    }
}
