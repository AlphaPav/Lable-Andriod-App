package com.example.alphapav.lableapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.alphapav.lableapplication.util.GetData;

import net.sf.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Context mContext;
    EditText edit_name;
    EditText edit_psw;
    EditText edit_psw_confirm;
    EditText edit_email;

    Button btn_ok;
    Button btn_cancel;
    String url = "http://10.15.82.223:9090/app_get_data/app_register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext= getApplicationContext();
        bindViews();


    }
    private void bindViews(){
        edit_name= (EditText) findViewById(R.id.register_edit_name);
        edit_psw= (EditText) findViewById(R.id.register_edit_psw);
        edit_email= (EditText)findViewById(R.id.register_edit_email);
        edit_psw_confirm= (EditText) findViewById(R.id.register_edit_psd_confirm);
        btn_ok= (Button) findViewById(R.id.register_btn_ok);
        btn_cancel= (Button) findViewById(R.id.register_btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password= edit_psw.getText().toString();
                String password_confirm= edit_psw_confirm.getText().toString();

                if(password.equals(password_confirm))
                {
                    new Thread() {
                        public void run() {
                            int msg_what=0x007;
                            //begin request
                            try {
                                HashMap<String,String> map = new HashMap<>();

                                String username= edit_name.getText().toString();
                                String password= edit_psw.getText().toString();
                                String email  = edit_email.getText().toString();
                                map.put("username",username);
                                map.put("password",password);
                                map.put("email",email);

                                String result= GetData.getFormbodyPostData(url, map);
                                if(result!=null)
                                {
                                    JSONObject jsStr= JSONObject.fromObject(result);
                                    String str = jsStr.get("msg").toString();
                                    System.out.print("parse:" +  str);
                                    if(str.equals("请输入用户名")) msg_what=0x001;
                                    else if(str.equals("用户名已注册")) msg_what=0x002;
                                    else if(str.equals("请输入邮箱")) msg_what=0x003;
                                    else if(str.equals("请输入密码")) msg_what=0x004;
                                    else if(str.equals("注册失败")) msg_what=0x005;
                                    else if(str.equals("注册成功")) msg_what=0x006;
                                }


                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(msg_what);
                        }
                    }.start();

                }else
                {
                    Toast.makeText(mContext,"两次密码输入不同，请再确认！",Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                Toast.makeText(mContext,"switch to login activity~ ",Toast.LENGTH_SHORT).show();
                RegisterActivity.this.finish();
                System.out.println("RegisterActivity.this.finish();");
            }
        });

    }

    //用于刷新界面和跳转页面

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    Toast.makeText(RegisterActivity.this, "用户名已注册", Toast.LENGTH_SHORT).show();
                    break;
                case 0x003:
                    Toast.makeText(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                    break;
                case 0x004:
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                case 0x005:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case 0x006:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                    RegisterActivity.this.finish();
//                    System.out.println("RegisterActivity.this.finish();");
                    break;
                case 0x007:
                    Toast.makeText(RegisterActivity.this, "Wrong response format", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };




}
