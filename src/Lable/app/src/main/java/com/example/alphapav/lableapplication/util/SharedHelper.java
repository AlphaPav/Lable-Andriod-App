package com.example.alphapav.lableapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SharedHelper {
    private Context mContext;
    public SharedHelper() {
    }
    public SharedHelper(Context context)
    {
        mContext= context;
    }
    public void saveUser(String username, String password)
    {
        SharedPreferences sp = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
        Log.i("LOGCAT","当前用户 -账户密码已经写入SharedPreference" );
    }

    public void saveEntity(Entity entity){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("last_entity", new Gson().toJson(entity));
        editor.apply();
    }

    public Entity getLastEntity(){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        String json_relation = sp.getString("last_entity", "");
        if(json_relation.equals(""))
            return null;
        Entity entity = new Gson().fromJson(json_relation, Entity.class);
        return  entity;
    }


    public void saveEntityPersonList(List<String> listPerson){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("list_person", new Gson().toJson(listPerson));
        editor.apply();
    }
    public void saveEntityTitleList(List<String> listTitle){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("list_title", new Gson().toJson(listTitle));
        editor.apply();
    }

    public List<String> getLastEntityPersonList(){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        String json_list = sp.getString("list_person", "");
        if(json_list.equals(""))
            return new ArrayList<String>();
        ArrayList<String> listPerson = new Gson().fromJson(json_list, new TypeToken<ArrayList<String>>(){}.getType());
        return listPerson;
    }

    public List<String> getLastEntityTitleList(){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username") ,Context.MODE_PRIVATE);
        String json_list = sp.getString("list_title", "");
        if(json_list.equals(""))
            return new ArrayList<String>();
        ArrayList<String> listTitle = new Gson().fromJson(json_list, new TypeToken<ArrayList<String>>(){}.getType());
        return listTitle;
    }

    public void saveRelation(Relation relation){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("last_relation", new Gson().toJson(relation));
        editor.apply();
    }

    public Relation getLastRelation(){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        String json_relation = sp.getString("last_relation", "");
        if(json_relation.equals(""))
            return null;
        Relation relation = new Gson().fromJson(json_relation, Relation.class);
        return  relation;
    }

    public void recordLastType(String type){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(type.equals("relation") || type.equals("entity")){
            editor.putString("last_type", type);
        }
        else{
            editor.putString("last_type", "");
        }
        editor.apply();
    }

    public String getLastType(){
        SharedPreferences sp = mContext.getSharedPreferences("UNCOMPLETED"+readUser().get("username"), Context.MODE_PRIVATE);
        String type = sp.getString("last_type", "");
        if(type.equals("relation") || type.equals("entity"))
            return type;
        return "";
    }

    public void saveRememberUser(String username, String password)
    {
        SharedPreferences sp = mContext.getSharedPreferences("RemenberUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
        Log.i("LOGCAT","记住密码 -账户密码已经写入SharedPreference" );
    }

    public void saveToken(String token)
    {
        SharedPreferences sp = mContext.getSharedPreferences("Token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token",token);
        editor.putString("login_time",TimeUtils.getCurrentTime()); // to check 30 minutes,then auto log out??
        editor.apply();
        Log.i("LOGCAT","token已经写入SharedPreference" );
    }
    public void saveFontsize(int fontsize)
    {
        SharedPreferences sp = mContext.getSharedPreferences("Font", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("fontsize", fontsize);
        editor.apply();
        Log.i("LOGCAT","fontsize已经写入SharedPreference" );

    }
    public Map<String, String> readUser()
    {
        Map<String,String> data= new HashMap<String,String>();
        SharedPreferences sp = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
        data.put("username", Objects.requireNonNull(sp.getString("username", "")));
        data.put("password", Objects.requireNonNull(sp.getString("password", "")));
        return data;
    }

    public Map<String, String> readRememberUser()
    {
        Map<String,String> data= new HashMap<String,String>();
        SharedPreferences sp = mContext.getSharedPreferences("RemenberUser", Context.MODE_PRIVATE);
        data.put("username", Objects.requireNonNull(sp.getString("username", "")));
        data.put("password", Objects.requireNonNull(sp.getString("password", "")));
        return data;
    }
    public String readLoginTime()
    {
        SharedPreferences sp = mContext.getSharedPreferences("Token", Context.MODE_PRIVATE);
        return sp.getString("login_time", "");
    }
    public String readToken()
    {
        SharedPreferences sp = mContext.getSharedPreferences("Token", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
    public int readFontsize()
    {
        try {
            SharedPreferences sp = mContext.getSharedPreferences("Font", Context.MODE_PRIVATE);

            return sp.getInt("fontsize", 15);

        }catch (Exception e)
        {
            return 15;
        }

    }

}
