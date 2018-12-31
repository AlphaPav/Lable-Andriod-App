package com.example.alphapav.lableapplication.util;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RelationHistory {

    // userName
    private String userName = "";
    private static final String  prefix = "relation_";
    // all history Entity
    public ArrayList<Relation> all_history_relation = new ArrayList<Relation>();

    RelationHistory(String userName){
        this.userName = userName;
    }

    public boolean equalsUser(String userName){
        return this.userName.equals(userName);
    }

    static RelationHistory getNewRelationHistory(String userName, Application app){
        // new an entity history
        RelationHistory relationHistory = new RelationHistory(userName);
        try {
            // read
            FileInputStream fileOutputStream = app.openFileInput(prefix+userName);
            byte[] buffer = new byte[fileOutputStream.available()];
            fileOutputStream.read(buffer);
            fileOutputStream.close();
            // get string
            String jsonStr = new String(buffer, "utf-8");
            relationHistory.all_history_relation = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Relation>>(){}.getType());
            return  relationHistory;
        }catch (FileNotFoundException e){
            relationHistory.all_history_relation = new ArrayList<Relation>();
            return relationHistory;
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public void save2Local(Application app) throws Exception{
        // new an entity history
        String fcontent = new Gson().toJson(all_history_relation);
        // write
        FileOutputStream fileOutputStream = app.openFileOutput(prefix+userName, Context.MODE_PRIVATE);
        fileOutputStream.write(fcontent.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void updateRelation(Relation relation){
        // travel
        Relation find_relation = null;
        for(int i = 0; i < all_history_relation.size(); i++){
            Relation old_relation = all_history_relation.get(i);
            if(old_relation.isEqual(relation)){
                find_relation = old_relation;
                break;
            }
        }
        // if find
        if(find_relation != null){
            all_history_relation.remove(find_relation);
        }
        // add new
        all_history_relation.add(relation);
    }
}
