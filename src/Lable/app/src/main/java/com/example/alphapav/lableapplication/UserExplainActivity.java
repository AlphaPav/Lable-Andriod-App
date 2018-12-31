package com.example.alphapav.lableapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.alphapav.lableapplication.util.SharedHelper;

import java.util.Map;
import java.util.Objects;

public class UserExplainActivity extends AppCompatActivity {
    private RadioGroup radioGroup_font;
    private Context mContext;
    private SharedHelper sh;
    private int SMALL_FONTSIZE  =15;
    private int MIDDLE_FONTSIZE  =18;
    private int LARGE_FONTSIZE  =20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_explain);
        mContext= getApplicationContext();
        sh= new SharedHelper(mContext);
        bindViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int temp_fontsize= sh.readFontsize();
        System.out.println("read from sh font size: "+temp_fontsize);
        if(temp_fontsize==SMALL_FONTSIZE)
        {
            RadioButton btn= (RadioButton)findViewById(R.id.radiobtn_fontsmall);
            btn.setChecked(true);
        }else if (temp_fontsize==MIDDLE_FONTSIZE)
        {
            RadioButton btn= (RadioButton)findViewById(R.id.radiobtn_fontmiddle);
            btn.setChecked(true);
        }else if (temp_fontsize==LARGE_FONTSIZE)
        {
            RadioButton btn= (RadioButton)findViewById(R.id.radiobtn_fontlarge);
            btn.setChecked(true);
        }else {
            System.out.println("temp sh font size error");
        }

    }
    private  void bindViews()
    {
        radioGroup_font= (RadioGroup)findViewById(R.id.radiogroup_fontsize);
        radioGroup_font.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
    }
    private class OnCheckedChangeListenerImpl implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempbutton= (RadioButton)findViewById(checkedId);
//            System.out.println(checkedId);
            System.out.println(tempbutton.getText().toString());
            if(tempbutton.getText().toString().equals("小"))
            {
                sh.saveFontsize(SMALL_FONTSIZE);

            }else if(tempbutton.getText().toString().equals("中"))
            {
                sh.saveFontsize(MIDDLE_FONTSIZE);
            }else if(tempbutton.getText().toString().equals("大"))
            {
                sh.saveFontsize(LARGE_FONTSIZE);
            }
            else {
                System.out.println("tempbutton text error");
            }
        }
    }



}
