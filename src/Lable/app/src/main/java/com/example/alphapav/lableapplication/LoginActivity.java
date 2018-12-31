package com.example.alphapav.lableapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphapav.lableapplication.util.GetData;
import com.example.alphapav.lableapplication.util.SharedHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_account;
    private EditText edit_psw;
    private  Button btn_login;
    private Button btn_register;
    private  CheckBox check_rememberpsw;
    private Context mContext;
    private  String url = "http://10.15.82.223:9090/app_get_data/app_signincheck";
    private SharedHelper sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext= getApplicationContext();
        sh= new SharedHelper(mContext);
        bindViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Map<String,String> data = sh.readRememberUser();
//        edit_account.setText(data.get("username"));
//        edit_psw.setText(data.get("password"));

    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            System.out.println("当前屏幕为横屏");

        }else
        {
            System.out.println("当前屏幕为竖屏");
        }
    }




    private void bindViews(){
        edit_account= (EditText) findViewById(R.id.login_edit_account);
        edit_psw= (EditText) findViewById(R.id.login_edit_psw);
        btn_login= (Button) findViewById(R.id.login_btn_login);
        btn_register= (Button) findViewById(R.id.login_btn_register);
        check_rememberpsw= (CheckBox) findViewById(R.id.login_remember);
        Map<String,String> data = sh.readRememberUser();
        edit_account.setText(data.get("username"));
        edit_psw.setText(data.get("password"));
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                Toast.makeText(mContext,"switch to register activity~ ",Toast.LENGTH_SHORT).show();

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= edit_account.getText().toString();
                String password= edit_psw.getText().toString();
                new Thread() {
                    public void run() {
                        int msg_what=0x006;
                        //begin request
                        try {
                            HashMap<String,String> map = new HashMap<>();

                            String username= edit_account.getText().toString();
                            String password= edit_psw.getText().toString();
                            map.put("username",username);
                            map.put("password",password);

                            String result= GetData.getFormbodyPostData(url, map);

                            JSONObject jsStr= JSONObject.fromObject(result);
                            String str = jsStr.get("msg").toString();
                            System.out.print("\nparse msg: " +  str);
                            if(str.equals("请输入您的用户名!")) msg_what=0x001;
                            else if(str.equals("请输入密码!")) msg_what=0x002;
                            else if(str.equals("请您先注册，再访问我们网站!")) msg_what=0x003;
                            else if(str.equals("密码错误!")) msg_what=0x004;
                            else if(str.equals("登录成功")){
                                String token = jsStr.get("token").toString();
                                System.out.print("\nparse token: " +  token );
                                if(check_rememberpsw.isChecked())
                                {
                                    System.out.println("记住密码");
                                    sh.saveRememberUser(username, password);  //save username, password
                                }
                                sh.saveUser(username, password);  //save username, password
                                sh.saveToken(token);//save token
                                msg_what=0x005;
                            }

                            System.out.print("\nGet response : " + result);

                        }catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(msg_what);
                    }
                }.start();

            }
        });
    }




    //用于刷新界面和跳转页面

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    Toast.makeText(LoginActivity.this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    break;
                case 0x003:
                    Toast.makeText(LoginActivity.this, "请您先注册，再访问我们的网站", Toast.LENGTH_SHORT).show();
                    break;
                case 0x004:
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 0x005:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case 0x006:
                    Toast.makeText(LoginActivity.this, "Wrong response format", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };



}
