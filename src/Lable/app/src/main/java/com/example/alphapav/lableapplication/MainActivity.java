package com.example.alphapav.lableapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.alphapav.lableapplication.base.BaseActivity;
import com.example.alphapav.lableapplication.fragment.LableentityFragment;
import com.example.alphapav.lableapplication.fragment.LablerelationFragment;
import com.example.alphapav.lableapplication.fragment.UsercenterFragment;
import com.example.alphapav.lableapplication.util.ActivityUtils;
import com.example.alphapav.lableapplication.util.SharedHelper;
import com.example.alphapav.lableapplication.util.TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Fragment> mFragmentList;
    private Fragment mCurrentFragment;
    private Toolbar mToolbar;
    private SharedHelper sh;
    private Context mContext;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_labelentity:
                    try {
                        switchFragment(mCurrentFragment, mFragmentList.get(0), item.getTitle());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return true;
                case R.id.navigation_labelrelation:
                    try {
                        switchFragment(mCurrentFragment, mFragmentList.get(1), item.getTitle());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return true;
                case R.id.navigation_usercenter:
//                    sh.recordLastType("");
                    try {
                        switchFragment(mCurrentFragment, mFragmentList.get(2), item.getTitle());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LOGTAG"," Mainactivity onCreate(Bundle savedInstanceState)");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.title_labelentity));

        mContext= getApplicationContext();
        sh= new SharedHelper(mContext);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(LableentityFragment.newInstance());
        mFragmentList.add(LablerelationFragment.newInstance());
        mFragmentList.add(UsercenterFragment.newInstance());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(sh.getLastType().equals("entity") || sh.getLastType().equals("relation")){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("上次退出后您还有未完成的工作，是否继续？")
                    .setNegativeButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(sh.getLastType().equals("relation")){
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(1), R.id.fragment);
                                mCurrentFragment = mFragmentList.get(1);
                                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                                navigation.setSelectedItemId(R.id.navigation_labelrelation);

                            }else{
                                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fragment);
                                mCurrentFragment = mFragmentList.get(0);
                            }
                        }
                    })
                    .setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fragment);
                            sh.recordLastType("");
                            mCurrentFragment = mFragmentList.get(0);
                        }
                    })
                    .create();
            dialog.show();
        }
        else{
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fragment);
            mCurrentFragment = mFragmentList.get(0);
        }


    }

    public void switchFragment(Fragment from, Fragment to, CharSequence title) throws ParseException {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().
                    beginTransaction();
            mToolbar.setTitle(title);
            if (!to.isAdded()) {
                Log.i("LOGCAT","to is added not true");
                transaction.hide(from).add(R.id.fragment, to).commit();

            } else {
                TextView text_usercenter_time=(TextView)findViewById(R.id.usercenter_time);
                long diff = TimeUtils.getMinuteDiff(TimeUtils.getCurrentTime(), sh.readLoginTime());
                if(text_usercenter_time != null)
                    text_usercenter_time.setText(" 已登陆"+diff+"分钟 ");
                transaction.hide(from).show(to).commit();
            }
        }
    }


}
